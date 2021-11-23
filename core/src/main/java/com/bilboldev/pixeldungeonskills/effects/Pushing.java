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
package com.bilboldev.pixeldungeonskills.effects;

import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.bilboldev.pixeldungeonskills.actors.Actor;
import com.bilboldev.pixeldungeonskills.actors.Char;
import com.bilboldev.pixeldungeonskills.sprites.CharSprite;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PointF;

public class Pushing extends Actor {

	{
		actPriority = VFX_PRIO;
	}

	private CharSprite sprite;
	private int from;
	private int to;
	
	private Effect effect;
	
	public Pushing( Char ch, int from, int to ) {
		sprite = ch.sprite;
		this.from = from;
		this.to = to;
	}
	
	@Override
	protected boolean act() {
		if (sprite != null) {
			DeviceCompat.log("Actor", "Pushing act sprite pos:[" + sprite.x + "," + sprite.y + "]," +
					" from:" + from + " to:" + to );
			if (effect == null) {
				effect = new Effect();
			}
			return false;

		} else {

			Actor.remove( Pushing.this );
			return true;
		}
	}

	public class Effect extends Visual {

		private static final float DELAY = 0.15f;
		
		private PointF end;
		
		private float delay;
		
		public Effect() {
			super( 0, 0, 0, 0 );
			
			point( sprite.worldToCamera( from ) );
			end = sprite.worldToCamera( to );
			
			speed.set( 2 * (end.x - x) / DELAY, 2 * (end.y - y) / DELAY );
			acc.set( -speed.x / DELAY, -speed.y / DELAY );
			
			delay = 0;
			
			sprite.parent.add( this );
		}
		
		@Override
		public void update() {
			super.update();
			DeviceCompat.log("Actor", "delay:" + delay + " Game.elapsed:" + Game.elapsed
				+ " DELAY:" + DELAY);

			if ((delay += Game.elapsed) < DELAY) {
				
				sprite.x = x;
				sprite.y = y;

				DeviceCompat.log("Actor", "Pushing update->["+ x + "," + y + "]");
				
			} else {
				
				sprite.point( end );
				DeviceCompat.log("Actor", "Pushing end->["+ end.x + "," + end.y + "]");

				killAndErase();
				Actor.remove( Pushing.this );
				
				next();
			}
		}
	}

}
