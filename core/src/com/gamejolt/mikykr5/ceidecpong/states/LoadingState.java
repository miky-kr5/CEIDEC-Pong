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

/**
 * A state that shows a loading screen and updates the {@link AsyncAssetLoader}.
 * 
 * @author Miguel Astor
 */
public class LoadingState extends BaseState{
	/**
	 * The {@link AsyncAssetLoader} instance that must be updated.
	 */
	private AsyncAssetLoader  loader;

	/**
	 * An instance of the {@link CachedFontManager} used to render the loading screen.
	 */
	private CachedFontManager fontManager;

	/**
	 * A time counter to avoid changing this state too quickly.
	 */
	private float             timeSinceShown;

	/**
	 * A flag to indicate that this state is finished.
	 */
	private boolean           loadingDone;

	/**
	 * The {@link BitmapFont} used to render the loading screen.
	 */
	private BitmapFont          font;

	/**
	 * A {@link ScrollingBackground} effect to make things livelier.
	 */
	private ScrollingBackground scrollingBckg;

	/**
	 * Creates the loading screen.
	 * 
	 * @param core A game core. See {@link BaseState#BaseState(GameCore)} for details.
	 * @throws IllegalArgumentException If core is null.
	 */
	public LoadingState(final GameCore core) throws IllegalArgumentException{
		super(core);

		// Get the singleton instances.
		loader = AsyncAssetLoader.getInstance();
		fontManager = CachedFontManager.getInstance();

		// Create the start button font.
		font = fontManager.loadFont("data/fonts/Big_Bottom_Cartoon.ttf", CachedFontManager.BASE_FONT_SIZE * 3);

		// Set up the background.
		scrollingBckg = new ScrollingBackground("data/gfx/textures/floortiles.png", false);

		// Set the flags.
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
			// Render the background.
			scrollingBckg.render(core.batch);

			font.setColor(Color.BLACK);
			font.draw(core.batch, "Loading", -(bounds.width / 2), -(bounds.height / 2));
		}core.batch.end();

		// If the loader has not finished loading then update it.
		if(!loadingDone && loader != null){
			// Update the loader and check if it finished.
			if(loader.loadAssets()){
				loadingDone = true;
			}
		}

		// If it has been at least 3 seconds since this state got enabled then
		// change to the next state if the loader is finished. This is to avoid
		// a graphics bug that happens in the core if a state transition is scheduled
		// while another effect is already in place. 
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
