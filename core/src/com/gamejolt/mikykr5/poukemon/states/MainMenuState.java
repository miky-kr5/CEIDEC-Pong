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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.gamejolt.mikykr5.poukemon.GameCore;
import com.gamejolt.mikykr5.poukemon.GameCore.game_states_t;
import com.gamejolt.mikykr5.poukemon.ProjectConstants;

public class MainMenuState extends BaseState{
	private static final String TAG = "MAIN_MENU";
	private static final String CLASS_NAME = MainMenuState.class.getSimpleName();
	private static final String SHADER_PATH = "shaders/movingBckg/movingBckg";

	// Helper fields.
	private   float   u_scaling[];
	private   float   u_displacement;

	// Buttons and other GUI components.
	private TextButton startButton;
	private Rectangle  startButtonBBox;
	private TextButton quitButton;
	private Rectangle  quitButtonBBox;
	private Sprite     background;

	// Graphic data for the start button.
	private Texture    menuButtonEnabledTexture;
	private Texture    menuButtonDisabledTexture;
	private Texture    menuButtonPressedTexture;
	private NinePatch  menuButtonEnabled9p;
	private NinePatch  menuButtonDisabled9p;
	private NinePatch  menuButtonPressed9p;
	private BitmapFont font;

	// Other graphics.
	private Texture       backgroundTexture;
	private ShaderProgram backgroundShader;

	// Button touch helper fields.
	private boolean startButtonTouched;
	private int     startButtonTouchPointer;
	private boolean quitButtonTouched;
	private int     quitButtonTouchPointer;

	public MainMenuState(final GameCore core) throws IllegalArgumentException{
		super();

		TextButtonStyle       textButtonStyle;
		FreeTypeFontGenerator fontGenerator;
		FreeTypeFontParameter fontParameters;

		if(core == null)
			throw new IllegalArgumentException(CLASS_NAME + ": Core is null.");

		this.core = core;

		// Create the start button background.
		menuButtonEnabledTexture = new Texture(Gdx.files.internal("data/gfx/gui/Anonymous_Pill_Button_Yellow.png"));
		menuButtonEnabled9p = new NinePatch(new TextureRegion(menuButtonEnabledTexture, 0, 0, menuButtonEnabledTexture.getWidth(), menuButtonEnabledTexture.getHeight()), 49, 49, 45, 45);
		menuButtonDisabledTexture = new Texture(Gdx.files.internal("data/gfx/gui/Anonymous_Pill_Button_Cyan.png"));
		menuButtonDisabled9p = new NinePatch(new TextureRegion(menuButtonDisabledTexture, 0, 0, menuButtonDisabledTexture.getWidth(), menuButtonDisabledTexture.getHeight()), 49, 49, 45, 45);
		menuButtonPressedTexture = new Texture(Gdx.files.internal("data/gfx/gui/Anonymous_Pill_Button_Blue.png"));
		menuButtonPressed9p = new NinePatch(new TextureRegion(menuButtonPressedTexture, 0, 0, menuButtonPressedTexture.getWidth(), menuButtonPressedTexture.getHeight()), 49, 49, 45, 45);

		// Create the start button font.
		fontParameters = new FreeTypeFontParameter();
		fontParameters.characters = ProjectConstants.FONT_CHARS;
		fontParameters.size = ProjectConstants.MENU_BUTTON_FONT_SIZE;
		fontParameters.flip = false;
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/d-puntillas-B-to-tiptoe.ttf"));
		font = fontGenerator.generateFont(fontParameters);
		fontGenerator.dispose();

		// Create the buttons.
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = new NinePatchDrawable(menuButtonEnabled9p);
		textButtonStyle.checked = new NinePatchDrawable(menuButtonPressed9p);
		textButtonStyle.disabled = new NinePatchDrawable(menuButtonDisabled9p);
		textButtonStyle.fontColor = new Color(Color.BLACK);
		textButtonStyle.downFontColor = new Color(Color.WHITE);
		textButtonStyle.disabledFontColor = new Color(Color.BLACK);

		startButton = new TextButton("Start game", textButtonStyle);
		startButton.setText("Start game");
		startButtonBBox = new Rectangle(0, 0, startButton.getWidth(), startButton.getHeight());

		quitButton = new TextButton("Quit", textButtonStyle);
		quitButton.setText("quit");
		quitButtonBBox = new Rectangle(0, 0, quitButton.getWidth(), quitButton.getHeight());

		// Set buttons.
		startButton.setPosition(-(startButton.getWidth() / 2), -(startButton.getHeight() / 2));
		startButtonBBox.setPosition(startButton.getX(), startButton.getY());
		quitButton.setPosition(-(quitButton.getWidth() / 2), (startButton.getY() - startButton.getHeight()) - 10);
		quitButtonBBox.setPosition(quitButton.getX(), quitButton.getY());

		// Set up the background.
		backgroundTexture = new Texture(Gdx.files.internal("data/gfx/textures/grass.png"));
		backgroundTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(backgroundTexture);
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background.setPosition(-(Gdx.graphics.getWidth() / 2), -(Gdx.graphics.getHeight() / 2));

		backgroundShader = new ShaderProgram(Gdx.files.internal(SHADER_PATH + "_vert.glsl"), Gdx.files.internal(SHADER_PATH + "_frag.glsl"));
		if(!backgroundShader.isCompiled()){
			Gdx.app.error(TAG, CLASS_NAME + ".MainMenuStateBase() :: Failed to compile the background shader.");
			Gdx.app.error(TAG, CLASS_NAME + backgroundShader.getLog());
			backgroundShader = null;
		}

		u_scaling = new float[2];
		u_scaling[0] = Gdx.graphics.getWidth() > Gdx.graphics.getHeight() ? 16.0f : 9.0f;
		u_scaling[1] = Gdx.graphics.getHeight() > Gdx.graphics.getWidth() ? 16.0f : 9.0f;

		u_displacement = 1.0f;

		startButtonTouched = false;
		startButtonTouchPointer = -1;
		quitButtonTouched = false;
		quitButtonTouchPointer = -1;

		stateEnabled = false;
	}

