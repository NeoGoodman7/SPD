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

import java.util.HashMap;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.BitmaskEmitter;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Badges;
import com.bilboldev.pixeldungeonskills.Difficulties;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.GamesInProgress;
import com.bilboldev.pixeldungeonskills.PixelDungeon;
import com.bilboldev.pixeldungeonskills.VersionNewsInfo;
import com.bilboldev.pixeldungeonskills.actors.hero.HeroClass;
import com.bilboldev.pixeldungeonskills.effects.BannerSprites;
import com.bilboldev.pixeldungeonskills.effects.Speck;
import com.bilboldev.pixeldungeonskills.effects.BannerSprites.Type;
import com.bilboldev.pixeldungeonskills.ui.Archs;
import com.bilboldev.pixeldungeonskills.ui.ExitButton;
import com.bilboldev.pixeldungeonskills.ui.Icons;
import com.bilboldev.pixeldungeonskills.ui.RedButton;
import com.bilboldev.pixeldungeonskills.ui.ResumeButton;
import com.bilboldev.pixeldungeonskills.ui.Switch3DButton;
import com.bilboldev.pixeldungeonskills.utils.Utils;
import com.bilboldev.pixeldungeonskills.windows.Wnd3dSwitch;
import com.bilboldev.pixeldungeonskills.windows.WndChallenges;
import com.bilboldev.pixeldungeonskills.windows.WndClass;
import com.bilboldev.pixeldungeonskills.windows.WndMessage;
import com.bilboldev.pixeldungeonskills.windows.WndOptions;
import com.bilboldev.pixeldungeonskills.windows.WndOptionsDifficulties;
import com.watabou.utils.Callback;

public class StartScene extends PixelScene {

	private static final float BUTTON_HEIGHT	= 24;
	private static final float GAP				= 2;

	private static final String TXT_3D_ON	= "2.5D mode on!";
	private static final String TXT_3D_OFF	= "Classic mode";

	private static final String TXT_LOAD	= "Load Game";
	private static final String TXT_NEW		= "New Game";
	
	private static final String TXT_ERASE		= "Erase current game";
	private static final String TXT_DPTH_LVL	= "Depth: %d, level: %d";
	
	private static final String TXT_REALLY	= "Do you really want to start new game?";
	private static final String TXT_WARNING	= "Your current game progress will be erased.";
	private static final String TXT_YES		= "Yes, start new game";
	private static final String TXT_NO		= "No, return to main menu";
	
	private static final String TXT_UNLOCK	= "To unlock this character class, slay the 3rd boss with any other class";
	
	private static final String TXT_WIN_THE_GAME = 
		"To unlock \"Challenges\", win the game with any character class.";
	
	private static final float WIDTH_P	= 116;
	private static final float HEIGHT_P	= 220;
	
	private static final float WIDTH_L	= 224;
	private static final float HEIGHT_L	= 124;
	
	private static HashMap<HeroClass, ClassShield> shields = new HashMap<HeroClass, ClassShield>();
	
	private float buttonX;
	private float buttonY;
	
	private GameButton btnLoad;
	private GameButton btnNewGame;
	
	private boolean huntressUnlocked;
	private Group unlock;
	
	public static HeroClass curClass;
	
	@Override
	public void create() {
		
		super.create();
		
		Badges.loadGlobal();
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		float width, height;
		if (landscape()) {
			width = WIDTH_L;
			height = HEIGHT_L;
		} else {
			width = WIDTH_P;
			height = HEIGHT_P;
		}

		float left = (w - width) / 2;
		float top = (h - height) / 2; 
		float bottom = h - top;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs ); 
		
		Image title = BannerSprites.get( Type.SELECT_YOUR_HERO );
		title.x = align( (w - title.width()) / 2 );
		title.y = align( top );
		add( title );
		
		buttonX = left;
		buttonY = bottom - BUTTON_HEIGHT;


		btnNewGame = new GameButton( TXT_NEW ) {
			@Override
			protected void onClick() {
                chooseOnline();
			}
		};
		add( btnNewGame );

		btnLoad = new GameButton( TXT_LOAD ) {	
			@Override
			protected void onClick() {
                MissionScene.scenePause = false;
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				Game.switchScene( InterlevelScene.class );
			}
		};
		add( btnLoad );	
		
		float centralHeight = buttonY - title.y - title.height();
		
