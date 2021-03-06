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

import com.bilboldev.pixeldungeonskills.sprites.CharSprite;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;

public class ChampRedHalo extends Halo {

    private CharSprite target;

    private float phase = 0;

    static final int RED = 0xb70202;
    static final int YELLOW = 0xe4ff00;
    static final int WHITE = 0xffffff;
    static final int BLACK = 0x000000;


    public ChampRedHalo(CharSprite sprite) {
        super( 20, RED, 0.25f );
        target = sprite;
        am = 0;
    }


    @Override
    public void update() {
        super.update();

        if (phase < 0) {
            if ((phase += Game.elapsed) >= 0) {
                killAndErase();
            } else {
                scale.set( (2 + phase) * radius / RADIUS );
                am = -phase * brightness;
            }
        } else if (phase < 1) {
            if ((phase += Game.elapsed) >= 1) {
                phase = 1;
            }
            scale.set( phase * radius / RADIUS );
            am = phase * brightness;
        }

        point( target.x + target.width / 2, target.y + target.height / 2 );
    }

    @Override
    public void draw() {
        Blending.setLightMode();
        super.draw();
        Blending.setNormalMode();
    }

    public void putOut() {
        phase = -1;
    }
}
