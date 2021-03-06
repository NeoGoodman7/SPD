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

import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.actors.Char;
import com.bilboldev.pixeldungeonskills.actors.buffs.Buff;
import com.bilboldev.pixeldungeonskills.actors.buffs.Frost;
import com.bilboldev.pixeldungeonskills.actors.hero.Hero;
import com.bilboldev.pixeldungeonskills.items.Item;
import com.bilboldev.pixeldungeonskills.sprites.CharSprite;
import com.bilboldev.pixeldungeonskills.sprites.ItemSpriteSheet;
import com.bilboldev.pixeldungeonskills.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FrostBow extends Bow {

	{
		name = "frost bow";
		image = ItemSpriteSheet.ForstBow;


        stackable = false;
	}



	public FrostBow() {
		this( 1 );
	}

	public FrostBow(int number) {
		super();
		quantity = number;
	}


	@Override
	public String desc() {
		return 
			"A magically imbued bow that has a chance to freeze targets.";
	}
	
	@Override
	public Item random() {
		quantity = 1;
		return this;
	}
	
	@Override
	public int price() {
		return quantity * 55;
	}



    @Override
    public void bowSpecial(Char target)
    {
        try {
            if (Random.Int(5) == 1) {
                Buff.prolong(target, Frost.class, Frost.duration(target) * Random.Float(1f, 2f));
                GLog.p("Target frozen!");

                target.sprite.showStatus(CharSprite.NEUTRAL, "Brrrr...");
            } else
                Buff.affect(target, Frost.class);
        }
        catch (Exception ex)
        {

        }
    }
}
