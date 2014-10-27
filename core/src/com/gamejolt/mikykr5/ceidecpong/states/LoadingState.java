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
package com.gamejolt.mikykr5.ceidecpong.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.gamejolt.mikykr5.ceidecpong.GameCore;
import com.gamejolt.mikykr5.ceidecpong.GameCore.game_states_t;
import com.gamejolt.mikykr5.ceidecpong.effects.ScrollingBackground;
import com.gamejolt.mikykr5.ceidecpong.utils.AsyncAssetLoader;
import com.gamejolt.mikykr5.ceidecpong.utils.managers.CachedFontManager;

public class LoadingState extends BaseState{
	// Helper fields.
	private AsyncAssetLoader  loader;
	private CachedFontManager fontManager;
	private float             timeSinceShown;
	private boolean           loadingDone;

	// Graphic data.
	private BitmapFont          font;
	private ScrollingBackground scrollingBckg;

	public LoadingState(final GameCore core) throws IllegalArgumentException{
		super(core);

		loader = AsyncAssetLoader.getInstance();
		fontManager = CachedFontManager.getInstance();

		// Create the start button font.
		font = fontManager.loadFont("data/fonts/Big_Bottom_Cartoon.ttf", CachedFontManager.BASE_FONT_SIZE * 3);

		// Set up the background.
		scrollingBckg = new ScrollingBackground("data/gfx/textures/floortiles.png", false);

		stateEnabled = false;
		loadingDone = false;
	}

	@Override
	public void show(){
		timeSinceShown = 0.0f;
	}

	@Override
	public void render(float delta){
		TextBounds bounds = font.getBounds("Loading");

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		core.batch.setProjectionMatrix(pixelPerfectCamera.combined);
		core.batch.begin();{
			// Render background.
			scrollingBckg.render(core.batch);

			font.setColor(Color.BLACK);
			font.draw(core.batch, "Loading", -(bounds.width / 2), -(bounds.height / 2));

		}core.batch.end();

		if(!loadingDone && loader != null){
			if(loader.loadAssets()){
				loadingDone = true;
			}
		}

		timeSinceShown += delta;
		if(loadingDone && timeSinceShown >= 3.0f)
			core.nextState = game_states_t.MAIN_MENU;
	}

	@Override
	public void dispose(){
		scrollingBckg.dispose();
		CachedFontManager.freeInstance();
		AsyncAssetLoader.freeInstance();
		loader = null;
	}
}
