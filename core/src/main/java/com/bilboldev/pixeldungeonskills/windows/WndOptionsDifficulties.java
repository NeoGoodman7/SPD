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

import com.watabou.noosa.BitmapTextMultiline;
import com.bilboldev.pixeldungeonskills.scenes.PixelScene;
import com.bilboldev.pixeldungeonskills.ui.RedButton;
import com.bilboldev.pixeldungeonskills.ui.Window;

public class WndOptionsDifficulties extends Window {

	private static final int WIDTH			= 120;
	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 20;

	public WndOptionsDifficulties(String title, String message, String... options ) {
		super();
		
		BitmapTextMultiline tfTitle = PixelScene.createMultiline( title, 9 );
		tfTitle.hardlight( TITLE_COLOR );
		tfTitle.x = tfTitle.y = MARGIN;
		tfTitle.maxWidth = WIDTH - MARGIN * 2;
		tfTitle.measure();
		add( tfTitle );
		
		BitmapTextMultiline tfMesage = PixelScene.createMultiline( message, 6 );
		tfMesage.maxWidth = WIDTH - MARGIN * 2;
		tfMesage.measure();
		tfMesage.x = MARGIN;
		tfMesage.y = tfTitle.y + tfTitle.height() + MARGIN;
		add( tfMesage );
		
		float pos = tfMesage.y + tfMesage.height() + MARGIN;
		
		for (int i=0; i < options.length; i++) {
			final int index = i;
			RedButton btn = new RedButton( options[i] ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};
			btn.setRect( MARGIN, pos, WIDTH - MARGIN * 2, BUTTON_HEIGHT );
			add( btn );
			
			pos += BUTTON_HEIGHT + MARGIN;
		}
		
		resize( WIDTH, (int)pos );
	}
	
	protected void onSelect( int index ) {};
}
