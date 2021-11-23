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
package com.bilboldev.pixeldungeonskills.items.wands;

import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.actors.Actor;
import com.bilboldev.pixeldungeonskills.actors.Char;
import com.bilboldev.pixeldungeonskills.actors.buffs.Blindness;
import com.bilboldev.pixeldungeonskills.actors.buffs.Buff;
import com.bilboldev.pixeldungeonskills.actors.buffs.Invisibility;
import com.bilboldev.pixeldungeonskills.actors.buffs.Poison;
import com.bilboldev.pixeldungeonskills.actors.hero.Legend;
import com.bilboldev.pixeldungeonskills.actors.mobs.Bestiary;
import com.bilboldev.pixeldungeonskills.actors.mobs.Mob;
import com.bilboldev.pixeldungeonskills.actors.mobs.npcs.NPC;
import com.bilboldev.pixeldungeonskills.actors.mobs.npcs.SummonedPet;
import com.bilboldev.pixeldungeonskills.effects.MagicMissile;
import com.bilboldev.pixeldungeonskills.effects.Pushing;
import com.bilboldev.pixeldungeonskills.effects.Speck;
import com.bilboldev.pixeldungeonskills.effects.particles.ShadowParticle;
import com.bilboldev.pixeldungeonskills.items.weapon.missiles.NinjaBomb;
import com.bilboldev.pixeldungeonskills.mechanics.Ballistica;
import com.bilboldev.pixeldungeonskills.scenes.CellSelector;
import com.bilboldev.pixeldungeonskills.scenes.GameScene;
import com.bilboldev.pixeldungeonskills.scenes.MissionScene;
import com.bilboldev.pixeldungeonskills.sprites.CharSprite;
import com.bilboldev.pixeldungeonskills.sprites.WraithSprite;
import com.bilboldev.pixeldungeonskills.ui.QuickSlot;
import com.bilboldev.pixeldungeonskills.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfMagicCasting extends Wand {

	{
		name = "Wand of Hax";
	}

    public void castSpell(CAST_TYPES casting)
    {
        this.casting = casting;
        MissionScene.selectCell(zapper);
    }


    public void castSpellCost()
    {
        switch (casting)
        {
            case DARK_BOLT:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.passiveB2.getManaCost();
                Dungeon.hero.heroSkills.passiveB2.castTextYell();
                break;
            case DOMINANCE:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.passiveB3.getManaCost();
                Dungeon.hero.heroSkills.passiveB3.castTextYell();
                break;
            case SOUL_SPARK:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.active2.getManaCost();
                Dungeon.hero.heroSkills.active2.castTextYell();
                break;
            case SPARK:
                //Dungeon.hero.MP -= Dungeon.hero.heroSkills.active2.getManaCost();
                Dungeon.hero.skillTree.castSpark();
                break;
            case NINJA_BOMB:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.active2.getManaCost();
                Dungeon.hero.heroSkills.active2.castTextYell();
                break;
        }
    }

    public enum CAST_TYPES  {DARK_BOLT, DOMINANCE, SOUL_SPARK, SPARK, NINJA_BOMB};
    public CAST_TYPES casting = CAST_TYPES.DARK_BOLT;

    protected static CellSelector.Listener zapper = new  CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                curUser = Dungeon.hero;
                final int cell = Ballistica.cast(curUser.pos, target, true, true);
                curUser.sprite.zap(cell);



                final Wand curWand = Legend.haxWand;

                ((WandOfMagicCasting)curWand).castSpellCost();
                curUser.busy();

                curWand.fx(cell, new Callback() {
                    @Override
                    public void call() {
                        curWand.onZap(cell);
                        curWand.wandUsed();
                    }
                });

                Invisibility.dispel();


            }
        }

        @Override
        public String prompt() {
            return "Choose direction to cast";
        }
    };

	@Override
	protected void onZap( int cell ) {
		Char ch = Actor.findChar( cell );
		if (ch != null) {
            if(ch instanceof NPC && casting != CAST_TYPES.SOUL_SPARK)
                return;

            if(!Bestiary.isBoss(ch) && casting == CAST_TYPES.DARK_BOLT) {
                ch.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                Sample.INSTANCE.play(Assets.SND_CURSED);
                SummonedPet minion = new SummonedPet(WraithSprite.class);
                minion.name = "Consumed Soul";
                minion.screams = false;
                minion.HT = ch.HT;
                minion.HP = minion.HT;
                minion.defenseSkill = 5;
                minion.pos = cell;
                GameScene.add(minion);
                minion.sprite.alpha(0);
                minion.sprite.parent.add(new AlphaTweener(minion.sprite, 1, 0.15f));

                ch.die(null);
            }
            else  if(casting == CAST_TYPES.DOMINANCE)
            {
                // Abstraction at its finest (:
                if(!ch.name.equals("Temari") && !ch.name.equals("Necromancer")){
                    ch.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                    Sample.INSTANCE.play(Assets.SND_CURSED);
                    SummonedPet minion = new SummonedPet(ch.sprite.getClass());
                    minion.name = "Enslaved " + ch.name;
                    minion.screams = false;
                    minion.HT = ch.HT;
                    minion.HP = minion.HT;
                    minion.defenseSkill = ch.defenseSkill(Dungeon.hero);
                    minion.pos = cell;
                    GameScene.add(minion);
                    minion.sprite.alpha(0);
                    minion.sprite.parent.add(new AlphaTweener(minion.sprite, 1, 0.15f));
                    ch.sprite.visible = false;
                    ch.die(null);
                }
            }
            else  if(casting == CAST_TYPES.SOUL_SPARK)
            {
                ch.HP = ch.HT;
                ch.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
            }
            else  if(casting == CAST_TYPES.SPARK)
            {
                int damage = (int)Dungeon.hero.skillTree.getSparkDamage();
                ch.damage(damage, Dungeon.hero);
                if(Dungeon.hero.skillTree.getSparkBlind())
                {
                    Buff.prolong( ch, Blindness.class, Random.Int( 3, 6 ) );
                    ch.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
                    ch.sprite.showStatus(CharSprite.WARNING, "Blinded!");
                }
            }
			
		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}
	
	protected void fx( int cell, Callback callback ) {
        if(casting == CAST_TYPES.DARK_BOLT)
		    MagicMissile.shadow( curUser.sprite.parent, curUser.pos, cell, callback, 1 );
        else if(casting == CAST_TYPES.DOMINANCE)
            MagicMissile.shadow(curUser.sprite.parent, curUser.pos, cell, callback, 3);
        else if(casting == CAST_TYPES.SOUL_SPARK)
            MagicMissile.whiteLight(curUser.sprite.parent, curUser.pos, cell, callback);
        else if(casting == CAST_TYPES.SPARK)
            MagicMissile.blueLight(curUser.sprite.parent, curUser.pos, cell, callback);

        if(casting != CAST_TYPES.NINJA_BOMB){
            Sample.INSTANCE.play( Assets.SND_ZAP );
        }

        if(casting == CAST_TYPES.NINJA_BOMB){
            NinjaBomb nb = new NinjaBomb();
            nb.strength = Dungeon.hero.heroSkills.active2.level;
            nb.cast(curUser, cell);
        }
	}
	
	@Override
	public String desc() {
		return
			"The vile blast of this twisted bit of wood will imbue its target " +
			"with a deadly venom. A creature that is poisoned will suffer periodic " +
			"damage until the effect ends. The duration of the effect increases " +
			"with the level of the staff.";
	}
}
