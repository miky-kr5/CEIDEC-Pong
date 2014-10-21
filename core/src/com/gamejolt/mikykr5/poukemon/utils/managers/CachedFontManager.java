/*
 * Copyright (c) 2014, Miguel Angel Astor Romero
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * Read the LICENSE file for more details.
 */
package com.gamejolt.mikykr5.poukemon.utils.managers;

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

	public void unloadFont(String path){
		if(fonts.containsKey(path)){
			fonts.get(path).dispose();
			fonts.remove(path);
		}
	}

	private void dispose(){
		Gdx.app.log("FONT_MANAGER", "Disposing fonts.");

		for(BitmapFont font : fonts.values())
			font.dispose();

		fonts.clear();
	}
}
