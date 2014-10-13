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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gamejolt.mikykr5.poukemon.GameCore;
import com.gamejolt.mikykr5.poukemon.GameCore.game_states_t;

public class LogoScreenState extends BaseState{
	private Texture logo;
	private long then;

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

		core.batch.setProjectionMatrix(this.pixelPerfectCamera.combined);
		core.batch.begin();{
			core.batch.draw(logo, -logo.getWidth() / 2, -logo.getHeight() / 2);
		}core.batch.end();

		now = System.currentTimeMillis();
		delta = now - then;
		if(delta > 8000L){
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
		then = 0L;

		return true;
	};

	@Override
	public boolean keyDown(int keycode){
		then = 0L;

		return true;
	};
}
