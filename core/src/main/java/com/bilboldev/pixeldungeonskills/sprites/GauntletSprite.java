package com.bilboldev.pixeldungeonskills.sprites;

import com.watabou.noosa.TextureFilm;
import com.bilboldev.pixeldungeonskills.actors.hero.Hero;
import com.bilboldev.pixeldungeonskills.effects.ArcherMaidenHalo;
import com.bilboldev.pixeldungeonskills.effects.EmoIcon;
import com.bilboldev.pixeldungeonskills.effects.SoulFuryHalo;
import com.bilboldev.pixeldungeonskills.scenes.GameScene;

/**
 * Created by Moussa on 04-Feb-17.
 */
public class GauntletSprite extends LegendSprite {



    @Override
    public void updateArmor() {

        TextureFilm film = new TextureFilm( tiers(), ((Hero)ch).tier(), FRAME_WIDTH, FRAME_HEIGHT );

        idle = new Animation( 1, true );
        idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( RUN_FRAMERATE, true );
        run.frames( film, 2, 3, 4, 5, 6, 7 );

        die = new Animation( 20, false );
        die.frames( film, 8, 9, 10, 11, 12, 11 );

        attack = new Animation( 15, false );
        attack.frames( film, 13, 14, 15, 0 );

        zap = attack.clone();
        operate = attack.clone();


        fly = new Animation( 1, true );
        fly.frames( film, 18 );

        read = new Animation( 20, false );
        read.frames( film, 19, 20, 20, 20, 20, 20, 20, 20, 20, 19 );
    }


}
