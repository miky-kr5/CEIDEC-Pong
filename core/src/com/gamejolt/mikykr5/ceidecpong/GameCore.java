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
package com.gamejolt.mikykr5.ceidecpong;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.primitives.MutableFloat;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gamejolt.mikykr5.ceidecpong.interfaces.AssetsLoadedListener;
import com.gamejolt.mikykr5.ceidecpong.states.BaseState;
import com.gamejolt.mikykr5.ceidecpong.states.InGameState;
import com.gamejolt.mikykr5.ceidecpong.states.LoadingState;
import com.gamejolt.mikykr5.ceidecpong.states.LogoScreenState;
import com.gamejolt.mikykr5.ceidecpong.states.MainMenuState;
import com.gamejolt.mikykr5.ceidecpong.utils.AsyncAssetLoader;

public class GameCore extends Game {
	private static final String TAG = "GAME_CORE";
	private static final String CLASS_NAME = GameCore.class.getSimpleName();

	public enum game_states_t {
		LOGO_SCREEN(0), MAIN_MENU(1), IN_GAME(2), QUIT(3), LOADING(4);

		private int value;

		private game_states_t(int value){
			this.value = value;
		}

		public int getValue(){
			return this.value;
		}

		public static int getNumStates(){
			return 5;
		}
	};

	private game_states_t currState;
	public game_states_t nextState;
	private BaseState[] states;

	public SpriteBatch batch;
	private OrthographicCamera pixelPerfectCamera;

	// Fade in/out effect fields.
	private Texture fadeTexture;
	private MutableFloat alpha;
	private Tween fadeOut;
	private Tween fadeIn;
	private boolean fading;

	@Override
	public void create () {
		AsyncAssetLoader loader = AsyncAssetLoader.getInstance();

		// Set up rendering fields and settings.
		ShaderProgram.pedantic = false;
		batch = new SpriteBatch();
		batch.enableBlending();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		pixelPerfectCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Prepare the fading effect.
		Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Format.RGBA4444);
		pixmap.setColor(0, 0, 0, 1);
		pixmap.fill();
		fadeTexture = new Texture(pixmap);
		pixmap.dispose();

		alpha   = new MutableFloat(1.0f);
		fadeOut = Tween.to(alpha, 0, 0.5f).target(1.0f).ease(TweenEquations.easeInQuint);
		fadeIn  = Tween.to(alpha, 0, 2.5f).target(0.0f).ease(TweenEquations.easeInQuint);
		fadeIn.start();
		fading  = true;

		// Create application states.
		states = new BaseState[game_states_t.getNumStates()];

		try{
			states[game_states_t.LOGO_SCREEN.getValue()] = new LogoScreenState(this);
			states[game_states_t.MAIN_MENU.getValue()] = new MainMenuState(this);
			states[game_states_t.IN_GAME.getValue()] = new InGameState(this);
			states[game_states_t.LOADING.getValue()] = new LoadingState(this);
			states[game_states_t.QUIT.getValue()] = null;
		}catch(IllegalArgumentException e){
			Gdx.app.error(TAG, CLASS_NAME + ".create(): Illegal argument caught creating states: ", e);
			System.exit(1);
			return;
		}

		for(BaseState state : states){
			if(state != null && state instanceof AssetsLoadedListener)
				loader.addListener((AssetsLoadedListener)state);
		}
		AsyncAssetLoader.freeInstance();
		loader = null;

		// Set the initial current and next states.
		currState = game_states_t.LOGO_SCREEN;
		nextState = null;
		this.setScreen(states[currState.getValue()]);

		// Set log level
		if(ProjectConstants.DEBUG){
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}else{
			Gdx.app.setLogLevel(Application.LOG_NONE);
		}
	}

	@Override
	public void render () {
		super.render();

		// If the current state set a value for nextState then switch to that state.
		if(nextState != null){
			states[currState.getValue()].onStateDisabled();

			if(!fadeOut.isStarted()){
				// Start the fade out effect.
				fadeOut.start();
				fading = true;
			}else{
				// Update the fade out effect.
				fadeOut.update(Gdx.graphics.getDeltaTime());

				// When the fade out effect finishes, change to the requested state and launch the fade in effect.
				if(fadeOut.isFinished()){
					// Change to the requested state.
					if(nextState != game_states_t.QUIT){
						currState = nextState;
						nextState = null;
						//states[currState.getValue()].onStateEnabled();
						setScreen(states[currState.getValue()]);
					}else{
						nextState = null;
						Gdx.app.exit();
					}

					// Reset the fade out effect and launch the fade in.
					fadeOut.free();
					fadeOut = Tween.to(alpha, 0, 0.5f).target(1.0f).ease(TweenEquations.easeInQuint);
					fadeIn.start();
				}
			}
		}

		// If there is a fade in effect in progress.
		if(fadeIn.isStarted()){
			if(!fadeIn.isFinished()){
				// Update it until finished.
				fadeIn.update(Gdx.graphics.getDeltaTime());
			}else{
				// Stop and reset it when done.
				fading = false;
				fadeIn.free();
				fadeIn = Tween.to(alpha, 0, 0.5f).target(0.0f).ease(TweenEquations.easeInQuint);
				states[currState.getValue()].onStateEnabled();
			}
		}

		// Render the fading sprite with alpha blending.
		if(fading){
			batch.setProjectionMatrix(pixelPerfectCamera.combined);
			batch.begin();{
				batch.setColor(1, 1, 1, alpha.floatValue());
				batch.draw(fadeTexture, -(Gdx.graphics.getWidth() / 2), -(Gdx.graphics.getHeight() / 2));
				batch.setColor(1, 1, 1, 1);
			}batch.end();
		}
	}

	@Override
	public void dispose(){
		super.dispose();

		// Dispose screens.
		for(BaseState state : states){
			if(state != null)
				state.dispose();
		}

		fadeTexture.dispose();
		batch.dispose();
	}
}
