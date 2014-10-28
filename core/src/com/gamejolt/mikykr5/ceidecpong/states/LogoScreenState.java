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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gamejolt.mikykr5.ceidecpong.GameCore;
import com.gamejolt.mikykr5.ceidecpong.GameCore.game_states_t;

/**
 * A simple state that just shows a logo.
 * 
 * @author Miguel Astor
 */
public class LogoScreenState extends BaseState{
	/**
	 * The maximum amount of time to show the logo.
	 */
	private static final long MAX_TIME = 8000L;

	/**
	 * The logo to show.
	 */
	private Texture logo;

	/**
	 * A time counter.
	 */
	private long then;

	/**
	 * Create the logo.
	 * 
	 * @param core A game core. See {@link BaseState#BaseState(GameCore)} for details.
	 * @throws IllegalArgumentException If core is null.
	 */
	public LogoScreenState(final GameCore core) throws IllegalArgumentException{
		super(core);
		then = System.currentTimeMillis();
		logo = new Texture(Gdx.files.internal("data/gfx/textures/monkey.png"));
	}

	@Override
	public void render(float _){
		long now, delta;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render the logo.
		core.batch.setProjectionMatrix(this.pixelPerfectCamera.combined);
		core.batch.begin();{
			core.batch.draw(logo, -logo.getWidth() / 2, -logo.getHeight() / 2);
		}core.batch.end();

		// Check if the time expired, then change to the next state if needed.
		now = System.currentTimeMillis();
		delta = now - then;
		if(delta > MAX_TIME){
			core.nextState = game_states_t.LOADING;
			then = now;
		}
	}

	@Override
	public void dispose() {
		logo.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		// Set the time counter to 0 so that a state change will be scheduled on the next frame. 
		then = 0L;
		return true;
	};

	@Override
	public boolean keyDown(int keycode){
		// Set the time counter to 0 so that a state change will be scheduled on the next frame.
		then = 0L;
		return true;
	};
}
