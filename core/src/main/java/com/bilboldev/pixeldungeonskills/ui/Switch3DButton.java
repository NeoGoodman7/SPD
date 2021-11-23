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
package com.bilboldev.pixeldungeonskills.ui;

import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.bilboldev.pixeldungeonskills.Assets;
import com.bilboldev.pixeldungeonskills.Difficulties;
import com.bilboldev.pixeldungeonskills.PixelDungeon;
import com.bilboldev.pixeldungeonskills.VersionNewsInfo;
import com.bilboldev.pixeldungeonskills.scenes.MissionStartScene;
import com.bilboldev.pixeldungeonskills.scenes.StartScene;
import com.bilboldev.pixeldungeonskills.scenes.TitleScene;

public class Switch3DButton extends Button {

	private Image image;

	public Switch3DButton() {
		super();
		
		width = image.width;
		height = image.height;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		image = Icons.SWITCH_3D.get();
		add( image );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		image.x = x;
		image.y = y;
	}
	
	@Override
	protected void onPointerDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}
	
	@Override
	protected void onPointerUp() {
		image.resetColor();
	}
	
	@Override
	protected void onClick() {
		Difficulties.swap3D();

		if (Game.scene() instanceof StartScene) {
			PixelDungeon.switchNoFade( StartScene.class );
		} else {
			PixelDungeon.switchNoFade(MissionStartScene.class );
		}
	}
}
