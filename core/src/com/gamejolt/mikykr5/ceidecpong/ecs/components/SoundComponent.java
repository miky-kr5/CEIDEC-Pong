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
package com.gamejolt.mikykr5.ceidecpong.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gamejolt.mikykr5.ceidecpong.utils.managers.CachedSoundManager;

/**
 * A sound effect {@link Component}
 * 
 * @author Miguel Astor
 */
public class SoundComponent extends Component implements Poolable {
	/**
	 * The path of the sound effect. Used as a key by {@link CachedSoundManager}.
	 */
	public String path = "";

	@Override
	public void reset() {
		this.path = "";
	}
}
