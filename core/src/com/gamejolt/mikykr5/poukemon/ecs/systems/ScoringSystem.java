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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamejolt.mikykr5.poukemon.ProjectConstants;
import com.gamejolt.mikykr5.poukemon.ecs.components.Mappers;
import com.gamejolt.mikykr5.poukemon.ecs.components.PlayerComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.ScoreComponent;
import com.gamejolt.mikykr5.poukemon.utils.managers.CachedFontManager;

public class ScoringSystem extends IteratingSystem {
	private final SpriteBatch batch;
	private BitmapFont        font;

	@SuppressWarnings("unchecked")
	public ScoringSystem(final SpriteBatch batch){
		super(Family.getFor(ScoreComponent.class, PlayerComponent.class));
		this.batch = batch;
		this.font = CachedFontManager.getInstance().loadFont("data/fonts/CRYSTAL-Regular.ttf", 180);
		CachedFontManager.freeInstance();
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TextBounds bounds;
		float x, y;

		InterSystemMessage message;
		ScoreComponent score = Mappers.scoreMapper.get(entity);
		PlayerComponent player = Mappers.playerMapper.get(entity);

		while((message = InterSystemMessagingQueue.popMessage(ScoringSystem.class.getCanonicalName())) != null){
			int playerId;

			if(message.data.containsKey("SCORE")){
				playerId = (Integer) message.data.get("SCORE");

				if(playerId == player.id){
					score.score++;
				}else{
					InterSystemMessagingQueue.pushMessage(message);
					break;
				}
			}
		}

		bounds = font.getBounds(String.format("%02d", score.score));

		y = (ProjectConstants.FB_HEIGHT / 2.0f) - (bounds.height / 2.0f) - 20;
		if(player.id == 0){
			x = -(ProjectConstants.FB_WIDTH / 4.0f) - (bounds.width / 2.0f);
		}else if(player.id == 1){
			x = (ProjectConstants.FB_WIDTH / 4.0f) - (bounds.width / 2.0f);;
		}else
			return;

		font.setColor(Color.WHITE);
		font.draw(batch, String.format("%02d", score.score), x, y);
	}
}
