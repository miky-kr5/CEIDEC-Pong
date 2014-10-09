/*
 * Copyright (C) 2014 Miguel Angel Astor Romero
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gamejolt.mikykr5.poukemon.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class CachedFontManager{
	public static final String FONT_CHARS            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890:,";
	public static final int    BASE_FONT_SIZE = 40;

	private Map<String, BitmapFont> fonts;

	private CachedFontManager(){
		fonts = new HashMap<String, BitmapFont>();
	}

	private static final class SingletonHolder{
		public static int REF_COUNT = 0;
		public static CachedFontManager INSTANCE;
	}

	public static CachedFontManager getInstance(){
		if(SingletonHolder.REF_COUNT == 0)
			SingletonHolder.INSTANCE = new CachedFontManager();
		SingletonHolder.REF_COUNT++;
		return SingletonHolder.INSTANCE;
	}

	public static void freeInstance(){
		SingletonHolder.REF_COUNT--;
		if(SingletonHolder.REF_COUNT <= 0){
			SingletonHolder.INSTANCE.dispose();
			SingletonHolder.INSTANCE = null;
		}
	}

	public BitmapFont loadFont(String path){
		return loadFont(path, BASE_FONT_SIZE);
	}

	public BitmapFont loadFont(String path, int size){
		FreeTypeFontGenerator fontGenerator;
		FreeTypeFontParameter fontParameters;
		BitmapFont font;

		if(fonts.containsKey(path))
			return fonts.get(path);

		fontParameters = new FreeTypeFontParameter();
		fontParameters.characters = FONT_CHARS;
		fontParameters.size = size;
		fontParameters.flip = false;

		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		font = fontGenerator.generateFont(fontParameters);

		fonts.put(path, font);

		fontGenerator.dispose();

		return font;
	}

	private void dispose(){
		for(BitmapFont font : fonts.values())
			font.dispose();

		fonts.clear();
	}
}
