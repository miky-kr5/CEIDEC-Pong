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
package com.gamejolt.mikykr5.poukemon.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.gamejolt.mikykr5.poukemon.interfaces.AssetsLoadedListener;
import com.gamejolt.mikykr5.poukemon.utils.AsyncAssetLoader;

public class ScrollingBackground implements Disposable, AssetsLoadedListener{
	private static final String TAG = "SCROLLING_BACKGROUND";
	private static final String CLASS_NAME = ScrollingBackground.class.getSimpleName();
	private static final String SHADER_PATH = "shaders/movingBckg/movingBckg";

	private AsyncAssetLoader loader;
	private Texture                 backgroundTexture;
	private Sprite                  background;
	private ShaderProgram           shader;
	private int                     u_scaling;
	private int                     u_displacement;
	private float                   scaling;
	private float                   displacement;
	private String                  texturePath;

	public ScrollingBackground(String texturePath){
		this(texturePath, 2.0f, 0.0f, true);
	}

	public ScrollingBackground(String texturePath, boolean loadAsync){
		this(texturePath, 2.0f, 0.0f, loadAsync);
	}

	public ScrollingBackground(String texturePath, float scaling){
		this(texturePath, scaling, 0.0f, true);
	}

	public ScrollingBackground(String texturePath, float scaling, boolean loadAsync){
		this(texturePath, scaling, 0.0f, loadAsync);
	}

	public ScrollingBackground(String texturePath, float scaling, float displacement, boolean loadAsync){
		if(loadAsync){
			loader = AsyncAssetLoader.getInstance();
			loader.addAssetToLoad(texturePath, Texture.class);
			loader.addListener(this);
		}else{
			backgroundTexture = new Texture(Gdx.files.internal(texturePath));
			initGraphics();
		}

		shader = new ShaderProgram(Gdx.files.internal(SHADER_PATH + "_vert.glsl"), Gdx.files.internal(SHADER_PATH + "_frag.glsl"));
		if(!shader.isCompiled()){
			Gdx.app.error(TAG, CLASS_NAME + ".ScrollingBackground() :: Failed to compile the shader.");
			Gdx.app.error(TAG, CLASS_NAME + shader.getLog());
			shader = null;
		}

		u_scaling = shader.getUniformLocation("u_scaling");
		u_displacement = shader.getUniformLocation("u_displacement");

		this.texturePath = texturePath;
		this.scaling = scaling;
		this.displacement = displacement;
	}

	public void render(SpriteBatch batch) throws IllegalStateException{
		if(!batch.isDrawing())
			throw new IllegalStateException("Must be called between SpriteBatch.begin() and SpriteBatch.end()");

		if(shader != null){
			batch.setShader(shader);
			shader.setUniformf(u_scaling, scaling);
			shader.setUniformf(u_displacement, displacement);
		}
		background.draw(batch);

		if(shader != null) batch.setShader(null);

		displacement = displacement < 0.0f ? 1.0f : displacement - 0.0005f;
	}

	@Override
	public void dispose(){
		backgroundTexture.dispose();
		if(shader != null) shader.dispose();
	}

	@Override
	public void onAssetsLoaded(){
		backgroundTexture = loader.getAsset(texturePath, Texture.class);
		initGraphics();

		AsyncAssetLoader.freeInstance();
	}

	private void initGraphics(){
		// Set up the texture.
		backgroundTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// Set the sprite for rendering.
		background = new Sprite(backgroundTexture);
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background.setPosition(-(Gdx.graphics.getWidth() / 2), -(Gdx.graphics.getHeight() / 2));
	}
}