	@Override
	public void render(float delta){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		core.batch.setProjectionMatrix(pixelPerfectCamera.combined);
		core.batch.begin();{

			// Render background.
			core.batch.disableBlending();
			drawBackground(core.batch);
			core.batch.enableBlending();

			// Render buttons.
			startButton.draw(core.batch, 1.0f);
			quitButton.draw(core.batch, 1.0f);

		}core.batch.end();
	}

	@Override
	public void dispose(){
		menuButtonEnabledTexture.dispose();
		menuButtonDisabledTexture.dispose();
		menuButtonPressedTexture.dispose();
		backgroundTexture.dispose();
		if(backgroundShader != null) backgroundShader.dispose();
		font.dispose();
	}

	private void drawBackground(SpriteBatch batch){
		if(backgroundShader != null){
			batch.setShader(backgroundShader);
			backgroundShader.setUniformf("u_scaling", 2.0f);
			backgroundShader.setUniformf("u_displacement", u_displacement);
		}
		background.draw(batch);
		if(backgroundShader != null) batch.setShader(null);
		u_displacement = u_displacement < 0.0f ? 1.0f : u_displacement - 0.0005f;
	}

	/*;;;;;;;;;;;;;;;;;;;;;;;;;;
	  ; INPUT LISTENER METHODS ;
	  ;;;;;;;;;;;;;;;;;;;;;;;;;;*/

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		unprojectTouch(screenX, screenY);

		if(!startButton.isDisabled() && startButtonBBox.contains(touchPointWorldCoords) && !quitButtonTouched){
			startButton.setChecked(true);
			startButtonTouched = true;
			startButtonTouchPointer = pointer;
		}else if(!quitButton.isDisabled() && quitButtonBBox.contains(touchPointWorldCoords) && !startButtonTouched){
			quitButton.setChecked(true);
			quitButtonTouched = true;
			quitButtonTouchPointer = pointer;
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		unprojectTouch(screenX, screenY);

		if(!startButton.isDisabled() && startButtonBBox.contains(touchPointWorldCoords) && startButtonTouched){
			startButton.setChecked(false);
			startButtonTouched = false;
			startButtonTouchPointer = -1;
			core.nextState = game_states_t.IN_GAME;
		}else if(!quitButton.isDisabled() && quitButtonBBox.contains(touchPointWorldCoords) && quitButtonTouched){
			quitButton.setChecked(false);
			quitButtonTouched = false;
			quitButtonTouchPointer = -1;
			core.nextState = game_states_t.QUIT;
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		unprojectTouch(screenX, screenY);

		if(!startButton.isDisabled() && startButtonTouched && pointer == startButtonTouchPointer && !startButtonBBox.contains(touchPointWorldCoords)){
			startButtonTouchPointer = -1;
			startButtonTouched = false;
			startButton.setChecked(false);
		}else if(!quitButton.isDisabled() && quitButtonTouched && pointer == quitButtonTouchPointer && !quitButtonBBox.contains(touchPointWorldCoords)){
			quitButtonTouchPointer = -1;
			quitButtonTouched = false;
			quitButton.setChecked(false);
		}

		return true;
	}

	@Override
	public boolean keyDown(int keycode){
		if(keycode == Input.Keys.BACK){
			Gdx.app.exit();
			return true;
		}
		return false;
	}
}
