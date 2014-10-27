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
import com.badlogic.gdx.audio.Sound;

/**
 * A {@link Sound} effect loader and manager with a cache.
 * 
 * @author Miguel Astor
 */
public class CachedSoundManager {
	/**
	 * The cache of {@link Sound} objects.
	 */
	private Map<String, Sound> sounds;

	/**
	 * Creates the cache. Made private so that this class cannot be instantiated outside of itself.
	 */
	private CachedSoundManager(){
		sounds = new HashMap<String, Sound>();
	}

	/**
	 * A holder for the singleton instance of {@link CachedSoundManager}.
	 */
	private static final class SingletonHolder{
		/**
		 * How many references to the singleton instance there are.
		 */
		public static int REF_COUNT = 0;

		/**
		 * The singleton instance.
		 */
		public static CachedSoundManager INSTANCE;
	}

	/**
	 * Gets a reference to the singleton instance of this class.
	 * 
	 * @return The singleton instance.
	 */
	public static CachedSoundManager getInstance(){
		// If the instance does not exists then create it and update it's reference counter.
		if(SingletonHolder.REF_COUNT == 0)
			SingletonHolder.INSTANCE = new CachedSoundManager();
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
	 * Loads a {@link Sound} effect with the given path.
	 * 
	 * @param path The internal path of the sound effect to load.
	 * @return The sound effect.
	 */
	public Sound loadSound(String path){
		if(sounds.containsKey(path))
			return sounds.get(path);

		Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(path, s);

		return s;
	}

	/**
	 * Removes a specific {@link Sound} from the cache and disposes it.
	 * @param path
	 */
	public void unloadSound(String path){
		if(sounds.containsKey(path)){
			sounds.get(path).dispose();
			sounds.remove(path);
		}
	}

	/**
	 * Removes and disposes all the loaded sounds.
	 */
	private void dispose(){
		Gdx.app.log("SOUND_MANAGER", "Disposing sounds.");

		for(Sound sound : sounds.values())
			sound.dispose();
		sounds.clear();
	}
}
