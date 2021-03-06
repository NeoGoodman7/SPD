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
import com.bilboldev.pixeldungeonskills.actors.hero.HeroClass;
import com.bilboldev.pixeldungeonskills.effects.BannerSprites;
import com.bilboldev.pixeldungeonskills.effects.BannerSprites.Type;
import com.bilboldev.pixeldungeonskills.effects.Speck;
import com.bilboldev.pixeldungeonskills.ui.Archs;
import com.bilboldev.pixeldungeonskills.ui.ExitButton;
import com.bilboldev.pixeldungeonskills.ui.Icons;
import com.bilboldev.pixeldungeonskills.ui.RedButton;
import com.bilboldev.pixeldungeonskills.ui.ResumeButton;
import com.bilboldev.pixeldungeonskills.utils.Utils;
import com.bilboldev.pixeldungeonskills.windows.Wnd3dSwitch;
import com.bilboldev.pixeldungeonskills.windows.WndChallenges;
import com.bilboldev.pixeldungeonskills.windows.WndClass;
import com.bilboldev.pixeldungeonskills.windows.WndMessage;
import com.bilboldev.pixeldungeonskills.windows.WndOptions;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class MissionStartScene extends PixelScene {

	private static final float BUTTON_HEIGHT	= 24;
	private static final float GAP				= 2;
	
	private static final String TXT_LOAD	= "Load Game";
	private static final String TXT_NEW		= "Choose Scenario";
	
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

        ResumeButton btnResume = new ResumeButton(){

            @Override
            public void onClick()
            {
                Game.switchScene(StartScene.class);
            }

            @Override
            public void update() {


            }


        };

        btnResume.setPos(Camera.main.width - btnResume.width(), Camera.main.height / 2 - btnResume.height() / 2);
        btnResume.visible = true;
        add( btnResume );

		btnNewGame = new GameButton( TXT_NEW ) {
			@Override
			protected void onClick() {

                    chooseMission();

			}
		};
		add( btnNewGame );


		
		float centralHeight = buttonY - title.y - title.height();
		
		HeroClass[] classes = {
				HeroClass.HATSUNE, HeroClass.DATENSHI
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
				if (Game.scene() == MissionStartScene.this) {
					PixelDungeon.switchNoFade( MissionStartScene.class );
				}
			}
		};

		if(Difficulties.is3d){
			Difficulties.swap3D();
			Difficulties.isShow3dWindow();
			add(Wnd3dSwitch.campaign3DWarning());
		}
	}


    private void chooseMission()
    {
    	if(curClass == HeroClass.HATSUNE){
			MissionStartScene.this.add( new WndOptions( "The Defence Of Boonamai", "Select Mission", "First Wave" , "Betrayal", "Soul Fury" ) {
				@Override
				protected void onSelect( int index ) {
					chooseMissionFinal(index);
				}
			} );
		}
		else {
			MissionStartScene.this.add( new WndOptions( "Mother Dearest", "Select Mission", "The Escape" ) {
				@Override
				protected void onSelect( int index ) {
					chooseMissionFinalD(index);
				}
			} );
		}
    }

    private void chooseMissionFinal(int index)
    {
        if(index == 0) {
            String title = "";
            String Description = "Dark forces have caught the defences off guard.\n Morale is low and so are the resources at hand. \nHold the line...";
            final int diff = index;


            MissionStartScene.this.add(new WndOptions(title, Description, "Start", "Back") {

                @Override
                protected void onSelect(int index) {
                    if (index == 0)
                        startNewGame(0);
                }
            });

        }
        else  if(index == 1) {
			{
				MissionStartScene.this.add(new WndOptions("Betrayal", "The militia are losing ground. No choice but to rely on mercs...", "Start", "Back") {

					@Override
					protected void onSelect(int index) {
						if (index == 0)
							startNewGame(1);
					}
				});
			}
		}
		else
			{
				MissionStartScene.this.add(new WndOptions("Soul Fury", "Hatsune stands her ground as Boonamai gets evacuated.", "Start", "Back") {

					@Override
					protected void onSelect(int index) {
						if (index == 0)
							startNewGame(2);
					}
				});

		}

    }

	private void chooseMissionFinalD(int index)
	{
		MissionStartScene.this.add(new WndOptions("The Escape", "They do not understand, they never did.\nAll they do is whine about their pain.\nI have to save her.\n(In the works)"));
	}

	@Override
	public void destroy() {
		
		Badges.saveGlobal();
		Badges.loadingListener = null;
		
		super.destroy();
	}
	
	private void updateClass( HeroClass cl ) {

        if(cl != HeroClass.HATSUNE && cl != HeroClass.DATENSHI)
            cl = HeroClass.HATSUNE;


		if (curClass == cl) {
			add( new WndClass( cl ) );
			return;
		}
		
		if (curClass != null) {
			shields.get( curClass ).highlight( false );
		}
		shields.get( curClass = cl ).highlight( true );
		
		if (true) {
		
			unlock.visible = false;
			

				
				btnNewGame.visible = true;
				btnNewGame.secondary( null, false );
				btnNewGame.setRect( buttonX, buttonY, Camera.main.width - buttonX * 2, BUTTON_HEIGHT );

			
		} else {
			
			unlock.visible = true;

			btnNewGame.visible = false;
			
		}
	}
	
	private void startNewGame(int diff) {

		Dungeon.hero = null;
       // diff = Difficulties.getNormalizedDifficulty(diff);
        Dungeon.difficulty = 0;
        Dungeon.currentDifficulty = Difficulties.values()[0];
        Dungeon.currentDifficulty.reset();
		InterlevelScene.mode = InterlevelScene.Mode.MISSION;
		InterlevelScene.missionChosen = diff;
        MissionScene.scenePause = true;
		//if (PixelDungeon.intro()) {
		//	PixelDungeon.intro( false );
		//	Game.switchScene( IntroScene.class );
		//} else {
			Game.switchScene( InterlevelScene.class );
	//	}
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
			
			avatar.frame( (cl.ordinal() - 4) * WIDTH, 0, WIDTH, HEIGHT );
			avatar.scale.set( SCALE );
			

				normal = BASIC_NORMAL;
				highlighted = BASIC_HIGHLIGHTED;

			
			name.text( cl.name() );
			name.measure();
			name.hardlight( normal );
			
			brightness = MIN_BRIGHTNESS;
			updateBrightness();
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			avatar = new Image( Assets.AVATARS_MISSION );
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
				MissionStartScene.this.add( new WndChallenges( PixelDungeon.challenges(), true ) {
					public void onBackPressed() {
						super.onBackPressed();
						image.copy( Icons.get( PixelDungeon.challenges() > 0 ? 
							Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
					};
				} );
			} else {
				MissionStartScene.this.add( new WndMessage( TXT_WIN_THE_GAME ) );
			}
		}
		
		@Override
		protected void onPointerDown() {
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}
	}
}
