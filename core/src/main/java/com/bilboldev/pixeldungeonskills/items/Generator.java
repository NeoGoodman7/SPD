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
package com.bilboldev.pixeldungeonskills.items;

import java.util.HashMap;

import com.watabou.noosa.audio.Sample;
import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Badges;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.Statistics;
import com.bilboldev.pixeldungeonskills.actors.buffs.Hunger;
import com.bilboldev.pixeldungeonskills.actors.hero.Hero;
import com.bilboldev.pixeldungeonskills.effects.Speck;
import com.bilboldev.pixeldungeonskills.effects.SpellSprite;
import com.bilboldev.pixeldungeonskills.items.armor.*;
import com.bilboldev.pixeldungeonskills.items.bags.Bag;
import com.bilboldev.pixeldungeonskills.items.food.ChargrilledMeat;
import com.bilboldev.pixeldungeonskills.items.food.Food;
import com.bilboldev.pixeldungeonskills.items.food.MysteryMeat;
import com.bilboldev.pixeldungeonskills.items.food.OverpricedRation;
import com.bilboldev.pixeldungeonskills.items.food.Pasty;
import com.bilboldev.pixeldungeonskills.items.potions.*;
import com.bilboldev.pixeldungeonskills.items.rings.*;
import com.bilboldev.pixeldungeonskills.items.scrolls.*;
import com.bilboldev.pixeldungeonskills.items.wands.*;
import com.bilboldev.pixeldungeonskills.items.weapon.*;
import com.bilboldev.pixeldungeonskills.items.weapon.melee.*;
import com.bilboldev.pixeldungeonskills.items.weapon.missiles.*;
import com.bilboldev.pixeldungeonskills.plants.*;
import com.bilboldev.pixeldungeonskills.scenes.GameScene;
import com.bilboldev.pixeldungeonskills.scenes.GauntletScene;
import com.bilboldev.pixeldungeonskills.utils.GLog;
import com.watabou.utils.Random;

public class Generator {

	public static enum Category {
		WEAPON	( 15,	Weapon.class ),
		ARMOR	( 10,	Armor.class ),
		POTION	( 50,	Potion.class ),
		SCROLL	( 40,	Scroll.class ),
		WAND	( 4,	Wand.class ),
		RING	( 2,	Ring.class ),
		SEED	( 5,	Plant.Seed.class ),
		FOOD	( 0,	Food.class ),
		GOLD	( 50,	Gold.class ),
		MISC	( 5,	Item.class ),
		MELEE_WEAPON	( 15,	Weapon.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	};
	
	private static HashMap<Category,Float> categoryProbs = new HashMap<Generator.Category, Float>();
	
	static {
		
		Category.GOLD.classes = new Class<?>[]{ 
			Gold.class };
		Category.GOLD.probs = new float[]{ 1 };
		
		Category.SCROLL.classes = new Class<?>[]{ 
			ScrollOfIdentify.class, 
			ScrollOfTeleportation.class, 
			ScrollOfRemoveCurse.class, 
			ScrollOfRecharging.class,
			ScrollOfMagicMapping.class,
			ScrollOfChallenge.class,
			ScrollOfTerror.class,
			ScrollOfLullaby.class,
			ScrollOfPsionicBlast.class,
			ScrollOfMirrorImage.class,
			ScrollOfUpgrade.class,
			ScrollOfEnchantment.class,
                ScrollOfHome.class,
                ScrollOfSacrifice.class,
                ScrollOfBloodyRitual.class,
                ScrollOfSkill.class};
		Category.SCROLL.probs = new float[]{ 30, 10, 15, 10, 15, 12, 8, 8, 4, 6, 0, 1, 10, 1, 5, 5 };
		
		Category.POTION.classes = new Class<?>[]{ 
			PotionOfHealing.class, 
			PotionOfExperience.class,
			PotionOfToxicGas.class, 
			PotionOfParalyticGas.class,
			PotionOfLiquidFlame.class,
			PotionOfLevitation.class,
			PotionOfStrength.class,
			PotionOfMindVision.class,
			PotionOfPurity.class,
			PotionOfInvisibility.class,
			PotionOfMight.class,
			PotionOfFrost.class,
            PotionOfMana.class};
		Category.POTION.probs = new float[]{ 45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10, 20 };
		
		Category.WAND.classes = new Class<?>[]{ 
			WandOfTeleportation.class, 
			WandOfSlowness.class,
			WandOfFirebolt.class,
			WandOfRegrowth.class,
			WandOfPoison.class,
			WandOfBlink.class,
			WandOfLightning.class,
			WandOfAmok.class,
			WandOfReach.class,
			WandOfFlock.class,
			WandOfMagicMissile.class,
			WandOfDisintegration.class,
			WandOfAvalanche.class };
		Category.WAND.probs = new float[]{ 10, 10, 15, 6, 10, 11, 15, 10, 6, 10, 0, 5, 5 };
		
		Category.WEAPON.classes = new Class<?>[]{ 
			Dagger.class, 
			Knuckles.class,
			Quarterstaff.class, 
			Spear.class, 
			Mace.class, 
			Sword.class, 
			Longsword.class,
			BattleAxe.class,
			WarHammer.class, 
			Glaive.class,
			ShortSword.class,
			Dart.class,
			Javelin.class,
			IncendiaryDart.class,
			CurareDart.class,
			Shuriken.class,
			Boomerang.class,
			Tamahawk.class,
                Bow.class,
                FrostBow.class,
                FlameBow.class,
                Arrow.class,
                BombArrow.class,
                NecroBlade.class,
				SoulBlade.class,
                DualSwords.class
                };
		Category.WEAPON.probs = new float[]{ 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 0, 0, 10, 10, 10, 10, 0, 10, 0, 1, 1, 3, 1, 3,3, 3 };
		
		Category.ARMOR.classes = new Class<?>[]{ 
			ClothArmor.class, 
			LeatherArmor.class, 
			MailArmor.class, 
			ScaleArmor.class, 
			PlateArmor.class };
		Category.ARMOR.probs = new float[]{ 1, 1, 1, 1, 1 };
		
		Category.FOOD.classes = new Class<?>[]{ 
			Food.class, 
			Pasty.class,
			MysteryMeat.class };
		Category.FOOD.probs = new float[]{ 4, 1, 0 };
			
		Category.RING.classes = new Class<?>[]{ 
			RingOfMending.class,
			RingOfDetection.class,
			RingOfShadows.class,
			RingOfPower.class,
			RingOfHerbalism.class,
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfSatiety.class,
			RingOfHaste.class,
			RingOfElements.class,
			RingOfHaggler.class,
			RingOfThorns.class };
		Category.RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 };
		
