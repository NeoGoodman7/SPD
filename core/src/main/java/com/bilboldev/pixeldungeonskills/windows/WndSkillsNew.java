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
package com.bilboldev.pixeldungeonskills.windows;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Dungeon;
import com.bilboldev.pixeldungeonskills.PixelDungeon;
import com.bilboldev.pixeldungeonskills.actors.skills.BranchSkill;
import com.bilboldev.pixeldungeonskills.actors.skills.Regeneration;
import com.bilboldev.pixeldungeonskills.actors.skills.Skill;
import com.bilboldev.pixeldungeonskills.actors.skills.Smite;
import com.bilboldev.pixeldungeonskills.actors.skills.skilltree.SkillContainer;
import com.bilboldev.pixeldungeonskills.actors.skills.skilltree.SkillTree;
import com.bilboldev.pixeldungeonskills.actors.skills.skilltree.WarriorSkillTree;
import com.bilboldev.pixeldungeonskills.items.bags.Bag;
import com.bilboldev.pixeldungeonskills.items.bags.Keyring;
import com.bilboldev.pixeldungeonskills.items.bags.ScrollHolder;
import com.bilboldev.pixeldungeonskills.items.bags.SeedPouch;
import com.bilboldev.pixeldungeonskills.items.bags.WandHolster;
import com.bilboldev.pixeldungeonskills.scenes.GameScene;
import com.bilboldev.pixeldungeonskills.scenes.PixelScene;
import com.bilboldev.pixeldungeonskills.ui.Icons;
import com.bilboldev.pixeldungeonskills.ui.SkillSlot;
import com.bilboldev.pixeldungeonskills.ui.SkillSlotNew;
import com.bilboldev.pixeldungeonskills.utils.Utils;
import com.watabou.utils.RectF;

public class WndSkillsNew extends WndTabbed {


	protected static final int COLS_P	= 4;
	protected static final int COLS_L	= 6;

	protected static final int SLOT_SIZE	= 14;
	protected static final int SLOT_MARGIN	= 1;

	protected static final int TAB_WIDTH	= 25;

	protected static final int TITLE_HEIGHT	= 12;

	private Listener listener;
	private String title;

	private int nCols;
	private int nRows;

	protected int count;
	protected int col;
	protected int row;


    public boolean noDegrade = PixelDungeon.itemDeg();
	public WndSkillsNew(Listener listener, String title) {
		
		super();

		this.listener = listener;
		this.title = title;
		

		nCols = 8;
		nRows = 6;
		
		int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
		int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);
		
		BitmapText txtTitle = PixelScene.createText( title != null ? title : Utils.capitalize( "Skills" + (Skill.availableSkill > 0 ? " (" + Skill.availableSkill + " points)" :"" )), 9 );
		txtTitle.hardlight( TITLE_COLOR );
		txtTitle.measure();
		txtTitle.x = (int)(slotsWidth - txtTitle.width()) / 2;
		txtTitle.y = (int)(TITLE_HEIGHT - txtTitle.height()) / 2;
		add( txtTitle );
		
