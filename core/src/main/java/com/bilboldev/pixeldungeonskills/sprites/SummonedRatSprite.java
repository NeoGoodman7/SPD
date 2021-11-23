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
package com.bilboldev.pixeldungeonskills.sprites;

import com.watabou.glwrap.Matrix;
import com.bilboldev.pixeldungeonskills.actors.mobs.Mob;

public class SummonedRatSprite extends RatSprite {
	public SummonedRatSprite() {
		super();
	}

    @Override
    public void update() {
        super.update();
        tint( 1.4f, 1.00f, 1.00f, 0.2f );
        alpha(0.7f);
    }
}