		HeroClass[] classes = {
			HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS	
		};
		for (HeroClass cl : classes) {
			ClassShield shield = new ClassShield( cl );
			shields.put( cl, shield );
			add( shield );
		}
		if (landscape()) {
			float shieldW = width / 4;
			float shieldH = Math.min( centralHeight, shieldW );
			top = title.y + title.height + (centralHeight - shieldH) / 2;
			for (int i=0; i < classes.length; i++) {
				ClassShield shield = shields.get( classes[i] );
				shield.setRect( left + i * shieldW, top, shieldW, shieldH );
			}
			
			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos( 
				w / 2 - challenge.width() / 2,
				top + shieldH - challenge.height() / 2 );
			add( challenge );
			
		} else {
			float shieldW = width / 2;
			float shieldH = Math.min( centralHeight / 2, shieldW * 1.2f );
			top = title.y + title.height() + centralHeight / 2 - shieldH;
			for (int i=0; i < classes.length; i++) {
				ClassShield shield = shields.get( classes[i] );
				shield.setRect( 
					left + (i % 2) * shieldW, 
					top + (i / 2) * shieldH, 
					shieldW, shieldH );
			}
			
			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos( 
				w / 2 - challenge.width() / 2,
				top + shieldH - challenge.height() / 2 );
			add( challenge );
		}

		ResumeButton btnResume = new ResumeButton(){

			@Override
			public void onClick()
			{
				Game.switchScene(MissionStartScene.class);
			}

			@Override
			public void update() {


			}


		};

		btnResume.setPos(Camera.main.width - btnResume.width(), Camera.main.height / 2 - btnResume.height() / 2);
		btnResume.visible = true;
		add( btnResume );
		
		unlock = new Group();
		add( unlock );
		
