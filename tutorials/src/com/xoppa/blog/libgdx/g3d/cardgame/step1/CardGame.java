package com.xoppa.blog.libgdx.g3d.cardgame.step1;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class CardGame extends ApplicationAdapter {
	SpriteBatch spriteBatch;
	TextureAtlas atlas;
	Sprite front;
	Sprite back;
	
	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		atlas = new TextureAtlas(data + "/carddeck.atlas");
		
		front = atlas.createSprite("clubs", 2);
		front.setPosition(100, 100);
		
		back = atlas.createSprite("back", 3);
		back.setPosition(300, 100);
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
		atlas.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		spriteBatch.begin();
		front.draw(spriteBatch);
		back.draw(spriteBatch);
		spriteBatch.end();
	}
}
