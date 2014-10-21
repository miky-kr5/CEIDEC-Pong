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
package com.gamejolt.mikykr5.poukemon.states;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamejolt.mikykr5.poukemon.GameCore;
import com.gamejolt.mikykr5.poukemon.GameCore.game_states_t;
import com.gamejolt.mikykr5.poukemon.ProjectConstants;
import com.gamejolt.mikykr5.poukemon.ecs.entities.EntityInitializerBase;
import com.gamejolt.mikykr5.poukemon.ecs.entities.PongEntityInitializer;
import com.gamejolt.mikykr5.poukemon.ecs.systems.CollisionDetectionSystem;
import com.gamejolt.mikykr5.poukemon.ecs.systems.ComputerPlayerPositioningSystem;
import com.gamejolt.mikykr5.poukemon.ecs.systems.HumanPlayerPositioningSystem;
import com.gamejolt.mikykr5.poukemon.ecs.systems.InterSystemMessage;
import com.gamejolt.mikykr5.poukemon.ecs.systems.InterSystemMessagingQueue;
import com.gamejolt.mikykr5.poukemon.ecs.systems.PositioningSystem;
import com.gamejolt.mikykr5.poukemon.ecs.systems.RenderingSystem;
import com.gamejolt.mikykr5.poukemon.ecs.systems.ScoringSystem;
import com.gamejolt.mikykr5.poukemon.interfaces.AssetsLoadedListener;

public class InGameState extends BaseState implements AssetsLoadedListener{
	private PooledEngine          engine;
	private EntityInitializerBase entityInitializer;
	private FrameBuffer           frameBuffer;
	private int                   w;
	private int                   h;
	private final float           oldRatio;
	private boolean               assetsLoaded;
	private OrthographicCamera    fbCamera;
	private Rectangle             fbBounds;
	private Vector2               fbWoorldCoords;

	public InGameState(final GameCore core) throws IllegalArgumentException{
		super(core);

		engine = new PooledEngine();
		frameBuffer = new FrameBuffer(Format.RGB565, ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT, false);
		fbBounds = new Rectangle();
		w = Gdx.graphics.getWidth();
		w = Gdx.graphics.getHeight();
		oldRatio = aspectRatio(ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT);
		assetsLoaded = false;
		fbCamera = new OrthographicCamera(ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT);
		fbWoorldCoords = new Vector2();

		// Create all entities.
		entityInitializer = new PongEntityInitializer();
		entityInitializer.createAllEntities(engine);

		// Add systems in the order they will be processed.
		engine.addSystem(new PositioningSystem());
		engine.addSystem(new CollisionDetectionSystem(engine));
		engine.addSystem(new HumanPlayerPositioningSystem());
		engine.addSystem(new ComputerPlayerPositioningSystem());
		engine.addSystem(new ScoringSystem(core.batch));
		engine.addSystem(new RenderingSystem(core.batch));
	}

	@Override
	public void render(float delta){
		float x, y, renderW, renderH;

		if(assetsLoaded){
			// Clear the screen.
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			// Render the scene to a frame buffer so that we can apply screen effects later.
			frameBuffer.begin();{
				Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				// Update the game using the ECS pattern.
				core.batch.setProjectionMatrix(fbCamera.combined);
				core.batch.begin();{
					engine.update(delta);
				}core.batch.end();

			}frameBuffer.end();

			// Scale the frame buffer to the current screen size.
			renderW = w;
			renderH = renderW / oldRatio;

			// Set the rendering position of the frame buffer.
			x = -(renderW / 2.0f);
			y = -(renderH / 2.0f);

			// Render the frame buffer applying screen effects if needed.
			core.batch.setProjectionMatrix(pixelPerfectCamera.combined);
			core.batch.begin();{
				core.batch.draw(frameBuffer.getColorBufferTexture(), x, y, renderW, renderH, 0, 0, ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT, false, true);
			}core.batch.end();
		}
	}

	@Override
	public void dispose(){
		frameBuffer.dispose();
		entityInitializer.dispose();
		engine.removeAllEntities();
	}

	@Override
	public void resize(int width, int height){
		// It's important to call the resize method of the superclass to ensure
		// the pixel perfect camera is properly recreated.
		super.resize(width, height);
		w = width;
		h = height;
	}

	@Override
	public boolean keyDown(int keycode){
		if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
			core.nextState = game_states_t.MAIN_MENU;
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		InterSystemMessage message;

		if(touchInsideFrameBuffer(screenX, screenY)){
			message = new InterSystemMessage(HumanPlayerPositioningSystem.class.getCanonicalName());
			message.data.put("INPUT_Y", convertWorldHeightToFrameBufferHeight(touchPointWorldCoords.y));
			InterSystemMessagingQueue.pushMessage(message);
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		InterSystemMessage message;

		if(touchInsideFrameBuffer(screenX, screenY)){
			message = new InterSystemMessage(HumanPlayerPositioningSystem.class.getCanonicalName());
			message.data.put("INPUT_Y", convertWorldHeightToFrameBufferHeight(touchPointWorldCoords.y));
			InterSystemMessagingQueue.pushMessage(message);
		}

		return true;
	}

	@Override
	public void onAssetsLoaded() {
		entityInitializer.setLoadableAssets(engine);
		assetsLoaded = true;
	}

	private boolean touchInsideFrameBuffer(int screenX, int screenY){
		float fbW, fbH;

		unprojectTouch(screenX, screenY);
		fbW = w;
		fbH = fbW / oldRatio;
		fbBounds.set(-(fbW / 2.0f), -(fbH / 2.0f), fbW, fbH);

		if(fbBounds.contains(touchPointWorldCoords)){
			return true;
		}else{
			return false;
		}
	}

	private float convertWorldHeightToFrameBufferHeight(float height){
		float newHeight, oldHeight, b = (float)ProjectConstants.FB_HEIGHT / (float)h;

		oldHeight = height + ((ProjectConstants.FB_HEIGHT / 2.0f) - 1.0f);
		oldHeight /= (float)h;
		newHeight = (oldHeight * b) * ProjectConstants.FB_HEIGHT;
		newHeight -= ProjectConstants.FB_HEIGHT;

		return newHeight;
	}

	/**
	 * Calculates the aspect ratio of a given width and height.
	 * 
	 * @param w The width.
	 * @param h The height.
	 * @return The aspect ratio.
	 */
	private float aspectRatio(float w, float h){
		return w / h;
	}
}
