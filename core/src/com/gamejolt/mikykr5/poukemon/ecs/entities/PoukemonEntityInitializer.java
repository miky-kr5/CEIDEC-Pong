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

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.gamejolt.mikykr5.poukemon.ecs.components.Mappers;
import com.gamejolt.mikykr5.poukemon.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.TextureComponent;
import com.gamejolt.mikykr5.poukemon.ecs.components.VelocityComponent;
import com.gamejolt.mikykr5.poukemon.utils.AsyncAssetLoader;

public class PoukemonEntityInitializer extends EntityInitializerBase {
	private AsyncAssetLoader loader;
	private Entity           ball;
	private Entity           roseliaBody;
	private Entity           roseliaHead;
	private Entity           roseliaArms;
	private Entity           roseliaEyeLeft;
	private Entity           roseliaEyeRight;
	private boolean          entitiesCreated;
	private boolean          assetsLoaded;

	public PoukemonEntityInitializer() {
		entitiesCreated = false;
		assetsLoaded = false;
	}

	@Override
	public void createAllEntities(PooledEngine engine){
		loader = AsyncAssetLoader.getInstance();

		// TODO: Load Roselia's sprites.
		// TODO: Load ball sprites.
		// TODO: Create entities.

		loader.addAssetToLoad("data/gfx/textures/ball.png", Texture.class);

		ball = engine.createEntity();
		ball.add(engine.createComponent(PositionComponent.class));
		ball.add(engine.createComponent(VelocityComponent.class));

		engine.addEntity(ball);

		entitiesCreated = true;
	}

	@Override
	public void setLoadableAssets(PooledEngine engine) throws IllegalStateException{
		if(!entitiesCreated)
			throw new IllegalStateException("Entities have not been created before setting assets.");

		ball.add(engine.createComponent(TextureComponent.class));
		Mappers.textureMapper.get(ball).texture = loader.getAsset("data/gfx/textures/ball.png", Texture.class);
		Mappers.positionMapper.get(ball).setXY(-(Mappers.textureMapper.get(ball).texture.getWidth() / 2), -(1080 / 2) + 128);

		//TODO: Set Roselia's textures. 

		AsyncAssetLoader.freeInstance();
		assetsLoaded = true;
	}

	@Override
	public void dispose() throws IllegalStateException {
		if(!entitiesCreated)
			throw new IllegalStateException("Entities have not been created before disposing assets.");

		if(!assetsLoaded)
			throw new IllegalStateException("Assets have not been loaded before disposing.");

		ImmutableArray<Component> components = ball.getComponents();
		for(int i = 0; i < components.size(); i++){
			Component c = components.get(i);
			if(c instanceof TextureComponent){
				((TextureComponent)c).texture.dispose();
			}
		}

		// TODO: Dispose of Roselia's textures.
	}
}