		Category.SEED.classes = new Class<?>[]{ 
			Firebloom.Seed.class,
			Icecap.Seed.class,
			Sorrowmoss.Seed.class,
			Dreamweed.Seed.class,
			Sungrass.Seed.class,
			Earthroot.Seed.class,
			Fadeleaf.Seed.class,
			Rotberry.Seed.class };
		Category.SEED.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 0 };
		
		Category.MISC.classes = new Class<?>[]{ 
			Bomb.class,
			Honeypot.class};
		Category.MISC.probs = new float[]{ 2, 1 };

		Category.MELEE_WEAPON.classes = new Class<?>[]{
				Dagger.class,
				Knuckles.class,
				Quarterstaff.class,
				Spear.class,
				Mace.class,
				Sword.class,
				Longsword.class,
				BattleAxe.class,
				WarHammer.class,
				Glaive.class,
				ShortSword.class
		};

		Category.MELEE_WEAPON.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	}
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}

	public static Item randomConsumable() {
		try {
		Category cat = Category.GOLD;

		if(Random.Float() < 0.5) {
			cat = Category.FOOD;
			if (Random.Float() < 0.65) {
				cat = Category.SEED;
			}
		}
		return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
		} catch (Exception e) {

			return null;

		}
	}

	public static Item randomGauntletArmor() {
		try {
			Armor armor = Dungeon.hero.belongings.armor;
			if(armor == null){
				return new ClothArmor().identify();
			}

			Armor newArmor = armor.getClass().newInstance();
			newArmor.level = armor.level + 1;
			newArmor.STR = armor.STR - 1;
			newArmor.identify();

			if(Random.Int(100) < newArmor.level * 15 && newArmor.tier < 5){
				int targetTier = armor.tier + 1;
				int safety = 20;
				Armor betterArmor = null;
				do{
					safety--;
					betterArmor = (Armor)((Item)Category.ARMOR.classes[Random.chances( Category.ARMOR.probs )].newInstance()).random();
				}while (betterArmor.tier != targetTier && safety > 0);

				if(safety != 0){
					betterArmor.identify();
					if(newArmor.level > 1)
					{
						betterArmor.level = newArmor.level - 1;
						betterArmor.STR -= betterArmor.level - 1;
					}
					return betterArmor;
				}
			}

			return newArmor;
		} catch (Exception e) {

			return new ClothArmor().identify();

		}
	}

	public static Item randomGauntletWeapon() {
		try {
			MeleeWeapon weapon = (MeleeWeapon) Dungeon.hero.belongings.weapon;
			if(weapon == null){
				return new Dagger().identify();
			}

			MeleeWeapon newWeapon = weapon.getClass().newInstance();
			newWeapon.level = weapon.level + 1;
			newWeapon.STR = weapon.STR - 1;
			newWeapon.identify();

			// Swap weapons in same tier
			if(Random.Int(100) < 50){
				int targetTier = weapon.tier;
				int safety = 20;
				MeleeWeapon betterWeapon = null;
				do{
					safety--;
					betterWeapon = (MeleeWeapon)((Item)Category.MELEE_WEAPON.classes[Random.chances( Category.MELEE_WEAPON.probs )].newInstance()).random();
				}while (betterWeapon.tier != targetTier && safety > 0);

				if(safety != 0){
					betterWeapon.level = newWeapon.level;
					betterWeapon.STR = newWeapon.STR;
					betterWeapon.identify();
					return betterWeapon;
				}
			}

			// Better tier
			if(Random.Int(100) < newWeapon.level * 15 && newWeapon.tier < 5){
				int targetTier = weapon.tier + 1;
				int safety = 20;
				MeleeWeapon betterWeapon = null;
				do{
					safety--;
					betterWeapon = (MeleeWeapon)((Item)Category.MELEE_WEAPON.classes[Random.chances( Category.MELEE_WEAPON.probs )].newInstance()).random();
				}while (betterWeapon.tier != targetTier && safety > 0);

				if(safety != 0){
					betterWeapon.identify();
					if(newWeapon.level > 1){
						betterWeapon.level = newWeapon.level - 1;
						betterWeapon.STR -= betterWeapon.level - 1;
					}
					return betterWeapon;
				}
			}

			return newWeapon;
		} catch (Exception e) {
			return new Knuckles().identify();
		}
	}

	public static Item randomGauntletMisc() {
		try {
			if(Random.Int(100) < 25){
				return new ChargrilledMeat(){
					@Override
					public void execute( Hero hero, String action ) {
						if (action.equals( AC_EAT )) {

							((Hunger)hero.buff( Hunger.class )).satisfy( energy );
							GLog.i( message );

							switch (hero.heroClass) {
								case GAUNTLET:
								case WARRIOR:
									if (hero.HP < hero.HT) {
										hero.HP = Math.min( hero.HP + 5, hero.HT );
										hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
									}
									break;
								case MAGE:
									hero.belongings.charge( false );
									ScrollOfRecharging.charge( hero );
									break;
								case ROGUE:
								case HUNTRESS:
									break;
							}

							hero.sprite.operate( hero.pos );
							hero.busy();
							SpellSprite.show( hero, SpellSprite.FOOD );
							Sample.INSTANCE.play( Assets.SND_EAT );

							hero.spend( 0.1f );

							Statistics.foodEaten++;
							Badges.validateFoodEaten();

						} else {

							super.execute( hero, action );

						}
					}
				};
			}

			if(Random.Int(100) < 25){
				return new ScrollOfSkill().identify();
			}

			if(Random.Int(100) < 50){
				return new Food() {
					@Override
					public void execute( Hero hero, String action ) {
						if (action.equals( AC_EAT )) {

							((Hunger)hero.buff( Hunger.class )).satisfy( energy );
							GLog.i( message );

							switch (hero.heroClass) {
								case GAUNTLET:
								case WARRIOR:
									if (hero.HP < hero.HT) {
										hero.HP = Math.min( hero.HP + 5, hero.HT );
										hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
									}
									break;
								case MAGE:
									hero.belongings.charge( false );
									ScrollOfRecharging.charge( hero );
									break;
								case ROGUE:
								case HUNTRESS:
									break;
							}

							hero.sprite.operate( hero.pos );
							hero.busy();
							SpellSprite.show( hero, SpellSprite.FOOD );
							Sample.INSTANCE.play( Assets.SND_EAT );

							hero.spend( 0.1f );

							Statistics.foodEaten++;
							Badges.validateFoodEaten();

						} else {

							super.execute( hero, action );

						}
					}
				};
			}


			if(GauntletScene.level + 1> Dungeon.hero.lvl && Random.Int(100) < 50){
				return new PotionOfExperience().identify();
			}

			if(GauntletScene.level * 2 + 10> Dungeon.hero.STR && Random.Int(100) < 50){
				return new PotionOfStrength().identify();
			}

			return new PotionOfHealing().identify();

		} catch (Exception e) {
			return new PotionOfHealing().identify();
		}
	}
	
	public static Item random( Category cat ) {
		try {
			
			categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			return null;
			
		}
	}
	
	public static Armor randomArmor() throws Exception {
		
		int curStr = Hero.STARTING_STR + Dungeon.potionOfStrength;
		
		Category cat = Category.ARMOR;
		
		Armor a1 = (Armor)cat.classes[Random.chances( cat.probs )].newInstance();
		Armor a2 = (Armor)cat.classes[Random.chances( cat.probs )].newInstance();
		
		a1.random();
		a2.random();
		
		return Math.abs( curStr - a1.STR ) < Math.abs( curStr - a2.STR ) ? a1 : a2;
	}
	
	public static Weapon randomWeapon() throws Exception {
		
		int curStr = Hero.STARTING_STR + Dungeon.potionOfStrength;
		
		Category cat = Category.WEAPON;
		
		Weapon w1 = (Weapon)cat.classes[Random.chances( cat.probs )].newInstance();
		Weapon w2 = (Weapon)cat.classes[Random.chances( cat.probs )].newInstance();
		
		w1.random();
		w2.random();
		
		return Math.abs( curStr - w1.STR ) < Math.abs( curStr - w2.STR ) ? w1 : w2;
	}
}
