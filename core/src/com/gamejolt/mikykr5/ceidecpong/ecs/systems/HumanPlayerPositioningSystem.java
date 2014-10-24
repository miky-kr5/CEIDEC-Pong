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
import com.gamejolt.mikykr5.ceidecpong.ProjectConstants;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.BoundingBoxComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.Mappers;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.PlayerComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging.InterSystemMessage;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging.InterSystemMessagingQueue;

public class HumanPlayerPositioningSystem extends IteratingSystem {
	private final float screenTopBorder;
	private final float screenBottomBorder;

	@SuppressWarnings("unchecked")
	public HumanPlayerPositioningSystem() {
		super(Family.getFor(PlayerComponent.class, PositionComponent.class, BoundingBoxComponent.class));

		screenTopBorder = ((float)ProjectConstants.FB_HEIGHT / 2.0f) - 1.0f;
		screenBottomBorder = -((float)ProjectConstants.FB_HEIGHT / 2.0f);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		InterSystemMessage   message;
		PositionComponent    position = Mappers.positionMapper.get(entity);
		PlayerComponent      player   = Mappers.playerMapper.get(entity);
		BoundingBoxComponent bounds   = Mappers.bboxMapper.get(entity);

		if(player.id == PlayerComponent.HUMAN_PLAYER){
			while((message = InterSystemMessagingQueue.popMessage(HumanPlayerPositioningSystem.class.getCanonicalName())) != null){
				float playerY;

				if(message.data.containsKey("INPUT_Y")){
					playerY = (Float) message.data.get("INPUT_Y");
					position.y = playerY;

					if(position.y < screenBottomBorder)
						position.y = screenBottomBorder;

					if(position.y + bounds.bbox.getHeight() >= screenTopBorder)
						position.y = screenTopBorder - bounds.bbox.getHeight();
				}
			}
		}
	}
}
