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
package com.gamejolt.mikykr5.ceidecpong.interfaces;

import com.gamejolt.mikykr5.ceidecpong.utils.AsyncAssetLoader;

/**
 * An interface for objects that want to be notified when {@link AsyncAssetLoader} has finished loading all requested assets.
 * 
 * @author Miguel Astor
 */
public interface AssetsLoadedListener{
	/**
	 * Called when {@link AsyncAssetLoader} has finished loading so that observers can fetch their assets.
	 */
	public void onAssetsLoaded();
}
