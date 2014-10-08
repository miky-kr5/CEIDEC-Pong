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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gamejolt.mikykr5.poukemon.GameCore;

public abstract class BaseState implements Screen, InputProcessor{
	protected GameCore core;
	protected boolean stateEnabled;
	protected OrthographicCamera pixelPerfectCamera;
	protected Vector3 win2world;
	protected Vector2 touchPointWorldCoords;

	public BaseState(){
		this.pixelPerfectCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		win2world = new Vector3(0.0f, 0.0f, 0.0f);
		touchPointWorldCoords = new Vector2();
	}

	/*;;;;;;;;;;;;;;;;;
	  ; STATE METHODS ;
	  ;;;;;;;;;;;;;;;;;*/

	public void onStateEnabled(){
		stateEnabled = true;
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}

	public void onStateDisabled(){
		stateEnabled = false;
		Gdx.input.setInputProcessor(null);
		Gdx.input.setCatchBackKey(false);
		Gdx.input.setCatchMenuKey(false);
	}

	/*;;;;;;;;;;;;;;;;;;
	  ; SCREEN METHODS ;
	  ;;;;;;;;;;;;;;;;;;*/

	@Override
	public abstract void render(float delta);

	@Override
	public abstract void dispose();

	@Override
	public void resize(int width, int height){ }

	@Override
	public void show(){ }

	@Override
	public void hide(){ }

	@Override
	public void pause(){ }

	@Override
	public void resume(){ }

	/*;;;;;;;;;;;;;;;;;;
	  ; HELPER METHODS ;
	  ;;;;;;;;;;;;;;;;;;*/

	protected final void unprojectTouch(int screenX, int screenY){
		win2world.set(screenX, screenY, 0.0f);
		pixelPerfectCamera.unproject(win2world);
		touchPointWorldCoords.set(win2world.x, win2world.y);
	}

	/*;;;;;;;;;;;;;;;;;;;;;;;;;;;
	  ; INPUT PROCESSOR METHODS ;
	  ;;;;;;;;;;;;;;;;;;;;;;;;;;;*/

	@Override
	public boolean keyDown(int keycode){
		return false;
	};

	@Override
	public boolean keyUp(int keycode){
		return false;
	};

	@Override
	public boolean keyTyped(char character){
		return false;
	};

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		return false;
	};

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		return false;
	};

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		return false;
	};

	@Override
	public boolean mouseMoved(int screenX, int screenY){
		return false;
	};

	@Override
	public boolean scrolled(int amount){
		return false;
	};
}