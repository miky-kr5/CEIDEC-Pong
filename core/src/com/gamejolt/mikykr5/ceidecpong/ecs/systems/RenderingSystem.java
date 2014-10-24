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
package com.gamejolt.mikykr5.ceidecpong.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.Mappers;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.SpriteComponent;

public class RenderingSystem extends IteratingSystem{
	private final SpriteBatch batch;

	@SuppressWarnings("unchecked")
	public RenderingSystem(SpriteBatch batch){
		super(Family.getFor(PositionComponent.class, SpriteComponent.class));

		this.batch = batch;
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) throws IllegalStateException{
		PositionComponent position = Mappers.positionMapper.get(entity);
		SpriteComponent   sprite   = Mappers.spriteMapper.get(entity);

		if(!batch.isDrawing())
			throw new IllegalStateException("Sprite batch did not call begin before processing entites.");

		if(sprite.sprite != null){
			batch.draw(sprite.sprite, position.x, position.y);
		}
	}
}
