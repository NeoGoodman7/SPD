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
package com.bilboldev.pixeldungeonskills.actors.buffs;

import com.bilboldev.pixeldungeonskills.actors.hero.Hero;
import com.bilboldev.pixeldungeonskills.actors.skills.CurrentSkills;
import com.bilboldev.pixeldungeonskills.items.rings.RingOfMending;

public class Regeneration extends Buff {

	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}

	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT && !((Hero)target).isStarving()) {
				target.HP += 1;
			}
			
			int bonus = 0;
			for (Buff buff : target.buffs( RingOfMending.Rejuvenation.class )) {
				bonus += ((RingOfMending.Rejuvenation)buff).level;
			}

            bonus += ((Hero)target).heroSkills.passiveA2.healthRegenerationBonus(); // <-- Warrior regeneration if present
			bonus += ((Hero)target).skillTree.getRegenerationBonus();

			spend( (float)(REGENERATION_DELAY / Math.pow( 1.2, bonus )) );
			
		} else {
			
			diactivate();
			
		}

		return true;
	}
}
