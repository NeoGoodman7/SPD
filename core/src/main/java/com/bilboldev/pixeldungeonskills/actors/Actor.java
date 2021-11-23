/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.bilboldev.pixeldungeonskills.actors;

import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.TimeUtils;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.Statistics;
import com.bilboldev.pixeldungeonskills.actors.blobs.Blob;
import com.bilboldev.pixeldungeonskills.actors.buffs.Buff;
import com.bilboldev.pixeldungeonskills.actors.hero.Hero;
import com.bilboldev.pixeldungeonskills.actors.mobs.Mob;
import com.bilboldev.pixeldungeonskills.levels.Level;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.SparseArray;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Actor implements Bundlable {
	
	public static final float TICK	= 1f;

	public float time;
	
	protected int id = 0;

	//default priority values for general actor categories
	//note that some specific actors pick more specific values
	//e.g. a buff acting after all normal buffs might have priority BUFF_PRIO + 1
	protected static final int VFX_PRIO    = 100;   //visual effects take priority
	protected static final int HERO_PRIO   = 0;     //positive is before hero, negative after
	protected static final int BLOB_PRIO   = -10;   //blobs act after hero, before mobs
	protected static final int MOB_PRIO    = -20;   //mobs act between buffs and blobs
	protected static final int BUFF_PRIO   = -30;   //buffs act last in a turn
	private static final int   DEFAULT     = -100;  //if no priority is given, act after all else

	//used to determine what order actors act in if their time is equal. Higher values act earlier.
	protected int actPriority = DEFAULT;
	
	protected abstract boolean act();
	
	protected void spend( float time ) {
		this.time += time;
		// DeviceCompat.log("Actor", "spend " + this + " time:" + time);
		// if (this instanceof Hero) {
		// 	dumpStack();
		// }
		//if time is very close to a whole number, round to a whole number to fix errors
		float ex = Math.abs(this.time % 1f);
		if (ex < .001f){
			this.time = Math.round(this.time);
		}
	}
	
	protected void postpone( float time ) {
		if (this.time < now + time) {
			this.time = now + time;
			// DeviceCompat.log("Actor", "postpone " + this + " time:" + time);
			//if time is very close to a whole number, round to a whole number to fix errors
			float ex = Math.abs(this.time % 1f);
			if (ex < .001f){
				this.time = Math.round(this.time);
			}
		}
	}
	
	public float cooldown() {
		return time - now;
	}
	
	protected void diactivate() {
		time = Float.MAX_VALUE;
	}
	
	protected void onAdd() {}
	
	protected void onRemove() {}
	
	private static final String TIME	= "time";
	private static final String ID		= "id";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( TIME, time );
		bundle.put( ID, id );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		time = bundle.getFloat( TIME );
		id = bundle.getInt( ID );
	}
	
	public int id() {
		if (id > 0) {
			return id;
		} else {
			int max = 0;
			for (Actor a : all) {
				if (a.id > max) {
					max = a.id;
				}
			}
			return (id = max + 1);
		}
	}
	
	// **********************
	// *** Static members ***
	
	private static HashSet<Actor> all = new HashSet<Actor>();
	private static Actor current;
	private static Actor previous;

	public static Actor getCurrent() {
		return current;
	}
	
	private static SparseArray<Actor> ids = new SparseArray<Actor>();
	
	private static float now = 0;
	
	private static Char[] chars = new Char[Level.LENGTH];
	
	public static void clear() {
		
		now = 0;
		
		Arrays.fill( chars, null );
		all.clear();
		
		ids.clear();
	}
	
	public static void fixTime() {
		
		if (Dungeon.hero != null && all.contains( Dungeon.hero )) {
			Statistics.duration += now;
		}
		
		float min = Float.MAX_VALUE;
		for (Actor a : all) {
			if (a.time < min) {
				min = a.time;
			}
		}
		for (Actor a : all) {
			a.time -= min;
		}
		now = 0;
	}
	
	public static void init() {

		add( Dungeon.hero );

		for (Mob mob : Dungeon.level.mobs) {
			add( mob );
		}
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			add( blob );
		}
		
		current = null;
	}
	
	public static void occupyCell( Char ch ) {
		chars[ch.pos] = ch;
	}
	
	public static void freeCell( int pos ) {
		chars[pos] = null;
	}
	
	/*protected*/public void next() {
		if (current == this) {
			// if (this instanceof FirstWave.Temari || this instanceof FirstWave.HostileSkeleton) {
			// 	dumpStack();
			// }
			current = null;
		}
	}

	private static int frame = 0;
	public static int getFrame() { return frame;}
	private static float MAX_TIME_IN_PROGRESS = 100;
	public static void process(IProcessValidator processValidator) {
		frame++;

		if (processValidator != null && processValidator.check()) {
			return;
		}

		if (processValidator == null) {
			if (current != null) {
				return;
			}
		}

		boolean doNext;
		float start = TimeUtils.millis();
		do {
			now = Float.MAX_VALUE;
			previous = current;
			current = null;
			Arrays.fill( chars, null );
			
			for (Actor actor : all) {
				if (actor.time < now || (actor.time == now && (current == null || actor.actPriority > current.actPriority))) {
					now = actor.time;
					current = actor;
				}
				
				if (actor instanceof Char) {
					Char ch = (Char)actor;
					chars[ch.pos] = ch;
				}
			}

			if (current != null) {
				if (current instanceof Char && ((Char)current).sprite != null && ((Char)current).sprite.isMoving) {
					dumpCurrent(current, true, false);
					// If it's character's turn to act, but its sprite 
					// is moving, wait till the movement is over
					current = null;
					break;
				}

				//if(current instanceof  Char && ((Char)current).sprite != null)
					previous = current;
					doNext = current.act();
					dumpCurrent(previous,false, doNext);
				//else doNext = true;
				if (doNext && !Dungeon.hero.isAlive()) {
					doNext = false;
					current = null;
				}
			} else {
				doNext = false;
				dumpCurrent(current,false, false);
			}
			float elapsed = TimeUtils.millis() - start;
			if (elapsed >= MAX_TIME_IN_PROGRESS) {
				break;
			}
		} while (doNext);
	}
	
	public static void add( Actor actor ) {
		add( actor, now );
	}
	
	public static void addDelayed( Actor actor, float delay ) {
		add( actor, now + delay );
	}
	
	private static void add( Actor actor, float time ) {
		
		if (all.contains( actor )) {
			return;
		}
		
		if (actor.id > 0) {
			ids.put( actor.id,  actor );
		}
		
		all.add( actor );
		actor.time += time;
		actor.onAdd();
		
		if (actor instanceof Char) {
			Char ch = (Char)actor;
			chars[ch.pos] = ch;
			for (Buff buff : ch.buffs()) {
				all.add( buff );
				buff.onAdd();
			}
		}
	}
	
	public static void remove( Actor actor ) {
		
		if (actor != null) {
			all.remove( actor );
			actor.onRemove();
			
			if (actor.id > 0) {
				ids.remove( actor.id );
			}
		}
	}
	
	public static Char findChar( int pos ) {
		return pos >= 0 && pos < chars.length ? chars[pos] : null ;
	}

	public static Actor findById( int id ) {
		return ids.get( id );
	}
	
	public static HashSet<Actor> all() {
		return all;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() +  "{" + "time=" + time + ", id=" + id + ", actPriority=" + actPriority + "}@" + hashCode();
	}

	public static void dump() {
		StringBuilder buffer = new StringBuilder();
		for (Actor actor : all) {
			buffer.append( actor.toString() + "\n");
		}
		DeviceCompat.log("Actor", "dump actors:\n" + buffer.toString() +
				"\nnow:" + now + "\ncurrent:" + current + "\nhero:" + Dungeon.hero
		+ "\ntotal actors:" + all.size());
	}

	public static void dumpCurrent(Actor current, boolean isMoving, boolean doNext) {
		String action = "";
		if (current instanceof Mob) {
			action = ((Mob)current).state != null ? ((Mob)current).state.toString() : "";
		} else if (current instanceof Hero) {
			action = ((Hero)current).curAction != null ? ((Hero)current).curAction.toString() : "";
		}
		DeviceCompat.log("Actor", "\ncurrent:" + current + "\nhero:" + Dungeon.hero
				+ "\nhero sprite pos:[" + Dungeon.hero.sprite.x + "," + Dungeon.hero.sprite.y + "]"
				+ "\nhero pos:" + Dungeon.hero.pos
				+ "\ntotal actors:" + all.size()+"\nnow:" + now
		+ "\naction:" + action
		+ "\nframe:" + frame
	 + "\nisMoving:" + isMoving
		+ "\ndoNext:" + doNext);
	}

	public static void dumpStack() {
		Throwable t = new Throwable();
		t.printStackTrace();
	}

	public interface IProcessValidator {
		boolean check();
	}
}
