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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gamejolt.mikykr5.poukemon.GameCore;
import com.gamejolt.mikykr5.poukemon.GameCore.game_states_t;

public class LogoScreen extends BaseState {
	private static final String CLASS_NAME = LogoScreen.class.getSimpleName(); 
	private Texture logo;
	private long then;

	public LogoScreen(final GameCore core){
		if(core == null)
			throw new IllegalArgumentException(CLASS_NAME + ": Core is null.");

		this.core = core;
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
			core.nextState = game_states_t.MAIN_MENU;
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
