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
package com.gamejolt.mikykr5.poukemon.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gamejolt.mikykr5.poukemon.ProjectConstants;
import com.gamejolt.mikykr5.poukemon.ecs.components.BoundingBoxComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.Mappers;
import com.gamejolt.mikykr5.poukemon.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.SpriteComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.VelocityComponent;

public class CollisionDetectionSystem extends IteratingSystem {
	private ImmutableArray<Entity> collidables;
	private final float            screenLeftBorder;
	private final float            screenRightBorder;
	private final float            screenTopBorder;
	private final float            screenBottomBorder;

	@SuppressWarnings("unchecked")
	public CollisionDetectionSystem(Engine engine){
		super(Family.getFor(PositionComponent.class, BoundingBoxComponent.class, VelocityComponent.class));

		collidables = engine.getEntitiesFor(Family.getFor(BoundingBoxComponent.class));
		screenLeftBorder = -((float)ProjectConstants.FB_WIDTH / 2.0f);
		screenRightBorder = ((float)ProjectConstants.FB_WIDTH / 2.0f) - 1.0f;
		screenTopBorder = ((float)ProjectConstants.FB_HEIGHT / 2.0f) - 1.0f;
		screenBottomBorder = -((float)ProjectConstants.FB_HEIGHT / 2.0f);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime){
		InterSystemMessage message;
		PositionComponent    position = Mappers.positionMapper.get(entity);
		BoundingBoxComponent bounds   = Mappers.bboxMapper.get(entity);
		VelocityComponent    velocity = Mappers.velocityMapper.get(entity);

		// Check if this entity is within the screen.
		// If the entity collides with any of the borders then bounce or score as needed.
		if(position.x < screenLeftBorder){
			position.x = screenLeftBorder;
			velocity.vx = velocity.vx < 0.0f ? -velocity.vx : velocity.vx;
			resetEntity(entity);

			message = new InterSystemMessage(ScoringSystem.class.getCanonicalName());
			message.data.put("SCORE", 1);
			InterSystemMessagingQueue.pushMessage(message);
		}

		if(position.x + bounds.bbox.getWidth() >= screenRightBorder){
			position.x = screenRightBorder - bounds.bbox.getWidth();
			velocity.vx = velocity.vx > 0.0f ? -velocity.vx : velocity.vx;
			resetEntity(entity);

			message = new InterSystemMessage(ScoringSystem.class.getCanonicalName());
			message.data.put("SCORE", 0);
			InterSystemMessagingQueue.pushMessage(message);
		}

		if(position.y < screenBottomBorder){
			position.y = screenBottomBorder;
			velocity.vy = velocity.vy < 0.0f ? -velocity.vy : velocity.vy;
			accelerate(velocity);
		}

		if(position.y + bounds.bbox.getHeight() >= screenTopBorder){
			position.y = screenTopBorder - bounds.bbox.getHeight();
			velocity.vy = velocity.vy > 0.0f ? -velocity.vy : velocity.vy;
			accelerate(velocity);
		}

		for(int i = 0; i < collidables.size(); i++){
			if(collidables.get(i).getIndex() == entity.getIndex()){
				continue;
			}else{
			}
		}

		message = new InterSystemMessage(ComputerPlayerPositioningSystem.class.getCanonicalName());
		message.data.put("BALL_Y", position.y);
		InterSystemMessagingQueue.pushMessage(message);
	}

	private void accelerate(VelocityComponent velocity){
		velocity.vx *= 1.03f;
		velocity.vy *= 1.03f;
	}

	private void resetEntity(Entity entity){
		PositionComponent position = Mappers.positionMapper.get(entity);
		SpriteComponent   sprite   = Mappers.spriteMapper.get(entity);
		VelocityComponent velocity = Mappers.velocityMapper.get(entity);

		velocity.setXY(173, -475);

		if(position != null){
			if(sprite != null){
				position.setXY(-(sprite.sprite.getWidth() / 2), -(sprite.sprite.getHeight() / 2));
			}else{
				position.setXY(0, 0);
			}
		}
	}
}