		if (!(huntressUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_3 ))) {
		
			BitmapTextMultiline text = PixelScene.createMultiline( TXT_UNLOCK, 9 );
			text.maxWidth = (int)width;
			text.measure();
			
			float pos = (bottom - BUTTON_HEIGHT) + (BUTTON_HEIGHT - text.height()) / 2;
			for (BitmapText line : text.new LineSplitter().split()) {
				line.measure();
				line.hardlight( 0xFFFF00 );
				line.x = PixelScene.align( w / 2 - line.width() / 2 );
				line.y = PixelScene.align( pos );
				unlock.add( line );
				
				pos += line.height(); 
			}
		}
        huntressUnlocked = true; // Just let it go... let it go... bla bla bla
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		curClass = null;
		updateClass( HeroClass.values()[PixelDungeon.lastClass()] );
		
		fadeIn();
		
		Badges.loadingListener = new Callback() {
			@Override
			public void call() {
				if (Game.scene() == StartScene.this) {
					PixelDungeon.switchNoFade( StartScene.class );
				}
			}
		};

		Difficulties.is3d = PixelDungeon.enabled3d();
		BitmapText line = new BitmapText(PixelScene.font1x);
		line.text(Difficulties.is3d ? TXT_3D_ON : TXT_3D_OFF);
		line.measure();
		line.hardlight( 0xFFFF00 );
		line.x = PixelScene.align( 5 );
		line.y = PixelScene.align( 4  );
		add( line );

		Switch3DButton a = new Switch3DButton();

		a.setPos(55, 2);
		a.visible = true;
		add( a );

		if(Difficulties.isShow3dWindow()){
			add(new Wnd3dSwitch(Difficulties.is3d));
		}
	}

    private void chooseOnline()
    {
    	if( curClass == HeroClass.WARRIOR){
			chooseGameMode();
		}
		else {
			chooseDifficulty();
		}

        /*
        StartScene.this.add( new WndOptions( "Game Mode", "Online still in beta...", "Offline","Online" ) {
            @Override
            protected void onSelect( int index ) {
                if(index == 0)
                {
                    if (GamesInProgress.check( curClass ) != null) {
                        StartScene.this.add( new WndOptions( TXT_REALLY, TXT_WARNING, TXT_YES, TXT_NO ) {
                            @Override
                            protected void onSelect( int index ) {
                                if (index == 0) {
                                    chooseDifficulty();
                                }
                            }
                        } );

                    } else {
                        chooseDifficulty();
                    }
                }
                else
                {
                    Dungeon.hero = null;
                    Dungeon.difficulty = 1;
                    Dungeon.currentDifficulty = Difficulties.values()[1];
                    Dungeon.currentDifficulty.reset();
                    InterlevelScene.mode = InterlevelScene.Mode.PLAY_ONLINE;
                    Game.switchScene( InterlevelScene.class );
                }
            }
        } );
        */

    }

	private void chooseGameMode()
	{


        StartScene.this.add( new WndOptions( "Game Mode", "Please select a game mode", "Enter The Dungeon","Gauntlet (Beta)" ) {
            @Override
            protected void onSelect( int index ) {
                if(index == 0)
                {
					chooseDifficulty();
                }
                else
                {
					guantletChosen();
                }
            }
        } );


	}

	private void guantletChosen(){
		StartScene.this.add( new WndOptions( "Gauntlet (Beta)", "- A basic endless mode\n" +
				"- Only supported in classic mode (no 2.5D)\n" +
				"- Only available for Warrior (for now)\n" +
				"- Does NOT delete your regular game save", "Start" ) {
			@Override
			protected void onSelect( int index ) {
				if(index == 0)
				{
					Dungeon.hero = null;
					Dungeon.difficulty = 6;
					Dungeon.currentDifficulty = Difficulties.SUPEREASY;;
					Dungeon.currentDifficulty.reset();
					InterlevelScene.mode = InterlevelScene.Mode.GUANTLET;
					Game.switchScene( InterlevelScene.class );
				}
			}
		} );
	}

    private void chooseDifficulty()
    {
        StartScene.this.add( new WndOptionsDifficulties( "Game Difficulty", "Cannot be changed in game!", Difficulties.EASY.title(), Difficulties.NORMAL.title(), Difficulties.HELL.title() , Difficulties.SUICIDE.title() , Difficulties.JUSTKILLME.title() ) {
            @Override
            protected void onSelect( int index ) {
                chooseDifficultyFinal(index);
            }
        } );


    }

    private void chooseDifficultyFinal(int index)
    {
        String title = "";
        String Description = Difficulties.description(Difficulties.getNormalizedDifficulty(index));
        final int diff = index;


        StartScene.this.add( new WndOptionsDifficulties( title, Description, "Yes", "No" ) {

            @Override
            protected void onSelect( int index ) {
                if(index == 0)
                    startNewGame(diff);
            }
        } );


    }

	@Override
	public void destroy() {
		
		Badges.saveGlobal();
		Badges.loadingListener = null;
		
		super.destroy();
	}
	
	private void updateClass( HeroClass cl ) {

        if(cl == HeroClass.HATSUNE || cl == HeroClass.ONLINE || cl == HeroClass.DATENSHI || cl == HeroClass.GAUNTLET)
            cl = HeroClass.WARRIOR;


		if (curClass == cl) {
			add( new WndClass( cl ) );
			return;
		}
		
		if (curClass != null) {
			shields.get( curClass ).highlight( false );
		}
		shields.get( curClass = cl ).highlight( true );
		
		if (cl != HeroClass.HUNTRESS || huntressUnlocked) {
		
			unlock.visible = false;
			
			GamesInProgress.Info info = GamesInProgress.check( curClass );
			if (info != null) {
				
				btnLoad.visible = true;
				btnLoad.secondary( Utils.format( TXT_DPTH_LVL, info.depth, info.level ), info.challenges );
				
				btnNewGame.visible = true;
				btnNewGame.secondary( TXT_ERASE, false );
				
				float w = (Camera.main.width - GAP) / 2 - buttonX;
				
				btnLoad.setRect(
					buttonX, buttonY, w, BUTTON_HEIGHT );
				btnNewGame.setRect(
					btnLoad.right() + GAP, buttonY, w, BUTTON_HEIGHT );
				
			} else {
				btnLoad.visible = false;
				
				btnNewGame.visible = true;
				btnNewGame.secondary( null, false );
				btnNewGame.setRect( buttonX, buttonY, Camera.main.width - buttonX * 2, BUTTON_HEIGHT );
			}
			
		} else {
			
			unlock.visible = true;
			btnLoad.visible = false;
			btnNewGame.visible = false;
			
		}
	}
	
	private void startNewGame(int diff) {

		Dungeon.hero = null;
        diff = Difficulties.getNormalizedDifficulty(diff);
        Dungeon.difficulty = diff;
        Dungeon.currentDifficulty = Difficulties.values()[diff];
        Dungeon.currentDifficulty.reset();
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		MissionScene.scenePause = false;
		if (PixelDungeon.intro()) {
			PixelDungeon.intro( false );
			Game.switchScene( IntroScene.class );
		} else {
			Game.switchScene( InterlevelScene.class );
		}	
	}
	
	@Override
	protected void onBackPressed() {
		PixelDungeon.switchNoFade( TitleScene.class );
	}
	
	private static class GameButton extends RedButton {
		
		private static final int SECONDARY_COLOR_N	= 0xCACFC2;
		private static final int SECONDARY_COLOR_H	= 0xFFFF88;
		
		private BitmapText secondary;
		
		public GameButton( String primary ) {
			super( primary );
			
			this.secondary.text( null );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			secondary = createText( 6 );
			add( secondary );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (secondary.text().length() > 0) {
				text.y = align( y + (height - text.height() - secondary.baseLine()) / 2 );
				
				secondary.x = align( x + (width - secondary.width()) / 2 );
				secondary.y = align( text.y + text.height() ); 
			} else {
				text.y = align( y + (height - text.baseLine()) / 2 );
			}
		}
		
		public void secondary( String text, boolean highlighted ) {
			secondary.text( text );
			secondary.measure();
			
			secondary.hardlight( highlighted ? SECONDARY_COLOR_H : SECONDARY_COLOR_N );
		}
	}
	
	private class ClassShield extends Button {
		
		private static final float MIN_BRIGHTNESS	= 0.6f;
		
		private static final int BASIC_NORMAL		= 0x444444;
		private static final int BASIC_HIGHLIGHTED	= 0xCACFC2;
		
		private static final int MASTERY_NORMAL		= 0x666644;
		private static final int MASTERY_HIGHLIGHTED= 0xFFFF88;
		
		private static final int WIDTH	= 24;
		private static final int HEIGHT	= 28;
		private static final int SCALE	= 2;
		
		private HeroClass cl;
		
		private Image avatar;
		private BitmapText name;
		private Emitter emitter;
		
		private float brightness;
		
		private int normal;
		private int highlighted;
		
		public ClassShield( HeroClass cl ) {
			super();
		
			this.cl = cl;
			
			avatar.frame( cl.ordinal() * WIDTH, 0, WIDTH, HEIGHT );
			avatar.scale.set( SCALE );
			
			if (Badges.isUnlocked( cl.masteryBadge() )) {
				normal = MASTERY_NORMAL;
				highlighted = MASTERY_HIGHLIGHTED;
			} else {
				normal = BASIC_NORMAL;
				highlighted = BASIC_HIGHLIGHTED;
			}
			
			name.text( cl.name() );
			name.measure();
			name.hardlight( normal );
			
			brightness = MIN_BRIGHTNESS;
			updateBrightness();
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			avatar = new Image( Assets.AVATARS );
			add( avatar );
			
			name = PixelScene.createText( 9 );
			add( name );
			
			emitter = new BitmaskEmitter( avatar );
			add( emitter );
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			avatar.x = align( x + (width - avatar.width()) / 2 );
			avatar.y = align( y + (height - avatar.height() - name.height()) / 2 );
			
			name.x = align( x + (width - name.width()) / 2 );
			name.y = avatar.y + avatar.height() + SCALE;
		}
		
		@Override
		protected void onPointerDown() {
			
			emitter.revive();
			emitter.start( Speck.factory( Speck.LIGHT ), 0.05f, 7 );
			
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 1.2f );
			updateClass( cl );
		}
		
		@Override
		public void update() {
			super.update();
			
			if (brightness < 1.0f && brightness > MIN_BRIGHTNESS) {
				if ((brightness -= Game.elapsed) <= MIN_BRIGHTNESS) {
					brightness = MIN_BRIGHTNESS;
				}
				updateBrightness();
			}
		}
		
		public void highlight( boolean value ) {
			if (value) {
				brightness = 1.0f;
				name.hardlight( highlighted );
			} else {
				brightness = 0.999f;
				name.hardlight( normal );
			}

			updateBrightness();
		}
		
		private void updateBrightness() {
			avatar.gm = avatar.bm = avatar.rm = avatar.am = brightness;
		}
	}
	
	private class ChallengeButton extends Button {
		
		private Image image;
		
		public ChallengeButton() {
			super();
			
			width = image.width;
			height = image.height;
			
			image.am = Badges.isUnlocked( Badges.Badge.VICTORY ) ? 1.0f : 0.5f;
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			image = Icons.get( PixelDungeon.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF );
			add( image );
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			image.x = align( x );
			image.y = align( y  );
		}
		
		@Override
		protected void onClick() {
			if (Badges.isUnlocked( Badges.Badge.VICTORY )) {
				StartScene.this.add( new WndChallenges( PixelDungeon.challenges(), true ) {
					public void onBackPressed() {
						super.onBackPressed();
						image.copy( Icons.get( PixelDungeon.challenges() > 0 ? 
							Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
					};
				} );
			} else {
				StartScene.this.add( new WndMessage( TXT_WIN_THE_GAME ) );
			}
		}
		
		@Override
		protected void onPointerDown() {
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}
	}
}
