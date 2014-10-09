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

import java.util.LinkedList;

import com.badlogic.gdx.assets.AssetManager;
import com.gamejolt.mikykr5.poukemon.interfaces.AssetsLoadedListener;

public final class AsyncAssetLoader {
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
		if(SingletonHolder.REF_COUNT <= 0) SingletonHolder.INSTANCE = null;
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
}
