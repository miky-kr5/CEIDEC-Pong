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
package com.gamejolt.mikykr5.ceidecpong.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gamejolt.mikykr5.ceidecpong.ProjectConstants;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.BoundingBoxComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.Mappers;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.PlayerComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.PositionComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.ScoreComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.SoundComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.SpriteComponent;
import com.gamejolt.mikykr5.ceidecpong.ecs.components.VelocityComponent;
import com.gamejolt.mikykr5.ceidecpong.utils.AsyncAssetLoader;
import com.gamejolt.mikykr5.ceidecpong.utils.managers.CachedSoundManager;

/**
 * A concrete implementation of a {@link EntityInitializerBase} that creates all the entities
 * needed by the Pong game.
 * 
 * @author Miguel Astor
 */
public class PongEntityInitializer extends EntityInitializerBase{
	/**
	 * An assets loader instance.
	 */
	private AsyncAssetLoader loader;
	
	/**
	 * An entity that plays a sound when the user scores.
	 */
	private Entity           victorySound;
	
	/**
	 * An entity that plays a sound when the computer scores.
	 */
	private Entity           defeatSound;
	
	/**
	 * An entity that represents the ball in the game.
	 */
	private Entity           ball;
	
	/**
	 * An entity that represents the human player.
	 */
	private Entity           paddleUser;
	
	/**
	 * An entity that represents the computer player.
	 */
	private Entity           paddleComp;
	
	/**
	 * An entity that is used to render the background.
	 */
	private Entity           background;
	
	/**
	 * Flag that indicates that all entities have been created.
	 */
	private boolean          entitiesCreated;
	
	/**
	 * Flag that indicates that all assets associated to entities have been loaded.
	 */
	private boolean          assetsLoaded;

	/**
	 * Create the initializer and set the flags to false.
	 */
	public PongEntityInitializer() {
		entitiesCreated = false;
		assetsLoaded = false;
	}

	@Override
	public void createAllEntities(PooledEngine engine){
		// Get instances of the needed asset loaders.
		loader = AsyncAssetLoader.getInstance();
		CachedSoundManager soundManager = CachedSoundManager.getInstance();

		// Load all textures and sound effects.
		loader.addAssetToLoad("data/gfx/textures/pong_atlas.atlas", TextureAtlas.class);
		loader.addAssetToLoad("data/gfx/textures/bckg.png", Texture.class);
		soundManager.loadSound("data/sfx/BounceYoFrankie.ogg");
		soundManager.loadSound("data/sfx/oh_yeah_wav_cut.ogg");
		soundManager.loadSound("data/sfx/atari_boom.ogg");

		// Create the entities related to the sound effects.
		victorySound = engine.createEntity();
		victorySound.add(engine.createComponent(SoundComponent.class));
		defeatSound = engine.createEntity();
		defeatSound.add(engine.createComponent(SoundComponent.class));

		// Create the background.
		background = engine.createEntity();
		background.add(engine.createComponent(PositionComponent.class));
		background.add(engine.createComponent(SpriteComponent.class));

		// Create the ball.
		ball = engine.createEntity();
		ball.add(engine.createComponent(PositionComponent.class));
		ball.add(engine.createComponent(VelocityComponent.class));
		ball.add(engine.createComponent(SpriteComponent.class));
		ball.add(engine.createComponent(BoundingBoxComponent.class));
		ball.add(engine.createComponent(SoundComponent.class));

		// Create the human player.
		paddleUser = engine.createEntity();
		paddleUser.add(engine.createComponent(PositionComponent.class));
		paddleUser.add(engine.createComponent(VelocityComponent.class));
		paddleUser.add(engine.createComponent(SpriteComponent.class));
		paddleUser.add(engine.createComponent(BoundingBoxComponent.class));
		paddleUser.add(engine.createComponent(ScoreComponent.class));
		paddleUser.add(engine.createComponent(PlayerComponent.class));

		// Create the computer player.
		paddleComp = engine.createEntity();
		paddleComp.add(engine.createComponent(PositionComponent.class));
		paddleComp.add(engine.createComponent(VelocityComponent.class));
		paddleComp.add(engine.createComponent(SpriteComponent.class));
		paddleComp.add(engine.createComponent(BoundingBoxComponent.class));
		paddleComp.add(engine.createComponent(ScoreComponent.class));
		paddleComp.add(engine.createComponent(PlayerComponent.class));

		// Register all entities.
		engine.addEntity(victorySound);
		engine.addEntity(defeatSound);
		engine.addEntity(background);
		engine.addEntity(ball);
		engine.addEntity(paddleUser);
		engine.addEntity(paddleComp);

		// Mark the flag.
		entitiesCreated = true;
	}

