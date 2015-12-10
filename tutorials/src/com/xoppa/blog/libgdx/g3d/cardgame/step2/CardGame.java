package com.xoppa.blog.libgdx.g3d.cardgame.step2;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CardGame extends ApplicationAdapter {
	public final static float CARD_WIDTH = 1f;
	public final static float CARD_HEIGHT = CARD_WIDTH * 277f / 200f;
	public final static float MINIMUM_VIEWPORT_SIZE = 5f;
	
	SpriteBatch spriteBatch;
	TextureAtlas atlas;
	Sprite front;
	Sprite back;
	OrthographicCamera cam;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		atlas = new TextureAtlas(data + "/carddeck.atlas");
		
		front = atlas.createSprite("back", 2);
		front.setSize(CARD_WIDTH, CARD_HEIGHT);
		front.setPosition(-1, 1);
		
		back = atlas.createSprite("clubs", 3);
		back.setSize(CARD_WIDTH, CARD_HEIGHT);
		back.setPosition(1, 1);
		
		cam = new OrthographicCamera();
	}

	@Override
	public void resize(int width, int height) {
		if (width > height) {
			cam.viewportHeight = MINIMUM_VIEWPORT_SIZE;
			cam.viewportWidth = cam.viewportHeight * (float)width / (float)height;
		} else {
			cam.viewportWidth = MINIMUM_VIEWPORT_SIZE;
			cam.viewportHeight = cam.viewportWidth * (float)height / (float)width;
		}
		cam.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();
		front.draw(spriteBatch);
		back.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		atlas.dispose();
	}
}
