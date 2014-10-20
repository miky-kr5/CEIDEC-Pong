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
package com.gamejolt.mikykr5.poukemon.ecs.entities;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Disposable;

public abstract class EntityInitializerBase implements Disposable{
	public abstract void createAllEntities(PooledEngine engine);
	public abstract void setLoadableAssets(PooledEngine engine) throws IllegalStateException;
}
