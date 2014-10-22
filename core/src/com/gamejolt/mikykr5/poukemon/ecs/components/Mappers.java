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
package com.gamejolt.mikykr5.poukemon.ecs.components;

import com.badlogic.ashley.core.ComponentMapper;

public abstract class Mappers {
	public static final ComponentMapper<PositionComponent>    positionMapper    = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<VelocityComponent>    velocityMapper    = ComponentMapper.getFor(VelocityComponent.class);
	public static final ComponentMapper<TextureComponent>     textureMapper     = ComponentMapper.getFor(TextureComponent.class);
	public static final ComponentMapper<SpriteComponent>      spriteMapper      = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<BoundingBoxComponent> bboxMapper        = ComponentMapper.getFor(BoundingBoxComponent.class);
	public static final ComponentMapper<ScoreComponent>       scoreMapper       = ComponentMapper.getFor(ScoreComponent.class);
	public static final ComponentMapper<PlayerComponent>      playerMapper      = ComponentMapper.getFor(PlayerComponent.class);
	public static final ComponentMapper<SoundComponent>       soundMapper       = ComponentMapper.getFor(SoundComponent.class);
}
