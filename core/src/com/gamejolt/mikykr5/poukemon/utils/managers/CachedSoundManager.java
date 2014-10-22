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
import com.badlogic.gdx.audio.Sound;

public class CachedSoundManager {
	private Map<String, Sound> sounds;

	private CachedSoundManager(){
		sounds = new HashMap<String, Sound>();
	}

	private static final class SingletonHolder{
		public static int REF_COUNT = 0;
		public static CachedSoundManager INSTANCE;
	}

	public static CachedSoundManager getInstance(){
		if(SingletonHolder.REF_COUNT == 0)
			SingletonHolder.INSTANCE = new CachedSoundManager();
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

	public Sound loadSound(String path){
		if(sounds.containsKey(path))
			return sounds.get(path);

		Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(path, s);

		return s;
	}

	public void unloadSound(String path){
		if(sounds.containsKey(path)){
			sounds.get(path).dispose();
			sounds.remove(path);
		}
	}

	private void dispose(){
		Gdx.app.log("SOUND_MANAGER", "Disposing sounds.");

		for(Sound sound : sounds.values())
			sound.dispose();
		sounds.clear();
	}
}
