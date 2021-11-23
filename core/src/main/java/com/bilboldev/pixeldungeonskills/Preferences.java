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
package com.bilboldev.pixeldungeonskills;

import com.watabou.noosa.Game;
import com.watabou.utils.GameSettings;

public enum Preferences {

	INSTANCE;

	public static final String KEY_FULLSCREEN	= "fullscreen";
	public static final String KEY_LANDSCAPE	= "landscape";
	public static final String KEY_IMMERSIVE	= "immersive";
	public static final String KEY_GOOGLE_PLAY	= "google_play";
	public static final String KEY_SCALE_UP		= "scaleup";
	public static final String KEY_MUSIC		= "music";
	public static final String KEY_SOUND_FX		= "soundfx";
	public static final String KEY_ZOOM			= "zoom";
	public static final String KEY_LAST_CLASS	= "last_class";
	public static final String KEY_CHALLENGES	= "challenges";
	public static final String KEY_DONATED		= "donated";
	public static final String KEY_INTRO		= "intro";
	public static final String KEY_BRIGHTNESS	= "brightness";
	public static final String KEY_BRIGHTNESS_NEW	= "brightness_new";
	public static final String KEY_ENABLED_3D	= "enabled3D";
    public static final String KEY_DEGRADATION	= "nodegradation";
    public static final String KEY_ARCHER_MAIDEN	= "archermaiden";
    public static final String KEY_DISABLE_CHAMPIONS	= "disablechampions";
	public static final String KEY_SECOND_QUICK_SLOT	= "secondquickslot";
	public static final String KEY_SKILL_FONT_SIZE	= "skillfontsize";
	public static final String KEY_LAST_VERSION = "last_version";

	public void set(com.badlogic.gdx.Preferences prefs) {
		GameSettings.set(prefs);
	}

	int getInt( String key, int defValue  ) {
		return GameSettings.getInt( key, defValue );
	}

	boolean contains( String key ){
		return GameSettings.contains( key );
	}

	boolean getBoolean( String key, boolean defValue  ) {
		return GameSettings.getBoolean( key, defValue );
	}

	String getString( String key, String defValue  ) {
		return GameSettings.getString( key, defValue );
	}

	void put( String key, int value ) {
		GameSettings.put( key, value );
	}

	void put( String key, boolean value ) {
		GameSettings.put( key, value );
	}

	void put( String key, String value ) {
		GameSettings.put( key, value );
	}
}
