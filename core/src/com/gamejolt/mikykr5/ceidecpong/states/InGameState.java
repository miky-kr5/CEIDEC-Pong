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

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.gamejolt.mikykr5.ceidecpong.GameCore;
import com.gamejolt.mikykr5.ceidecpong.GameCore.game_states_t;
import com.gamejolt.mikykr5.ceidecpong.ProjectConstants;
import com.gamejolt.mikykr5.ceidecpong.ecs.entities.EntityInitializerBase;
import com.gamejolt.mikykr5.ceidecpong.ecs.entities.PongEntityInitializer;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.CollisionDetectionSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.ComputerPlayerPositioningSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.HumanPlayerPositioningSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.PositioningSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.RenderingSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.ScoringSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.SoundSystem;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging.InterSystemMessage;
import com.gamejolt.mikykr5.ceidecpong.ecs.systems.messaging.InterSystemMessagingQueue;
import com.gamejolt.mikykr5.ceidecpong.interfaces.AssetsLoadedListener;

/**
 * The state in charge of executing and handling the game itself.
 * 
 * @author Miguel Astor
 */
public class InGameState extends BaseState implements AssetsLoadedListener{
	/**
	 * The {@link Engine} responsible for handling the ECS design pattern.
	 */
	private PooledEngine          engine;

	/**
	 * The entity creator.
	 */
	private EntityInitializerBase entityInitializer;

	/**
	 * A {@link FrameBuffer} used to render to a logical screen. This way the game can be
	 * designed for a single screen resolution and scaled to the running device's screen resolution.
	 */
	private FrameBuffer           frameBuffer;

	/**
	 * The screen width.
	 */
	private int                   w;

	/**
	 * The screen height.
	 */
	private int                   h;

	/**
	 * The aspect ratio of the logical screen.
	 */
	private final float           fbAspectRatio;

	/**
	 * Flag to indicate that all assets have been successfully loaded.
	 */
	private boolean               assetsLoaded;

	/**
	 * A pixel perfect camera used for rendering to the frame buffer.
	 */
	private OrthographicCamera    fbCamera;

	/**
	 * The bounding rectangle of the frame buffer.
	 */
	private Rectangle             fbBounds;

	/**
	 * An auxiliary vector for input calculations.
	 */
	private final Vector3         temp;

	/**
	 * Creates the state and the entity processing systems.
	 * 
	 * @param core A GameCore instance. See {@link BaseState#BaseState(GameCore)}.
	 * @throws IllegalArgumentException If core is null;
	 */
	public InGameState(final GameCore core) throws IllegalArgumentException{
		super(core);

		// Initialize all fields.
		engine = new PooledEngine();
		w = Gdx.graphics.getWidth();
		w = Gdx.graphics.getHeight();
		assetsLoaded = false;
		temp = new Vector3();

		// Create the framebuffer.
		frameBuffer = new FrameBuffer(Format.RGB565, ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT, false);
		fbAspectRatio = aspectRatio(ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT);
		fbCamera = new OrthographicCamera(ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT);
		fbBounds = new Rectangle();

		// Create all entities.
		entityInitializer = new PongEntityInitializer();
		entityInitializer.createAllEntities(engine);

		// Add systems in the order they will be processed.
		engine.addSystem(new HumanPlayerPositioningSystem());
		engine.addSystem(new ComputerPlayerPositioningSystem());
		engine.addSystem(new PositioningSystem());
		engine.addSystem(new CollisionDetectionSystem(engine));
		engine.addSystem(new SoundSystem());
		engine.addSystem(new RenderingSystem(core.batch));
		engine.addSystem(new ScoringSystem(core.batch));
	}

	@Override
	public void render(float delta){
		float x, y, renderW, renderH;

		if(assetsLoaded){
			// Clear the screen.
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			// Render the scene to a frame buffer so that we can apply screen effects later.
			frameBuffer.begin();{
				Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				// Update the game using the ECS pattern.
				core.batch.setProjectionMatrix(fbCamera.combined);
				core.batch.begin();{
					engine.update(delta);
				}core.batch.end();

			}frameBuffer.end();

			// Scale the frame buffer to the current screen size.
			renderW = w;
			renderH = renderW / fbAspectRatio;

			// Set the rendering position of the frame buffer.
			x = -(renderW / 2.0f);
			y = -(renderH / 2.0f);

			// Render the frame buffer applying screen effects if needed.
			core.batch.setProjectionMatrix(pixelPerfectCamera.combined);
			core.batch.begin();{
				core.batch.draw(frameBuffer.getColorBufferTexture(), x, y, renderW, renderH, 0, 0, ProjectConstants.FB_WIDTH, ProjectConstants.FB_HEIGHT, false, true);
			}core.batch.end();
		}
	}

	@Override
	public void dispose(){
		frameBuffer.dispose();
		entityInitializer.dispose();
		engine.removeAllEntities();
	}

	@Override
	public void resize(int width, int height){
		// It's important to call the resize method of the superclass to ensure
		// the pixel perfect camera is properly recreated.
		super.resize(width, height);
		w = width;
		h = height;
	}

	@Override
	public boolean keyDown(int keycode){
		// If the user pressed the escape key (the back button in Android) then go back
		// to the main menu. Else ignore the key.
		if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
			core.nextState = game_states_t.MAIN_MENU;
			return true;
		}

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		InterSystemMessage message;

		// If the user touched the screen inside the frame buffer then notify the player positioning system.
		if(touchInsideFrameBuffer(screenX, screenY)){
			message = new InterSystemMessage(HumanPlayerPositioningSystem.class.getCanonicalName());
			message.data.put("INPUT_Y", convertWorldYToFrameBufferY(screenY));
			InterSystemMessagingQueue.pushMessage(message);
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		InterSystemMessage message;

		// If the user touched the screen inside the frame buffer then notify the player positioning system.
		if(touchInsideFrameBuffer(screenX, screenY)){
			message = new InterSystemMessage(HumanPlayerPositioningSystem.class.getCanonicalName());
			message.data.put("INPUT_Y", convertWorldYToFrameBufferY(screenY));
			InterSystemMessagingQueue.pushMessage(message);
		}

		return true;
	}

	@Override
	public void onAssetsLoaded(){
		entityInitializer.setLoadableAssets(engine);
		assetsLoaded = true;
	}

	/**
	 *  Checks if the user clicked or touched a point inside the frame buffer.
	 * 
	 * @param screenX The X coordinate of the touch point.
	 * @param screenY The Y coordinate of the touch point.
	 * @return True if the touch point is inside the frame buffer.
	 */
	private boolean touchInsideFrameBuffer(int screenX, int screenY){
		float fbW, fbH;

		unprojectTouch(screenX, screenY);
		fbW = w;
		fbH = fbW / fbAspectRatio;
		fbBounds.set(-(fbW / 2.0f), -(fbH / 2.0f), fbW, fbH);

		if(fbBounds.contains(touchPointWorldCoords)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Converts the Y coordinate of a touch point in screen coordinates to the world coordinates of the framebuffer.
	 * 
	 * @param y The Y coordinate of a touch point.
	 * @return The Y coordinate converted to world coordinates.
	 */
	private float convertWorldYToFrameBufferY(float y){
		float fbH = h / fbAspectRatio;
		temp.set(0, y + (fbH / 2.0f), 0);
		fbCamera.unproject(temp, 0, 0, w, fbH);

		return temp.y;
	}

	/**
	 * Calculates the aspect ratio of a given width and height.
	 * 
	 * @param w The width.
	 * @param h The height.
	 * @return The aspect ratio.
	 */
	private float aspectRatio(float w, float h){
		return w / h;
	}
}
