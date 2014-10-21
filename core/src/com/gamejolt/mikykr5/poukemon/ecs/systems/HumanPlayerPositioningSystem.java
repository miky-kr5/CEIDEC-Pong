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

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.gamejolt.mikykr5.poukemon.ecs.components.Mappers;
import com.gamejolt.mikykr5.poukemon.ecs.components.PlayerComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.PositionComponent;

public class HumanPlayerPositioningSystem extends IteratingSystem {
	@SuppressWarnings("unchecked")
	public HumanPlayerPositioningSystem() {
		super(Family.getFor(PlayerComponent.class, PositionComponent.class));
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		InterSystemMessage message;
		PositionComponent position = Mappers.positionMapper.get(entity);
		PlayerComponent   player   = Mappers.playerMapper.get(entity);

		if(player.id == 0){
			while((message = InterSystemMessagingQueue.popMessage(HumanPlayerPositioningSystem.class.getCanonicalName())) != null){
				float playerY;

				if(message.data.containsKey("INPUT_Y")){
					playerY = (Float) message.data.get("INPUT_Y");
					position.y = playerY;
				}
			}
		}
	}
}
