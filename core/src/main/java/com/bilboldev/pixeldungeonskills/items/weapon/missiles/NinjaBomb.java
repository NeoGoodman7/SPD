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
package com.bilboldev.pixeldungeonskills.items.weapon.missiles;

import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.actors.Actor;
import com.bilboldev.pixeldungeonskills.actors.buffs.Buff;
import com.bilboldev.pixeldungeonskills.actors.buffs.Sleep;
import com.bilboldev.pixeldungeonskills.actors.mobs.Bestiary;
import com.bilboldev.pixeldungeonskills.actors.mobs.Mob;
import com.bilboldev.pixeldungeonskills.actors.mobs.npcs.NPC;
import com.bilboldev.pixeldungeonskills.actors.mobs.npcs.SummonedPet;
import com.bilboldev.pixeldungeonskills.effects.Splash;
import com.bilboldev.pixeldungeonskills.sprites.ItemSpriteSheet;
import com.bilboldev.pixeldungeonskills.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class NinjaBomb extends MissileWeapon {

	public int strength;

	{
		name = "Ninja Bomb";
		image = ItemSpriteSheet.MOB_PROJECTILE;
        stackable = true;
	}

	public NinjaBomb() {
		this( 1 );
	}

	public NinjaBomb(int number) {
		super();
		quantity = number;
	}
	
	@Override
	public int min() {
		return 1;
	}
	
	@Override
	public int max() {
		return 4;
	}
	
	@Override
	public String desc() {
		return 
			"A magical crystal capable of capturing soul essence. Throw this at a weak or weakened foe and capture his spirit.";
	}

    @Override
    public String info() {

        StringBuilder info = new StringBuilder( desc() );


        return info.toString();
    }


    @Override
    protected void onThrow( int cell ) {
        if(Actor.findChar(cell) != null && Actor.findChar(cell) instanceof Mob && (!(Actor.findChar(cell) instanceof NPC) || Actor.findChar(cell) instanceof SummonedPet) && !Bestiary.isBoss(Actor.findChar(cell)) && (Random.Int(10 * strength, 20  * strength) > Actor.findChar(cell).HP || Actor.findChar(cell) instanceof SummonedPet))
        {
            Buff.affect(Actor.findChar(cell), Sleep.class);
            GLog.p("Knocked out " + Actor.findChar(cell).name + "!");
        }

        if (Dungeon.visible[cell]) {
                Sample.INSTANCE.play( Assets.SND_PUFF );
                // Splash.at(cell, Color.parseColor("#50d0d0d0"), 5);
                Splash.at(cell, 0x50d0d0d0, 5);
            }

    }

	@Override
	public int price() {
		return quantity * 12;
	}
}
