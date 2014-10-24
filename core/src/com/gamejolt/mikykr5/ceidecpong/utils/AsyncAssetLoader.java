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
package com.gamejolt.mikykr5.ceidecpong.utils;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.gamejolt.mikykr5.ceidecpong.interfaces.AssetsLoadedListener;

public final class AsyncAssetLoader implements Disposable{
	private LinkedList<AssetsLoadedListener> listeners;
	private AssetManager manager;

	private AsyncAssetLoader(){
		listeners = new LinkedList<AssetsLoadedListener>();
		manager = new AssetManager();
	}

	private static final class SingletonHolder{
		public static int REF_COUNT = 0;
		public static AsyncAssetLoader INSTANCE;
	}

	public static AsyncAssetLoader getInstance(){
		if(SingletonHolder.REF_COUNT == 0)
			SingletonHolder.INSTANCE = new AsyncAssetLoader();
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

	public void addListener(AssetsLoadedListener listener) throws IllegalArgumentException{
		try{
			checkParametes(listener, "listener");
		}catch(IllegalArgumentException e){
			throw e;
		}

		listeners.add(listener);
	}

	public <T> void addAssetToLoad(String path, Class<T> assetClass) throws IllegalArgumentException{
		try{
			checkParametes(path, "path");
			checkParametes(path, "assetClass");
		}catch(IllegalArgumentException e){
			throw e;
		}

		manager.load(path, assetClass);
	}

	public <T> T getAsset(String path, Class<T> assetClass) throws IllegalArgumentException{
		try{
			checkParametes(path, "path");
			checkParametes(path, "assetClass");
		}catch(IllegalArgumentException e){
			throw e;
		}

		return manager.get(path, assetClass);
	}

	public boolean loadAssets(){
		return manager.update();
	}

	public void notifyListeners(){
		for(AssetsLoadedListener listener : listeners)
			listener.onAssetsLoaded();

		listeners.clear();
	}

	private void checkParametes(Object parameter, String paramName) throws IllegalArgumentException{
		if(parameter == null) throw new IllegalArgumentException("Parameter: " + paramName + " is null.");
	}

	@Override
	public void dispose(){
		Gdx.app.log("ASYNC_LOADER", "Disposing asset manager.");
		manager.dispose();
	}
}