		placeSkills();

		
		resize( slotsWidth, slotsHeight + TITLE_HEIGHT );
	}
	


	protected void placeSkills(  ) {


       // placeSkill( Dungeon.hero.heroSkills.branchPA, true);
		placeSkillLine( );
		for(SkillContainer skillContainer : Dungeon.hero.skillTree.getSkills()){
			placeSkill( skillContainer, false);
		}

       // placeSkill( Dungeon.hero.heroSkills.passiveA3, false);

       //placeSkill( Dungeon.hero.heroSkills.branchPB, true);
       //placeSkill( Dungeon.hero.heroSkills.passiveB1, true);
       //placeSkill( Dungeon.hero.heroSkills.passiveB2, true);
       //placeSkill( Dungeon.hero.heroSkills.passiveB3, true);

       //placeSkill( Dungeon.hero.heroSkills.branchA, true);
       //placeSkill( Dungeon.hero.heroSkills.active1, true);
       //placeSkill( Dungeon.hero.heroSkills.active2, true);
       //placeSkill( Dungeon.hero.heroSkills.active3, true);

    }
	
	protected void placeSkill(final SkillContainer skill, boolean showBackground ) {


		int x = (skill.x != 4 ? SLOT_SIZE / 2 + SLOT_SIZE * 2 * (skill.x - 1) : SLOT_SIZE * 7);
		int y =  SLOT_SIZE + (skill.y - 1) * 3 / 2 * SLOT_SIZE + (skill.y - 1) * SLOT_SIZE / 2;

		// F this BS
		if(skill.y == 3){
			y = 4 * SLOT_SIZE;
		}

		if(skill.y == 4){
			y =SLOT_SIZE * 3 + SLOT_SIZE+ SLOT_SIZE *3/2;
		}

		add( new SkillButton( skill.skill ).setPos( x + 2, y + 2) );
		
		if (++col >= nCols) {
			col = 0;
			row++;
		}
		
		count++;
	}

	protected void placeSkillLine( ) {

		//int x = col * (SLOT_SIZE + SLOT_MARGIN);
		//int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

		int x = col * (SLOT_SIZE );
		int y = TITLE_HEIGHT + row * (SLOT_SIZE);

		add( new SkillButtonLine(  ).setPos( x, y ) );

		if (++col >= nCols) {
			col = 0;
			row++;
		}

		count++;
	}
	
	@Override
	public void onMenuPressed() {
		if (listener == null) {
			hide();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onSelect( null );
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onClick( Tab tab ) {
		hide();
		GameScene.show( new WndSkillsNew( listener, title ) );
	}
	
	@Override
	protected int tabHeight() {
		return 20;
	}
	
	private class BagTab extends Tab {
		
		private Image icon;

		private Bag bag;
		
		public BagTab( Bag bag ) {
			super();
			
			this.bag = bag;
			
			icon = icon();
			add( icon );
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			icon.am = selected ? 1.0f : 0.6f;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			icon.copy( icon() );
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
			if (!selected && icon.y < y + CUT) {
				RectF frame = icon.frame();
				frame.top += (y + CUT - icon.y) / icon.texture.height;
				icon.frame( frame );
				icon.y = y + CUT;
			}
		}
		
		private Image icon() {
			if (bag instanceof SeedPouch) {
				return Icons.get( Icons.SEED_POUCH );
			} else if (bag instanceof ScrollHolder) {
				return Icons.get( Icons.SCROLL_HOLDER );
			} else if (bag instanceof WandHolster) {
				return Icons.get( Icons.WAND_HOLSTER );
			} else if (bag instanceof Keyring) {
				return Icons.get( Icons.KEYRING );
			} else {
				return Icons.get( Icons.BACKPACK );
			}
		}
	}
	
	private static class Placeholder extends Skill {
		{
			name = null;
		}
		
		public Placeholder( int image ) {
			this.image = image;
		}
	}
	
	private class SkillButton extends SkillSlotNew {
		
		private static final int NORMAL		= 0xFF4A4D44;
		private static final int EQUIPPED	= 0xFF63665B;

		
		private Skill skill;
		private ColorBlock bg;
		
		private ColorBlock durability[];
		
		public SkillButton(Skill skill) {
			
			super( skill );

			this.skill = skill;


			width = height = SLOT_SIZE;

            durability = new ColorBlock[Skill.MAX_LEVEL];

            if(skill != null && skill.name != null && skill.level > 0  && skill.level <= Skill.MAX_LEVEL) {
                for (int i = 0; i < skill.level; i++) {
                    durability[i] = new ColorBlock(2, 2, 0xFF00EE00);
              //      add(durability[i]);
                }
                for (int i = skill.level; i < Skill.MAX_LEVEL; i++) {
                    durability[i] = new ColorBlock(2, 2, 0x4000EE00);
              //      add(durability[i]);
                }
            }

			bg.visible = false;
		}
		
		@Override
		protected void createChildren() {	
			bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
			add( bg );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;


            if(skill != null && skill.name != null && skill.level > 0  && skill.level <= Skill.MAX_LEVEL) {
                for (int i = 0; i < Skill.MAX_LEVEL; i++) {
                    durability[i].x = x + width - 9 + i * 3;
                    durability[i].y = y + 3;

                }
            }
			
			super.layout();
		}
		

		
		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};

		@Override
		protected void onPointerUp() {
			bg.brightness( 1.0f );
		};

		@Override
		protected void onClick() {
            if (listener != null) {

                hide();
                listener.onSelect(skill);

            } else {

                WndSkillsNew.this.add( new WndSkillNew( WndSkillsNew.this, skill ) );

            }
		}
		
		@Override
		protected boolean onLongClick() {
				return true;
		}
	}

	private class SkillButtonLine extends SkillSlot {

		private static final int NORMAL		= 0xFF4A4D44;
		private static final int EQUIPPED	= 0xFF63665B;


		private ColorBlock bg;

		private ColorBlock durability[];

		public SkillButtonLine() {

			super( new Regeneration(){
				@Override
				public int image() {
					return 5;
				}
			});

			//this.skill = skill;


			width = height = SLOT_SIZE;

			//durability = new ColorBlock[Skill.MAX_LEVEL];
//
			//if(skill != null && skill.name != null && skill.level > 0  && skill.level <= Skill.MAX_LEVEL) {
			//	for (int i = 0; i < skill.level; i++) {
			//		durability[i] = new ColorBlock(2, 2, 0xFF00EE00);
			//		add(durability[i]);
			//	}
			//	for (int i = skill.level; i < Skill.MAX_LEVEL; i++) {
			//		durability[i] = new ColorBlock(2, 2, 0x4000EE00);
			//		add(durability[i]);
			//	}
			//}

			bg.visible = true;
		}

		@Override
		protected void createChildren() {

			// need to put something in bg
			bg = new ColorBlock( SLOT_SIZE , SLOT_SIZE, NORMAL );
			bg.x = x + SLOT_SIZE / 2;
			bg.y = y  + SLOT_SIZE ;

			for(ColorBlock bg : Dungeon.hero.skillTree.setCoords(x, y).getBackground()){
				add(bg);
			}


			super.createChildren();
		}

		@Override
		protected void layout() {



			//if(skill != null && skill.name != null && skill.level > 0  && skill.level <= Skill.MAX_LEVEL) {
			//	for (int i = 0; i < Skill.MAX_LEVEL; i++) {
			//		durability[i].x = x + width - 9 + i * 3;
			//		durability[i].y = y + 3;
//
			//	}
			//}

			super.layout();
		}



		@Override
		protected void onPointerDown() {
			//bg.brightness( 1.5f );
			//Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};

		@Override
		protected void onPointerUp() {
			bg.brightness( 1.0f );
		};

		@Override
		protected void onClick() {

		}

		@Override
		protected boolean onLongClick() {
			return true;
		}
	}

	public interface Listener {
		void onSelect(Skill skill);
	}
}
