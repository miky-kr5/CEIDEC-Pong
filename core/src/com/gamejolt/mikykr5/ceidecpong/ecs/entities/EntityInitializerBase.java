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
package com.gamejolt.mikykr5.ceidecpong.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;
import com.gamejolt.mikykr5.ceidecpong.states.InGameState;

/**
 * Base class for entity initializers. Implementations must create all initial {@link Entity} objects
 * needed by their respective games.
 * 
 * @author Miguel Astor
 */
public abstract class EntityInitializerBase implements Disposable{
	/**
	 * Creates all {@link Entity} objects and loads the needed assets.
	 * @param engine A {@link PooledEngine} instance as used by {@link InGameState}
	 */
	public abstract void createAllEntities(PooledEngine engine);

	/**
	 * Associates all assets loaded to their respective entities. 
	 * 
	 * @param engine A {@link PooledEngine} instance as used by {@link InGameState}
	 * @throws IllegalStateException If the entities have not been created before calling this method.
	 */
	public abstract void setLoadableAssets(PooledEngine engine) throws IllegalStateException;
}
