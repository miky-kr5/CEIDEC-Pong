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
import com.gamejolt.mikykr5.ceidecpong.ecs.components.Mappers;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.VelocityComponent;

public class PositioningSystem extends IteratingSystem{
	@SuppressWarnings("unchecked")
	public PositioningSystem(){
		super(Family.getFor(PositionComponent.class, VelocityComponent.class));
	}

	@Override
	public void processEntity(Entity entity, float deltaTime){
		PositionComponent position = Mappers.positionMapper.get(entity);
		VelocityComponent velocity = Mappers.velocityMapper.get(entity);

		position.x += velocity.vx * deltaTime;
		position.y += velocity.vy * deltaTime;
	}
}
