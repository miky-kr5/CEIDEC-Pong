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
package com.gamejolt.mikykr5.ceidecpong.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.gamejolt.mikykr5.ceidecpong.interfaces.AssetsLoadedListener;
import com.gamejolt.mikykr5.ceidecpong.utils.AsyncAssetLoader;

/**
 * A reusable scrolling infinite background similar to those used commonly in PSX games.
 * 
 * @author Miguel Astor
 */
public class ScrollingBackground implements Disposable, AssetsLoadedListener{
	/**
	 * Tag used for logging.
	 */
	private static final String TAG = "SCROLLING_BACKGROUND";

	/**
	 * Class name used for logging.
	 */
	private static final String CLASS_NAME = ScrollingBackground.class.getSimpleName();

	/**
	 * The path of the shader used by the effect.
	 */
	private static final String SHADER_PATH = "shaders/movingBckg/movingBckg";

	/**
	 * The assets loader instance.
	 */
	private AsyncAssetLoader loader;

	/**
	 * The texture to render.
	 */
	private Texture                 backgroundTexture;

	/**
	 * An sprite to hold the texture.
	 */
	private Sprite                  background;

	/**
	 * A compiled shader object used during rendering.
	 */
	private ShaderProgram           shader;

	/**
	 * Holds the location of the u_scaling variable of the shader.
	 */
	private int                     u_scaling;

	/**
	 * Holds the location of the u_displacement variable of the shader.
	 */
	private int                     u_displacement;

	/**
	 * The scaling to apply to the texture.
	 */
	private float                   scaling;

	/**
	 * The displacement to apply to the texture.
	 */
	private float                   displacement;
	private String                  texturePath;

	/**
	 * Creates a new effect using a default scaling and displacement. The
	 * texture is loaded with {@link AsyncAssetLoader}.
	 * 
	 * @param texturePath The internal path of the texture to use.
	 */
	public ScrollingBackground(String texturePath){
		this(texturePath, 2.0f, 0.0f, true);
	}

	/**
	 * Creates a new effect using a default scaling and displacement.
	 * 
	 * @param texturePath The internal path of the texture to use.
	 * @param loadAsync Whether to use {@link AsyncAssetLoader} or not.
	 */
	public ScrollingBackground(String texturePath, boolean loadAsync){
		this(texturePath, 2.0f, 0.0f, loadAsync);
	}

	/**
	 * Creates a new effect using a default displacement. The
	 * texture is loaded with {@link AsyncAssetLoader}.
	 * 
	 * @param texturePath The internal path of the texture to use.
	 * @param scaling The scaling to apply to the texture.
	 */
	public ScrollingBackground(String texturePath, float scaling){
		this(texturePath, scaling, 0.0f, true);
	}

	/**
	 * Creates a new effect using a default displacement.
	 * 
	 * @param texturePath The internal path of the texture to use.
	 * @param scaling The scaling to apply to the texture.
	 * @param loadAsync Whether to use {@link AsyncAssetLoader} or not.
	 */
	public ScrollingBackground(String texturePath, float scaling, boolean loadAsync){
		this(texturePath, scaling, 0.0f, loadAsync);
	}

	/**
	 * Creates a new effect.
	 * 
	 * @param texturePath The internal path of the texture to use.
	 * @param scaling The scaling to apply to the texture.
	 * @param displacement The displacement to apply to the texture.
	 * @param loadAsync Whether to use {@link AsyncAssetLoader} or not.
	 */
	public ScrollingBackground(String texturePath, float scaling, float displacement, boolean loadAsync){
		if(loadAsync){
			// If an asynchronous load was requested then use the assets loader.
			loader = AsyncAssetLoader.getInstance();
			loader.addAssetToLoad(texturePath, Texture.class);
			loader.addListener(this);
		}else{
			// Else load the texture manually.
			backgroundTexture = new Texture(Gdx.files.internal(texturePath));
			initGraphics();
		}

		// Load and compile the shader. If the shader failed to load or compile the disable the effect.
		shader = new ShaderProgram(Gdx.files.internal(SHADER_PATH + "_vert.glsl"), Gdx.files.internal(SHADER_PATH + "_frag.glsl"));
		if(!shader.isCompiled()){
			Gdx.app.error(TAG, CLASS_NAME + ".ScrollingBackground() :: Failed to compile the shader.");
			Gdx.app.error(TAG, CLASS_NAME + shader.getLog());
			shader = null;
			u_scaling = 0;
			u_displacement = 0;
		}else{
			// If the shader compiled fine then cache it's uniforms.
			u_scaling = shader.getUniformLocation("u_scaling");
			u_displacement = shader.getUniformLocation("u_displacement");
		}

		// Set all other fields.
		this.texturePath = texturePath;
		this.scaling = scaling;
		this.displacement = displacement;
	}

	/**
	 * Render this effect.
	 * 
	 * @param batch The {@link SpriteBatch} to use for the rendering.
	 * @throws IllegalStateException If the {@link SpriteBatch} did not call {@link SpriteBatch#begin()} befor this method was called.
	 */
	public void render(SpriteBatch batch) throws IllegalStateException{
		if(!batch.isDrawing())
			throw new IllegalStateException("Must be called between SpriteBatch.begin() and SpriteBatch.end()");

		// If the shader was loaded then set it as the current shader.
		if(shader != null){
			batch.setShader(shader);
			shader.setUniformf(u_scaling, scaling);
			shader.setUniformf(u_displacement, displacement);
		}
		
		// Render.
		background.draw(batch);

		// If the shader was loaded then disable it.
		if(shader != null)
			batch.setShader(null);

		// Update the displacement a little bit.
		// GOTCHA: This will look slower or faster depending on the speed of the computer.
		displacement = displacement < 0.0f ? 1.0f : displacement - 0.0005f;
	}

	@Override
	public void dispose(){
		// Dispose all graphic assets.
		backgroundTexture.dispose();
		if(shader != null)
			shader.dispose();
	}

	@Override
	public void onAssetsLoaded(){
		// Get the graphics and initialize them. Then release the assets loader.
		backgroundTexture = loader.getAsset(texturePath, Texture.class);
		initGraphics();
		AsyncAssetLoader.freeInstance();
	}

	/**
	 * Sets the graphic asset's parameters and creates the sprite.
	 */
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
