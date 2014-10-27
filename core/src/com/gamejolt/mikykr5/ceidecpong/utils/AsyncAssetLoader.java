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

/**
 * An singleton wrapper around an {@link AssetManager} object.
 * 
 * @author Miguel Astor
 *
 */
public final class AsyncAssetLoader implements Disposable{
	/**
	 * A list of all listeners registered with this loader.
	 */
	private LinkedList<AssetsLoadedListener> listeners;

	/**
	 * The {@link AssetManager} used to load the assets.
	 */
	private AssetManager manager;

	/**
	 * Creates the listeners list and the assets manager. Made private so that this class cannot be
	 * instantiated outside of itself.
	 */
	private AsyncAssetLoader(){
		listeners = new LinkedList<AssetsLoadedListener>();
		manager = new AssetManager();
	}

	/**
	 * A holder for the singleton instance of {@link AsyncAssetLoader}.
	 */
	private static final class SingletonHolder{
		/**
		 * How many references to the singleton instance there are.
		 */
		public static int REF_COUNT = 0;

		/**
		 * The singleton instance.
		 */
		public static AsyncAssetLoader INSTANCE;
	}

	/**
	 * Gets a reference to the singleton instance of this class.
	 * 
	 * @return The singleton instance.
	 */
	public static AsyncAssetLoader getInstance(){
		// If the instance does not exists then create it and update it's reference counter.
		if(SingletonHolder.REF_COUNT == 0)
			SingletonHolder.INSTANCE = new AsyncAssetLoader();
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
	 * Adds a new listener to the listeners queue.
	 * 
	 * @param listener An {@link AssetsLoadedListener} instance.
	 * @throws IllegalArgumentException if listener is null.
	 */
	public void addListener(AssetsLoadedListener listener) throws IllegalArgumentException{
		try{
			checkParametes(listener, "listener");
		}catch(IllegalArgumentException e){
			throw e;
		}

		listeners.add(listener);
	}

	/**
	 * Requests a new asset to be loaded.
	 * 
	 * @param path The internal path of the asset.
	 * @param assetClass The class of the asset to load. Must be a class recognized by {@link AssetManager}.
	 * @throws IllegalArgumentException If either argument is null.
	 */
	public <T> void addAssetToLoad(String path, Class<T> assetClass) throws IllegalArgumentException{
		try{
			checkParametes(path, "path");
			checkParametes(path, "assetClass");
		}catch(IllegalArgumentException e){
			throw e;
		}

		manager.load(path, assetClass);
	}

	/**
	 * Fetches an asset from the manager after it has been loaded.
	 * 
	 * @param path The internal path of the asset.
	 * @param assetClass The class of the asset to load. Must be a class recognized by {@link AssetManager}.
	 * @return The asset.
	 * @throws IllegalArgumentException If either argument is null.
	 */
	public <T> T getAsset(String path, Class<T> assetClass) throws IllegalArgumentException{
		try{
			checkParametes(path, "path");
			checkParametes(path, "assetClass");
		}catch(IllegalArgumentException e){
			throw e;
		}

		return manager.get(path, assetClass);
	}

	/**
	 * Updates the {@link AssetManager}. Notifies all the registered listeners if the loading finished.
	 * 
	 * @return True if all assets are loaded.
	 */
	public boolean loadAssets(){
		boolean done = manager.update();

		if(done)
			notifyListeners();

		return done;
	}

	/**
	 * Notifies all listener objects that this loader has finished it's work.
	 */
	private void notifyListeners(){
		for(AssetsLoadedListener listener : listeners)
			listener.onAssetsLoaded();

		listeners.clear();
	}

	/**
	 * Checks if the given parameter is null.
	 * 
	 * @param parameter The parameter to check.
	 * @param paramName The name of the parameter to append to the exception if it is null.
	 * @throws IllegalArgumentException If parameter is null.
	 */
	private void checkParametes(Object parameter, String paramName) throws IllegalArgumentException{
		if(parameter == null) throw new IllegalArgumentException("Parameter: " + paramName + " is null.");
	}

	@Override
	public void dispose(){
		Gdx.app.log("ASYNC_LOADER", "Disposing asset manager.");
		manager.dispose();
	}
}
