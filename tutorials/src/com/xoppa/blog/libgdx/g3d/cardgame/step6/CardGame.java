package com.xoppa.blog.libgdx.g3d.cardgame.step6;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
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
	
	public static class Card extends Renderable {
		public final Suit suit;
		public final Pip pip;
		
		public Card(Suit suit, Pip pip, Sprite back, Sprite front) {
			assert(front.getTexture() == back.getTexture());
			this.suit = suit;
			this.pip = pip;
			
			material = new Material(
					TextureAttribute.createDiffuse(front.getTexture()),
					new BlendingAttribute(false, 1f),
					FloatAttribute.createAlphaTest(0.5f)
				);
			
			front.setSize(CARD_WIDTH, CARD_HEIGHT);
			back.setSize(CARD_WIDTH, CARD_HEIGHT);			
			front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);
			back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
			
			float[] vertices = convert(front.getVertices(), back.getVertices());
			short[] indices = new short[] {0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4 };
			
			// FIXME: this Mesh needs to be disposed
			meshPart.mesh = new Mesh(true, 8, 12, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
			meshPart.mesh.setVertices(vertices);
			meshPart.mesh.setIndices(indices);
			meshPart.offset = 0;
			meshPart.size = meshPart.mesh.getNumIndices();
			meshPart.primitiveType = GL20.GL_TRIANGLES;
			meshPart.update();
		}
		
		private static float[] convert(float[] front, float[] back) {
			return new float[] {
				front[Batch.X2], front[Batch.Y2], 0, 0, 0, 1, front[Batch.U2], front[Batch.V2],
				front[Batch.X1], front[Batch.Y1], 0, 0, 0, 1, front[Batch.U1], front[Batch.V1],
				front[Batch.X4], front[Batch.Y4], 0, 0, 0, 1, front[Batch.U4], front[Batch.V4],
				front[Batch.X3], front[Batch.Y3], 0, 0, 0, 1, front[Batch.U3], front[Batch.V3],

				back[Batch.X1], back[Batch.Y1], 0, 0, 0, -1, back[Batch.U1], back[Batch.V1],
				back[Batch.X2], back[Batch.Y2], 0, 0, 0, -1, back[Batch.U2], back[Batch.V2],
				back[Batch.X3], back[Batch.Y3], 0, 0, 0, -1, back[Batch.U3], back[Batch.V3],
				back[Batch.X4], back[Batch.Y4], 0, 0, 0, -1, back[Batch.U4], back[Batch.V4]
			};
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
	
	TextureAtlas atlas;
	PerspectiveCamera cam;
	CardDeck deck;
	ObjectSet<Card> cards;
	CameraInputController camController;
	ModelBatch modelBatch;

	@Override
	public void create() {
		modelBatch = new ModelBatch();
		
		atlas = new TextureAtlas(data + "/carddeck.atlas");
		cards = new ObjectSet<Card>();

		deck = new CardDeck(atlas, 3);

		Card card1 = deck.getCard(Suit.Diamonds, Pip.Queen);
		card1.worldTransform.translate(-1, 0, 0);
		cards.add(card1);
		
		Card card2 = deck.getCard(Suit.Hearts, Pip.Four);
		card2.worldTransform.translate(0, 0, 0);
		cards.add(card2);
		
		Card card3 = deck.getCard(Suit.Spades, Pip.Ace);
		card3.worldTransform.translate(1, 0, 0);
		cards.add(card3);
		
		cam = new PerspectiveCamera();
		cam.position.set(0, 0, 10);
		cam.lookAt(0, 0, 0);
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void resize(int width, int height) {
		float halfHeight = MINIMUM_VIEWPORT_SIZE * 0.5f;
		if (height > width)
			halfHeight *= (float)height / (float)width;
		float halfFovRadians = MathUtils.degreesToRadians * cam.fieldOfView * 0.5f;
		float distance = halfHeight / (float)Math.tan(halfFovRadians);
		
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.position.set(0, 0, distance);
		cam.lookAt(0, 0, 0);
		cam.update();
	}

	@Override
	public void render() {
		final float delta = Math.min(1/30f, Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camController.update();
		
		cards.first().worldTransform.rotate(Vector3.Y, 90 * delta);
		
		modelBatch.begin(cam);
		for (Card card : cards)
			modelBatch.render(card);
		modelBatch.end();
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
		modelBatch.dispose();
		atlas.dispose();
	}
}
