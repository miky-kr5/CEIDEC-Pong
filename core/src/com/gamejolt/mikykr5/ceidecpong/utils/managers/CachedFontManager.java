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
package com.gamejolt.mikykr5.ceidecpong.utils.managers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * A {@link BitmapFont} loader and manager with a cache.
 * 
 * @author Miguel Astor
 */
public class CachedFontManager{
	/**
	 * The characters used by all the {@link BitmapFont} objects loaded.
	 */
	public static final String FONT_CHARS            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890:,";
	
	/**
	 * The standar font size for all loaded {@link BitmapFont} objects.
	 */
	public static final int    BASE_FONT_SIZE = 40;

	/**
	 * The cache of {@link BitmapFont} objects.
	 */
	private Map<String, BitmapFont> fonts;

	/**
	 * Creates the cache. Made private so that this class cannot be instantiated outside of itself.
	 */
	private CachedFontManager(){
		fonts = new HashMap<String, BitmapFont>();
	}

	/**
	 * A holder for the singleton instance of {@link CachedFontManager}.
	 */
	private static final class SingletonHolder{
		/**
		 * How many references to the singleton instance there are.
		 */
		public static int REF_COUNT = 0;

		/**
		 * The singleton instance.
		 */
		public static CachedFontManager INSTANCE;
	}

	/**
	 * Gets a reference to the singleton instance of this class.
	 * 
	 * @return The singleton instance.
	 */
	public static CachedFontManager getInstance(){
		// If the instance does not exists then create it and update it's reference counter.
		if(SingletonHolder.REF_COUNT == 0)
			SingletonHolder.INSTANCE = new CachedFontManager();
		SingletonHolder.REF_COUNT++;

		return SingletonHolder.INSTANCE;
	}

	/**
	 * Releases a reference to the singleton instance of this class.
	 */
	public static void freeInstance(){
		SingletonHolder.REF_COUNT--;

		// If there are no more references to the instance then delete it.
		if(SingletonHolder.REF_COUNT <= 0){
			SingletonHolder.INSTANCE.dispose();
			SingletonHolder.INSTANCE = null;
		}
	}

	/**
	 * Loads a {@link BitmapFont} with the given path and {@link CachedFontManager#BASE_FONT_SIZE} as default size.
	 * 
	 * @param path The internal path of the font to load.
	 * @return The font.
	 */
	public BitmapFont loadFont(String path){
		return loadFont(path, BASE_FONT_SIZE);
	}

	/**
	 * Loads a {@link BitmapFont} with the given path and size.
	 * 
	 * @param path The internal path of the font to load.
	 * @param size The size of the font to load.
	 * @return
	 */
	public BitmapFont loadFont(String path, int size){
		// If the font is already in the cache the return it.
		// GOTCHA: The same font cannot be loaded with two different sizes.
		if(fonts.containsKey(path))
			return fonts.get(path);

		// Declare all variables used to create a font.
		FreeTypeFontGenerator fontGenerator;
		FreeTypeFontParameter fontParameters;
		BitmapFont font;

		// Set the parameters of the font to load.
		fontParameters = new FreeTypeFontParameter();
		fontParameters.characters = FONT_CHARS;
		fontParameters.size = size;
		fontParameters.flip = false;

		// Create a new bitmap font from the given TrueType font.
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		font = fontGenerator.generateFont(fontParameters);
		fonts.put(path, font);

		// Clean the font generator.
		fontGenerator.dispose();

		return font;
	}

	/**
	 * Removes a specific {@link BitmapFont} from the cache and disposes it.
	 * @param path
	 */
	public void unloadFont(String path){
		if(fonts.containsKey(path)){
			fonts.get(path).dispose();
			fonts.remove(path);
		}
	}

	/**
	 * Removes and disposes all the loaded fonts.
	 */
	private void dispose(){
		Gdx.app.log("FONT_MANAGER", "Disposing fonts.");

		//
		for(BitmapFont font : fonts.values())
			font.dispose();
		fonts.clear();
	}
}
