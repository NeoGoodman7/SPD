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
package com.bilboldev.pixeldungeonskills.effects;

import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.bilboldev.pixeldungeonskills.scenes.GameScene;
import com.bilboldev.pixeldungeonskills.sprites.CharSprite;
import com.bilboldev.pixeldungeonskills.ui.Icons;
import com.watabou.utils.Random;

public class EmoIcon extends Image {

	protected float maxSize = 2;
	protected float timeScale = 1;
	
	protected boolean growing	= true;
	
	protected CharSprite owner;
	
	public EmoIcon( CharSprite owner ) {
		super();
		
		this.owner = owner;
		GameScene.add( this );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (visible) {
            if(!(this instanceof RatFlag)) {
                if (growing) {
                    scale.set(scale.x + Game.elapsed * timeScale);
                    if (scale.x > maxSize) {
                        growing = false;
                    }
                } else {
                    scale.set(scale.x - Game.elapsed * timeScale);
                    if (scale.x < 1) {
                        growing = true;
                    }
                }
            }
            else
            {
                scale.set(0.8f);
            }

            if(!(this instanceof OnlineTurn)) {
                x = owner.x + owner.width - width / 2;
                y = owner.y - height;
            }
            else
            {
                x = owner.x + width / 2 - 1;
                y = owner.y - height - 2;
            }
		}
	}
	
	public static class Sleep extends EmoIcon {
		
		public Sleep( CharSprite owner ) {
			
			super( owner );
			
			copy( Icons.get( Icons.SLEEP ) );
			
			maxSize = 1.2f;
			timeScale = 0.5f;
			
			origin.set( width / 2, height / 2 );
			scale.set( Random.Float( 1, maxSize ) );
		}
	}
	
	public static class Alert extends EmoIcon {
		
		public Alert( CharSprite owner ) {
			
			super( owner );
			
			copy( Icons.get( Icons.ALERT ) );
			
			maxSize = 1.3f;
			timeScale = 2;
			
			origin.set( 2.5f, height - 2.5f );
			scale.set( Random.Float( 1, maxSize ) );
		}
	}

    public static class RatFlag extends EmoIcon {

        public RatFlag( CharSprite owner ) {

            super( owner );

            copy( Icons.get( Icons.RAT_KING_FLAG ) );

            maxSize = 0.5f;
            timeScale = 1f;

            origin.set( width / 2, height / 2 );
            scale.set( 0.5f );
        }
    }

    public static class OnlineTurn extends EmoIcon {

        public OnlineTurn( CharSprite owner ) {

            super( owner );

            copy( Icons.get( Icons.ARROW_DOWN ) );

            maxSize = 1.3f;
            timeScale = 1f;

            origin.set( width / 2, height / 2 );
            scale.set( Random.Float( 1, maxSize ) );
        }
    }

}