	@Override
	public void setLoadableAssets(PooledEngine engine) throws IllegalStateException{
		if(!entitiesCreated)
			throw new IllegalStateException("Entities have not been created before setting assets.");

		// Some variables used to initialize the ball.
		Vector2      randomVector = new Vector2().set(Vector2.X).setAngle(MathUtils.random(-60, 60));
		int          randomSign   = MathUtils.random(-1, 1) >= 0 ? 1 : -1;
		
		// Fetch the assets.
		TextureAtlas atlas        = loader.getAsset("data/gfx/textures/pong_atlas.atlas", TextureAtlas.class);
		Texture      bckg         = loader.getAsset("data/gfx/textures/bckg.png", Texture.class);

		// Add the sound effects to the entities.
		Mappers.soundMapper.get(victorySound).path = "data/sfx/oh_yeah_wav_cut.ogg";
		Mappers.soundMapper.get(defeatSound).path = "data/sfx/atari_boom.ogg";

		// Set up the background.
		Mappers.spriteMapper.get(background).sprite = new Sprite(bckg);
		Mappers.positionMapper.get(background).setXY(-(ProjectConstants.FB_WIDTH / 2.0f), -(ProjectConstants.FB_HEIGHT / 2.0f));

		// Set up the ball.
		Mappers.spriteMapper.get(ball).sprite = atlas.createSprite("ball");
		Mappers.positionMapper.get(ball).setXY(-(Mappers.spriteMapper.get(ball).sprite.getWidth() / 2), -(Mappers.spriteMapper.get(ball).sprite.getHeight() / 2));
		Mappers.velocityMapper.get(ball).setXY(randomVector.x * 475.0f * randomSign, randomVector.y * 475.0f * randomSign);
		Mappers.bboxMapper.get(ball).bbox.set(Mappers.spriteMapper.get(ball).sprite.getBoundingRectangle());
		Mappers.soundMapper.get(ball).path = "data/sfx/BounceYoFrankie.ogg";

		// Set up the human player.
		Mappers.spriteMapper.get(paddleUser).sprite = atlas.createSprite("glasspaddle2");
		Mappers.positionMapper.get(paddleUser).setXY(-(ProjectConstants.FB_WIDTH / 2) + 100, -(Mappers.spriteMapper.get(paddleUser).sprite.getHeight() / 2));
		Mappers.bboxMapper.get(paddleUser).bbox.set(Mappers.spriteMapper.get(paddleUser).sprite.getBoundingRectangle());
		Mappers.playerMapper.get(paddleUser).id = PlayerComponent.HUMAN_PLAYER;

		// Set up the computer player.
		Mappers.spriteMapper.get(paddleComp).sprite = atlas.createSprite("paddle");
		Mappers.positionMapper.get(paddleComp).setXY(((ProjectConstants.FB_WIDTH / 2) - 1) - 100 - Mappers.spriteMapper.get(paddleComp).sprite.getWidth(), -(Mappers.spriteMapper.get(paddleComp).sprite.getHeight() / 2));
		Mappers.bboxMapper.get(paddleComp).bbox.set(Mappers.spriteMapper.get(paddleComp).sprite.getBoundingRectangle());
		Mappers.playerMapper.get(paddleComp).id = PlayerComponent.COMPUTER_PLAYER;

		// Release the assets loader instance and mark the flag.
		AsyncAssetLoader.freeInstance();
		assetsLoaded = true;
	}

	@Override
	public void dispose() throws IllegalStateException {
		if(!entitiesCreated)
			throw new IllegalStateException("Entities have not been created before disposing assets.");

		if(!assetsLoaded)
			throw new IllegalStateException("Assets have not been loaded before disposing.");

		// Release the sound manager instance.
		CachedSoundManager.freeInstance();
	}
}
