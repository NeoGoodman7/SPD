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
package com.bilboldev.pixeldungeonskills.scenes;

import com.bilboldev.pixeldungeonskills.items.Heap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Badges;
import com.bilboldev.pixeldungeonskills.Difficulties;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.levels.gauntlet.GauntletLevel2;
import com.bilboldev.pixeldungeonskills.sprites.GauntletSprite;
import com.bilboldev.pixeldungeonskills.thetiles.DungeonTerrainTilemap;
import com.bilboldev.pixeldungeonskills.thetiles.DungeonTilemap;
import com.bilboldev.pixeldungeonskills.thetiles.DungeonTilemapOld;
import com.bilboldev.pixeldungeonskills.thetiles.FogOfWar;
import com.bilboldev.pixeldungeonskills.PixelDungeon;
import com.bilboldev.pixeldungeonskills.Statistics;
import com.bilboldev.pixeldungeonskills.actors.Actor;
import com.bilboldev.pixeldungeonskills.actors.blobs.Blob;
import com.bilboldev.pixeldungeonskills.actors.mobs.ColdGirl;
import com.bilboldev.pixeldungeonskills.actors.mobs.Mob;
import com.bilboldev.pixeldungeonskills.effects.Flare;
import com.bilboldev.pixeldungeonskills.items.Item;
import com.bilboldev.pixeldungeonskills.items.potions.Potion;
import com.bilboldev.pixeldungeonskills.items.wands.WandOfBlink;
import com.bilboldev.pixeldungeonskills.levels.Level;
import com.bilboldev.pixeldungeonskills.levels.MovieLevel;
import com.bilboldev.pixeldungeonskills.levels.RegularLevel;
import com.bilboldev.pixeldungeonskills.levels.features.Chasm;
import com.bilboldev.pixeldungeonskills.plants.Plant;
import com.bilboldev.pixeldungeonskills.sprites.LegendSprite;
import com.bilboldev.pixeldungeonskills.ui.AttackIndicator;
import com.bilboldev.pixeldungeonskills.ui.BusyIndicator;
import com.bilboldev.pixeldungeonskills.ui.GameLog;
import com.bilboldev.pixeldungeonskills.ui.GauntletStatusPane;
import com.bilboldev.pixeldungeonskills.ui.GauntletToolbar;
import com.bilboldev.pixeldungeonskills.ui.HealthIndicator;
import com.bilboldev.pixeldungeonskills.ui.MissionStatusPane;
import com.bilboldev.pixeldungeonskills.ui.MissionToolbar;
import com.bilboldev.pixeldungeonskills.utils.GLog;
import com.bilboldev.pixeldungeonskills.windows.WndBag.Mode;
import com.bilboldev.pixeldungeonskills.windows.WndGame;
import com.bilboldev.pixeldungeonskills.windows.WndStory;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class GauntletScene extends GameScene {


	private static GauntletScene gauntletScene;

	public LegendSprite hero;
	public static boolean scenePause = false;
	protected GauntletToolbar toolbar;

	public static int level = 1;

	@Override
	public void create() {
		gauntletScene = this;
		Music.INSTANCE.play(Assets.TUNE, true);
		Music.INSTANCE.volume(1f);

		PixelDungeon.lastClass( Dungeon.hero.heroClass.ordinal() );

		super.originalCreate();
		Camera.main.zoom( defaultZoom + PixelDungeon.zoom() );

		scene = this;

		terrain = new Group();
		add( terrain );

		water = new SkinnedBlock(
				Level.WIDTH * DungeonTilemap.SIZE,
				Level.HEIGHT * DungeonTilemap.SIZE,
				Dungeon.level.waterTex() );
		terrain.add( water );

		ripples = new Group();
		terrain.add( ripples );

		tiles = new DungeonTilemapOld();
		terrain.add( tiles );

		Dungeon.level.addVisuals( this );

		plants = new Group();
		add( plants );

		int size = Dungeon.level.plants.size;
		List<Plant> plantList = Dungeon.level.plants.valueList();
		for (int i=0; i < size; i++) {
			addPlantSprite( plantList.get( i ) );
		}

		heaps = new Group();
		add( heaps );

		size = Dungeon.level.heaps.size;
		List<Heap> heapList = Dungeon.level.heaps.valueList();
		for (int i=0; i < size; i++) {
			addHeapSprite( heapList.get( i ) );
		}

		emitters = new Group();
		effects = new Group();
		emoicons = new Group();

		mobs = new Group();
		add( mobs );

		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}

		add( emitters );
		add( effects );

		gases = new Group();
		add( gases );

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}

		fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
		fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
		add( fog );

		brightness( PixelDungeon.brightness() );


		spells = new Group();
		add( spells );

		statuses = new Group();
		add( statuses );

		add( emoicons );

		hero = new GauntletSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );

		add( new HealthIndicator() );

		add( cellSelector = new CellSelector( tiles ) );

		GauntletStatusPane sb = new GauntletStatusPane();
		sb.camera = uiCamera;
		sb.setSize( uiCamera.width, 0 );
		add( sb );

		toolbar = new GauntletToolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );

		AttackIndicator attack = new AttackIndicator();
		attack.camera = uiCamera;
		attack.setPos(
				uiCamera.width - attack.width(),
				toolbar.top() - attack.height() );
		add( attack );

		log = new GameLog();
		log.camera = uiCamera;
		log.setRect( 0, toolbar.top(), attack.left(),  0 );
		add( log );

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add( busy );

		switch (InterlevelScene.mode) {
			case RESURRECT:
				WandOfBlink.appear( Dungeon.hero, Dungeon.level.entrance );
				new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
				break;
			case RETURN:
				WandOfBlink.appear(  Dungeon.hero, Dungeon.hero.pos );
				break;
			case FALL:
				Chasm.heroLand();
				break;
			case DESCEND:
				switch (Dungeon.depth) {
					case 1:
						WndStory.showChapter( WndStory.ID_SEWERS );
						if(PixelDungeon.itemDeg() == false)
							WndStory.showStory( TXT_WARN_DEGRADATION );
						break;
					case 6:
						WndStory.showChapter( WndStory.ID_PRISON );
						break;
					case 11:
						WndStory.showChapter( WndStory.ID_CAVES );
						break;
					case 16:
						WndStory.showChapter( WndStory.ID_METROPOLIS );
						break;
					case 22:
						WndStory.showChapter( WndStory.ID_HALLS );
						break;
				}
				if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
					Badges.validateNoKilling();
				}
				break;
			default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}

		// Camera.main.target = hero;
		Camera.main.focusOn(hero);

		if (InterlevelScene.mode != InterlevelScene.Mode.NONE && Dungeon.depth != 0) {
			if (Dungeon.depth < Statistics.deepestFloor) {
				GLog.h( TXT_WELCOME_BACK, Dungeon.depth );
			} else {
				if(Dungeon.depth != ColdGirl.FROST_DEPTH) {
					GLog.h(TXT_WELCOME, Dungeon.depth);
					Sample.INSTANCE.play(Assets.SND_DESCEND);
				}
				else
				{
					GLog.h(TXT_FROST);
					Sample.INSTANCE.play(Assets.SND_TELEPORT);
				}
			}
			switch (Dungeon.level.feeling) {
				case CHASM:
					GLog.w( TXT_CHASM );
					break;
				case WATER:
					GLog.w( TXT_WATER );
					break;
				case GRASS:
					GLog.w( TXT_GRASS );
					break;
				default:
			}
			if (Dungeon.level instanceof RegularLevel &&
					((RegularLevel) Dungeon.level).secretDoors > Random.IntRange( 3, 4 )) {
				GLog.w( TXT_SECRETS );
			}
			if (Dungeon.nightMode && !Dungeon.bossLevel()) {
				GLog.w( TXT_NIGHT_MODE );
			}

			InterlevelScene.mode = InterlevelScene.Mode.NONE;

			fadeIn();
		}
	}

	public void destroy() {

		scene = null;
		//	Badges.saveGlobal();

		super.destroy();
	}

	@Override
	public /*synchronized*/ void onPause() {
		//try {
		//	Dungeon.saveAll();
		//	Badges.saveGlobal();
		//} catch (IOException e) {
		//
		//}
	}

	// @Override
	// public /*synchronized*/ void update() {
	// 	if (Dungeon.hero == null) {
	// 		return;
	// 	}
	//
	// 	super.update();
	//
	// 	water.offset( 0, -5 * Game.elapsed );
	//
	// 	Actor.process();
	//
	// 	if (Dungeon.hero.ready && !Dungeon.hero.paralysed) {
	// 		log.newLine();
	// 	}
	//
	// 	cellSelector.enabled = Dungeon.hero.ready;
	// }

	@Override
	protected void onBackPressed() {
		if(Dungeon.depth == 0 && Dungeon.level instanceof MovieLevel)
		{
			Music.INSTANCE.enable(PixelDungeon.music());
			Game.switchScene(TitleScene.class);
			Dungeon.observe();
		}
		else if (!cancel()) {
			add( new WndGame() );
		}
	}
	//
	// @Override
	// protected void onMenuPressed() {
	// 	if (Dungeon.hero.ready) {
	// 		selectItem( null, Mode.ALL, null );
	// 	}
	// }

	public static void pickUp( Item item ) {
		gauntletScene.toolbar.pickup( item );
	}

	public static void nextLevel(){
		level++;

		if(level < 3 && Dungeon.currentDifficulty != Difficulties.SUPEREASY){
			Dungeon.currentDifficulty = Difficulties.SUPEREASY;
			GLog.w("Difficulty decreased to " + Dungeon.currentDifficulty.name());
		}

		if(level >= 3 && level < 5 && Dungeon.currentDifficulty != Difficulties.EASY){
			Dungeon.currentDifficulty = Difficulties.EASY;
			GLog.w("Difficulty increased to " + Dungeon.currentDifficulty.name());
		}

		if(level >= 5 && level < 7 && Dungeon.currentDifficulty != Difficulties.NORMAL){
			Dungeon.currentDifficulty = Difficulties.NORMAL;
			GLog.w("Difficulty increased to " + Dungeon.currentDifficulty.name());
		}

		if(level >= 7 && level < 10 && Dungeon.currentDifficulty != Difficulties.HARD){
			Dungeon.currentDifficulty = Difficulties.HARD;
			GLog.w("Difficulty increased to " + Dungeon.currentDifficulty.name());
		}

		if(level >= 10 && level < 13 && Dungeon.currentDifficulty != Difficulties.HELL){
			Dungeon.currentDifficulty = Difficulties.HELL;
			GLog.w("Difficulty increased to " + Dungeon.currentDifficulty.name());
		}

		if(level >= 13 && level < 15 && Dungeon.currentDifficulty != Difficulties.SUICIDE){
			Dungeon.currentDifficulty = Difficulties.SUICIDE;
			GLog.w("Difficulty increased to " + Dungeon.currentDifficulty.name());
		}

		if(level >= 15 && Dungeon.currentDifficulty != Difficulties.JUSTKILLME){
			Dungeon.currentDifficulty = Difficulties.JUSTKILLME;
			GLog.w("Difficulty increased to " + Dungeon.currentDifficulty.name());
		}

		InterlevelScene.mode = InterlevelScene.Mode.GUANTLET_NEXT;
		Game.switchScene( InterlevelScene.class );

	}
}
