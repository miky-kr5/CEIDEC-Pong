/*
 * Copyright (C) 2014 Miguel Angel Astor Romero
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gamejolt.mikykr5.poukemon.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.gamejolt.mikykr5.poukemon.GameCore;
import com.gamejolt.mikykr5.poukemon.GameCore.game_states_t;
import com.gamejolt.mikykr5.poukemon.effects.ScrollingBackground;
import com.gamejolt.mikykr5.poukemon.utils.AsyncAssetLoader;
import com.gamejolt.mikykr5.poukemon.utils.CachedFontManager;

public class LoadingState extends BaseState{
	private static final String CLASS_NAME = MainMenuState.class.getSimpleName();

	// Helper fields.
	private AsyncAssetLoader  loader;
	private CachedFontManager fontManager;
	private float             timeSinceShown;
	private boolean           loadingDone;

	// Graphic data.
	private BitmapFont          font;
	private ScrollingBackground scrollingBckg;

	public LoadingState(final GameCore core) throws IllegalArgumentException{
		super();

		loader = AsyncAssetLoader.getInstance();
		fontManager = CachedFontManager.getInstance();

		if(core == null)
			throw new IllegalArgumentException(CLASS_NAME + ": Core is null.");

		this.core = core;

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
				loader.notifyListeners();
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
