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

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gamejolt.mikykr5.poukemon.ProjectConstants;
import com.gamejolt.mikykr5.poukemon.ecs.components.BoundingBoxComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.Mappers;
import com.gamejolt.mikykr5.poukemon.ecs.components.PlayerComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.ScoreComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.SpriteComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.VelocityComponent;
import com.gamejolt.mikykr5.poukemon.utils.AsyncAssetLoader;

public class PongEntityInitializer extends EntityInitializerBase {
	private AsyncAssetLoader loader;
	private Entity           ball;
	private Entity           paddleUser;
	private Entity           paddleComp;
	private boolean          entitiesCreated;
	private boolean          assetsLoaded;

	public PongEntityInitializer() {
		entitiesCreated = false;
		assetsLoaded = false;
	}

	@Override
	public void createAllEntities(PooledEngine engine){
		loader = AsyncAssetLoader.getInstance();

		loader.addAssetToLoad("data/gfx/textures/pong_atlas.atlas", TextureAtlas.class);

		ball = engine.createEntity();
		ball.add(engine.createComponent(PositionComponent.class));
		ball.add(engine.createComponent(VelocityComponent.class));
		ball.add(engine.createComponent(SpriteComponent.class));
		ball.add(engine.createComponent(BoundingBoxComponent.class));

		paddleUser = engine.createEntity();
		paddleUser.add(engine.createComponent(PositionComponent.class));
		paddleUser.add(engine.createComponent(SpriteComponent.class));
		paddleUser.add(engine.createComponent(BoundingBoxComponent.class));
		paddleUser.add(engine.createComponent(ScoreComponent.class));
		paddleUser.add(engine.createComponent(PlayerComponent.class));

		paddleComp = engine.createEntity();
		paddleComp.add(engine.createComponent(PositionComponent.class));
		paddleComp.add(engine.createComponent(VelocityComponent.class));
		paddleComp.add(engine.createComponent(SpriteComponent.class));
		paddleComp.add(engine.createComponent(BoundingBoxComponent.class));
		paddleComp.add(engine.createComponent(ScoreComponent.class));
		paddleComp.add(engine.createComponent(PlayerComponent.class));

		engine.addEntity(ball);
		engine.addEntity(paddleUser);
		engine.addEntity(paddleComp);

		entitiesCreated = true;
	}

	@Override
	public void setLoadableAssets(PooledEngine engine) throws IllegalStateException{
		if(!entitiesCreated)
			throw new IllegalStateException("Entities have not been created before setting assets.");

		TextureAtlas atlas = loader.getAsset("data/gfx/textures/pong_atlas.atlas", TextureAtlas.class);

		Mappers.spriteMapper.get(ball).sprite = atlas.createSprite("ball");
		Mappers.positionMapper.get(ball).setXY(-(Mappers.spriteMapper.get(ball).sprite.getWidth() / 2), -(Mappers.spriteMapper.get(ball).sprite.getHeight() / 2));
		Mappers.velocityMapper.get(ball).setXY(173, -475);
		Mappers.bboxMapper.get(ball).bbox.set(Mappers.spriteMapper.get(ball).sprite.getBoundingRectangle());

		Mappers.spriteMapper.get(paddleUser).sprite = atlas.createSprite("glasspaddle2");
		Mappers.positionMapper.get(paddleUser).setXY(-(ProjectConstants.FB_WIDTH / 2) + 100, -(Mappers.spriteMapper.get(paddleUser).sprite.getHeight() / 2));
		Mappers.bboxMapper.get(paddleUser).bbox.set(Mappers.spriteMapper.get(paddleUser).sprite.getBoundingRectangle());
		Mappers.playerMapper.get(paddleUser).id = 0;

		Mappers.spriteMapper.get(paddleComp).sprite = atlas.createSprite("paddle");
		Mappers.positionMapper.get(paddleComp).setXY(((ProjectConstants.FB_WIDTH / 2) - 1) - 100, -(Mappers.spriteMapper.get(paddleComp).sprite.getHeight() / 2));
		Mappers.bboxMapper.get(paddleComp).bbox.set(Mappers.spriteMapper.get(paddleComp).sprite.getBoundingRectangle());
		Mappers.playerMapper.get(paddleComp).id = 1;

		AsyncAssetLoader.freeInstance();
		assetsLoaded = true;
	}

	@Override
	public void dispose() throws IllegalStateException {
		if(!entitiesCreated)
			throw new IllegalStateException("Entities have not been created before disposing assets.");

		if(!assetsLoaded)
			throw new IllegalStateException("Assets have not been loaded before disposing.");
	}
}
