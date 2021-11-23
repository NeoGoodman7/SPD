/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
package com.bilboldev.pixeldungeonskills;

import com.bilboldev.pixeldungeonskills.scenes.GameScene;
import com.bilboldev.pixeldungeonskills.scenes.PixelScene;
import com.bilboldev.pixeldungeonskills.scenes.TitleScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PlatformSupport;

public class PixelDungeon extends Game {
	
	public PixelDungeon(PlatformSupport platformSupport) {
		super( TitleScene.class, platformSupport );
		
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.scrolls.ScrollOfUpgrade.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfEnhancement" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.blobs.WaterOfHealth.class,
			"com.watabou.pixeldungeon.actors.blobs.Light" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.rings.RingOfMending.class,
			"com.watabou.pixeldungeon.items.rings.RingOfRejuvenation" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.wands.WandOfReach.class,
			"com.watabou.pixeldungeon.items.wands.WandOfTelekenesis" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.blobs.Foliage.class,
			"com.watabou.pixeldungeon.actors.blobs.Blooming" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.buffs.Shadows.class,
			"com.watabou.pixeldungeon.actors.buffs.Rejuvenation" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.scrolls.ScrollOfPsionicBlast.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfNuclearBlast" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.hero.Hero.class,
			"com.watabou.pixeldungeon.actors.Hero" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.mobs.npcs.Shopkeeper.class,
			"com.watabou.pixeldungeon.actors.mobs.Shopkeeper" );
		// 1.6.1
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.quest.DriedRose.class,
			"com.watabou.pixeldungeon.items.DriedRose" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.mobs.npcs.MirrorImage.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfMirrorImage$MirrorImage" );
		// 1.6.4
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.rings.RingOfElements.class,
			"com.watabou.pixeldungeon.items.rings.RingOfCleansing" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.rings.RingOfElements.class,
			"com.watabou.pixeldungeon.items.rings.RingOfResistance" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.weapon.missiles.Boomerang.class,
			"com.watabou.pixeldungeon.items.weapon.missiles.RangersBoomerang" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.rings.RingOfPower.class,
			"com.watabou.pixeldungeon.items.rings.RingOfEnergy" );
		// 1.7.2
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.plants.Dreamweed.class,
			"com.watabou.pixeldungeon.plants.Blindweed" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.plants.Dreamweed.Seed.class,
			"com.watabou.pixeldungeon.plants.Blindweed$Seed" );
		// 1.7.4
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.weapon.enchantments.Shock.class,
			"com.watabou.pixeldungeon.items.weapon.enchantments.Piercing" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.weapon.enchantments.Shock.class,
			"com.watabou.pixeldungeon.items.weapon.enchantments.Swing" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.scrolls.ScrollOfEnchantment.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfWeaponUpgrade" );
		// 1.7.5
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.scrolls.ScrollOfEnchantment.class,
			"com.watabou.pixeldungeon.items.Stylus" );
		// 1.8.0
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.actors.mobs.FetidRat.class,
			"com.watabou.pixeldungeon.actors.mobs.npcs.Ghost$FetidRat" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.plants.Rotberry.class,
			"com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry" );
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.plants.Rotberry.Seed.class,
			"com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry$Seed" );
		// 1.9.0
		com.watabou.utils.Bundle.addAlias(
			com.bilboldev.pixeldungeonskills.items.wands.WandOfReach.class,
			"com.watabou.pixeldungeon.items.wands.WandOfTelekinesis" );
	}

	@Override
	public void create() {
		super.create();
		
		updateImmersiveMode();
		
		// DisplayMetrics metrics = new DisplayMetrics();
		// instance.getWindowManager().getDefaultDisplay().getMetrics( metrics );
		// boolean landscape = metrics.widthPixels > metrics.heightPixels;
		//
		// if (Preferences.INSTANCE.getBoolean( Preferences.KEY_LANDSCAPE, false ) != landscape) {
		// 	landscape( !landscape );
		// }
		
		Music.INSTANCE.enable( music() );
		Sample.INSTANCE.enable( soundFx() );
		
		Sample.INSTANCE.load( 
			Assets.SND_CLICK, 
			Assets.SND_BADGE, 
			Assets.SND_GOLD,
			
			Assets.SND_DESCEND,
			Assets.SND_STEP,
			Assets.SND_WATER,
			Assets.SND_OPEN,
			Assets.SND_UNLOCK,
			Assets.SND_ITEM,
			Assets.SND_DEWDROP, 
			Assets.SND_HIT, 
			Assets.SND_MISS,
			Assets.SND_EAT,
			Assets.SND_READ,
			Assets.SND_LULLABY,
			Assets.SND_DRINK,
			Assets.SND_SHATTER,
			Assets.SND_ZAP,
			Assets.SND_LIGHTNING,
			Assets.SND_LEVELUP,
			Assets.SND_DEATH,
			Assets.SND_CHALLENGE,
			Assets.SND_CURSED,
			Assets.SND_EVOKE,
			Assets.SND_TRAP,
			Assets.SND_TOMB,
			Assets.SND_ALERT,
			Assets.SND_MELD,
			Assets.SND_BOSS,
			Assets.SND_BLAST,
			Assets.SND_PLANT,
			Assets.SND_RAY,
			Assets.SND_BEACON,
			Assets.SND_TELEPORT,
			Assets.SND_CHARMS,
			Assets.SND_MASTERY,
			Assets.SND_PUFF,
			Assets.SND_ROCKS,
			Assets.SND_BURNING,
			Assets.SND_FALLING,
			Assets.SND_GHOST,
			Assets.SND_SECRET,
			Assets.SND_BONES,
			Assets.SND_BEE,
			Assets.SND_DEGRADE,
			Assets.SND_MIMIC );
	}

	@Override
	public void finish() {
		if (!DeviceCompat.isiOS()) {
			super.finish();
		} else {
			//can't exit on iOS (Apple guidelines), so just go to title screen
			switchScene(TitleScene.class);
		}
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}

	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}

	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}

	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}

	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}

	@Override
	public void destroy(){
		super.destroy();
	}

	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}

	public void updateImmersiveMode() {
		platform.updateImmersiveMode();
	}

	/*
	 * ---> Prefernces
	 */

	public static void landscape( boolean value ){
		Preferences.INSTANCE.put( Preferences.KEY_LANDSCAPE, value );
		((PixelDungeon)PixelDungeon.instance).updateDisplaySize();
	}

	//can return null because we need to directly handle the case of landscape not being set
	// as there are different defaults for different devices
	public static Boolean landscape(){
		if (Preferences.INSTANCE.contains(Preferences.KEY_LANDSCAPE)){
			return Preferences.INSTANCE.getBoolean(Preferences.KEY_LANDSCAPE, false);
		} else {
			return null;
		}
	}

	// *** IMMERSIVE MODE ****

	public static void immerse( boolean value ) {
		Preferences.INSTANCE.put(Preferences.KEY_IMMERSIVE, value );
		((PixelDungeon)PixelDungeon.instance).updateImmersiveMode();
	}

	public static boolean immersed() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_IMMERSIVE, false );
	}
	
	// *****************************
	
	public static void scaleUp( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_SCALE_UP, value );
		switchScene( TitleScene.class );
	}
	
	public static boolean scaleUp() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SCALE_UP, true );
	}

	public static void zoom( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_ZOOM, value );
	}
	
	public static int zoom() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_ZOOM, 0 );
	}
	
	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_MUSIC, value );
	}
	
	public static boolean music() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_MUSIC, true );
	}
	
	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_SOUND_FX, value );
	}
	
	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SOUND_FX, true );
	}

    public static void itemDeg( boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_DEGRADATION, value );
    }

    public static boolean itemDeg() {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_DEGRADATION, true );
    }

	public static boolean skillFontSize() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SKILL_FONT_SIZE, true );
	}

	public static void skillFontSize(boolean value) {
		Preferences.INSTANCE.put( Preferences.KEY_SKILL_FONT_SIZE, value );
	}
	
	public static void brightness( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS, value );
		if (scene() instanceof GameScene) {
			((GameScene)scene()).brightness( value );
		}
	}
	
	public static boolean brightness() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_BRIGHTNESS, false );
	}

	public static void brightnessNew( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS_NEW, value );
		if (scene() instanceof GameScene) {
			((GameScene)scene()).brightness( value );
		}
	}

	public static int brightnessNew() {
		int toReturn = Preferences.INSTANCE.getInt( Preferences.KEY_BRIGHTNESS_NEW, -10 );
		if(toReturn == -10){
			return brightness() ? 2 : -2;
		}

		return toReturn;
	}

	public static boolean secondQuickSlot() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SECOND_QUICK_SLOT, false );
	}

	public static void secondQuickSlot( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_SECOND_QUICK_SLOT, value );
	}

	public static void enabled3d( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_ENABLED_3D, value );
	}

	public static boolean enabled3d() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_ENABLED_3D, false );
	}
	
	public static void donated( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_DONATED, value );
	}
	
	public static int donated() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_DONATED, 0 );
	}

    public static void maidenUnlocked( Boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_ARCHER_MAIDEN, value );
    }

    public static boolean maidenUnlocked() {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_ARCHER_MAIDEN, false );
    }

    public static void disableChampionsUnlocked( Boolean value ) {
        Preferences.INSTANCE.put( Preferences.KEY_DISABLE_CHAMPIONS, value );
    }

    public static boolean disableChampionsUnlocked() {
        return Preferences.INSTANCE.getBoolean( Preferences.KEY_DISABLE_CHAMPIONS, false );
    }
	
	public static void lastClass( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_LAST_CLASS, value );
	}
	
	public static int lastClass() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_CLASS, 0 );
	}
	
	public static void challenges( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_CHALLENGES, value );
	}
	
	public static int challenges() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_CHALLENGES, 0 );
	}
	
	public static void intro( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_INTRO, value );
	}
	
	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_INTRO, true );
	}

	public static void fullscreen( boolean value ){
		Preferences.INSTANCE.put( Preferences.KEY_FULLSCREEN, value );
		PixelDungeon.updateSystemUI();
	}

	public static boolean fullscreen(){
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_FULLSCREEN, false );
	}

	public static void lastVersion( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_LAST_VERSION, value );
	}

	public static int lastVersion() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_VERSION, 0 );
	}
	/*
	 * <--- Preferences
	 */
	
	// public static void reportException( Throwable tr ) {
	// 	Log.e( "PD", Log.getStackTraceString( tr ) );
	// }
}