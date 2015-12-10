package com.xoppa.blog.libgdx.g3d.cardgame.step3;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class CardGame implements ApplicationListener {
	public final static float CARD_WIDTH = 1f;
	public final static float CARD_HEIGHT = CARD_WIDTH * 277f / 200f;
	public final static float MINIMUM_VIEWPORT_SIZE = 5f;

	public enum Suit {
		Clubs("clubs", 0), Diamonds("diamonds", 1), Hearts("hearts", 2), Spades("spades", 3);
		public final String name;
		public final int index;
		private Suit(String name, int index) {
			this.name = name;
			this.index = index;
		}
	}
	public enum Pip {
		Ace(1), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), Jack(11), Queen(12), King(13);
		public final int value;
		public final int index;
		private Pip(int value) {
			this.value = value;
			this.index = value - 1;
		}
	}
	
	public static class Card {
		public final Suit suit;
		public final Pip pip;
		
		private final Sprite front;
		private final Sprite back;
		
		private boolean turned;
		
		public Card(Suit suit, Pip pip, Sprite back, Sprite front) {
			back.setSize(CARD_WIDTH, CARD_HEIGHT);
			front.setSize(CARD_WIDTH, CARD_HEIGHT);
			this.suit = suit;
			this.pip = pip;
			this.back = back;
			this.front = front;
		}
		
		public void setPosition(float x, float y) {
			front.setPosition(x - 0.5f * front.getWidth(), y - 0.5f * front.getHeight());
			back.setPosition(x - 0.5f * back.getWidth(), y - 0.5f * back.getHeight());
		}
		
		public void turn() {
			turned = !turned;
		}
		
		public void draw(Batch batch) {
			if (turned)
				back.draw(batch);
			else
				front.draw(batch);
		}
	}
	
	public static class CardDeck {
		private final Card[][] cards;
		
		public CardDeck(TextureAtlas atlas, int backIndex) {
			cards = new Card[Suit.values().length][];
			for (Suit suit : Suit.values()) {
				cards[suit.index] = new Card[Pip.values().length];
				for (Pip pip : Pip.values()) {
					Sprite front = atlas.createSprite(suit.name, pip.value);
					Sprite back = atlas.createSprite("back", backIndex);
					cards[suit.index][pip.index] = new Card(suit, pip, back, front);
				}
			}
		}
		
		public Card getCard(Suit suit, Pip pip) {
			return cards[suit.index][pip.index];
		}
	}
	
	
	SpriteBatch spriteBatch;
	TextureAtlas atlas;
	Sprite front;
	Sprite back;
	OrthographicCamera cam;
	CardDeck deck;
	ObjectSet<Card> cards;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		atlas = new TextureAtlas(data + "/carddeck.atlas");
		cards = new ObjectSet<Card>();
		
		deck = new CardDeck(atlas, 3);
		
		Card card1 = deck.getCard(Suit.Diamonds, Pip.Queen);
		card1.setPosition(-1, 0);
		cards.add(card1);
		
		Card card2 = deck.getCard(Suit.Hearts, Pip.Four);
		card2.setPosition(0, 0);
		cards.add(card2);
		
		Card card3 = deck.getCard(Suit.Spades, Pip.Ace);
		card3.setPosition(1, 0);
		card3.turn();
		cards.add(card3);
		
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
		for (Card card : cards)
			card.draw(spriteBatch);
		spriteBatch.end();
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		atlas.dispose();
	}
}
