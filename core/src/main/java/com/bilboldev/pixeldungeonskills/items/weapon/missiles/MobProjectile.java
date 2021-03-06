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

import com.bilboldev.pixeldungeonskills.items.Item;
import com.bilboldev.pixeldungeonskills.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class MobProjectile extends MissileWeapon {

	{
		name = "projectile";
		image = ItemSpriteSheet.MOB_PROJECTILE;
	}

	public MobProjectile() {
		this( 1 );
	}

	public MobProjectile(int number) {
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
			"These simple metal spikes are weighted to fly true and " +
			"sting their prey with a flick of the wrist.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 5, 15 );
		return this;
	}

	@Override
	public int price() {
		return quantity * 2;
	}
}
