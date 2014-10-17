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
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gamejolt.mikykr5.poukemon.GameCore;
import com.gamejolt.mikykr5.poukemon.GameCore.game_states_t;
import com.gamejolt.mikykr5.poukemon.ecs.entities.EntityInitializerBase;
import com.gamejolt.mikykr5.poukemon.ecs.entities.PoukemonEntityInitializer;
import com.gamejolt.mikykr5.poukemon.interfaces.AssetsLoadedListener;

public class InGameState extends BaseState implements AssetsLoadedListener{
	private static final int FB_WIDTH = 1920;
	private static final int FB_HEIGHT = 1080;

	private PooledEngine          engine;
	private EntityInitializerBase entityInitializer;
	private FrameBuffer           frameBuffer;
	private int                   w;
	private final float           oldRatio;
	private boolean               assetsLoaded;

	public InGameState(final GameCore core) throws IllegalArgumentException{
		super(core);

		engine = new PooledEngine();
		frameBuffer = new FrameBuffer(Format.RGB565, FB_WIDTH, FB_HEIGHT, false);
		w = Gdx.graphics.getWidth();
		oldRatio = aspectRatio(FB_WIDTH, FB_HEIGHT);
		assetsLoaded = false;

		entityInitializer = new PoukemonEntityInitializer();
		entityInitializer.createAllEntities(engine);
	}

	@Override
	public void render(float delta){
		float x, y, renderW, renderH;

		if(assetsLoaded){
			// Update the game using the ECS pattern.
			engine.update(delta);

			// Clear the screen.
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			// Render the scene to a frame buffer so that we can apply screen effects later.
			frameBuffer.begin();{
				Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			}frameBuffer.end();

			// Scale the frame buffer to the current screen size.
			renderW = w;
			renderH = renderW / oldRatio;

			// Set the rendering position of the frame buffer.
			x = -(renderW / 2.0f);
			y = -(renderH / 2.0f);

			// Render the frame buffer applying screen effects if needed.
			core.batch.begin();{
				core.batch.draw(frameBuffer.getColorBufferTexture(), x, y, renderW, renderH);
			}core.batch.end();
		}
	}

	@Override
	public void dispose(){
		frameBuffer.dispose();
		engine.removeAllEntities();
	}

	@Override
	public void resize(int width, int height){
		// It's important to call the resize method of the superclass to ensure
		// the pixel perfect camera is properly recreated.
		super.resize(FB_WIDTH, FB_HEIGHT);
		w = width;
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
		unprojectTouch(screenX, screenY);

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		unprojectTouch(screenX, screenY);

		return false;
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

	@Override
	public void onAssetsLoaded() {
		entityInitializer.setLoadableAssets(engine);
		assetsLoaded = true;
	}
}
