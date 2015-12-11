package com.xoppa.blog.libgdx.g3d.cardgame.stepa;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.xoppa.blog.libgdx.g3d.cardgame.step7.CardGame.CardBatch;

public class CardGame implements ApplicationListener {
	public final static float CARD_WIDTH = 1f;
	public final static float CARD_HEIGHT = CARD_WIDTH * 277f / 200f;
	public final static float MINIMUM_VIEWPORT_SIZE = 7f;

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
		
		private final float[] vertices;
		private final short[] indices;
		
		public final Matrix4 transform = new Matrix4();
		public final Vector3 position = new Vector3();
		public float angle;
		
		public Card(Suit suit, Pip pip, Sprite back, Sprite front) {
			assert(front.getTexture() == back.getTexture());
			this.suit = suit;
			this.pip = pip;
			front.setPosition(-front.getWidth() * 0.5f, -front.getHeight() * 0.5f);
			back.setPosition(-back.getWidth() * 0.5f, -back.getHeight() * 0.5f);
			
			vertices = convert(front.getVertices(), back.getVertices());
			indices = new short[] {0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4 };
		}
		
		private static float[] convert(float[] front, float[] back) {
			return new float[] {
				front[Batch.X2], front[Batch.Y2], 0.01f, 0, 0, 1, front[Batch.U2], front[Batch.V2],
				front[Batch.X1], front[Batch.Y1], 0.01f, 0, 0, 1, front[Batch.U1], front[Batch.V1],
				front[Batch.X4], front[Batch.Y4], 0.01f, 0, 0, 1, front[Batch.U4], front[Batch.V4],
				front[Batch.X3], front[Batch.Y3], 0.01f, 0, 0, 1, front[Batch.U3], front[Batch.V3],

				back[Batch.X1], back[Batch.Y1], -0.01f, 0, 0, -1, back[Batch.U1], back[Batch.V1],
				back[Batch.X2], back[Batch.Y2], -0.01f, 0, 0, -1, back[Batch.U2], back[Batch.V2],
				back[Batch.X3], back[Batch.Y3], -0.01f, 0, 0, -1, back[Batch.U3], back[Batch.V3],
				back[Batch.X4], back[Batch.Y4], -0.01f, 0, 0, -1, back[Batch.U4], back[Batch.V4]
			};
		}
		
		public void update() {
			float z = position.z + 0.5f * Math.abs(MathUtils.sinDeg(angle));
			transform.setToRotation(Vector3.Y, angle);
			transform.trn(position.x, position.y, z);
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
					front.setSize(CARD_WIDTH, CARD_HEIGHT);
					Sprite back = atlas.createSprite("back", backIndex);
					back.setSize(CARD_WIDTH, CARD_HEIGHT);
					cards[suit.index][pip.index] = new Card(suit, pip, back, front);
				}
			}
		}
		
		public Card getCard(Suit suit, Pip pip) {
			return cards[suit.index][pip.index];
		}
	}
	
	public static class CardBatch extends ObjectSet<Card> implements RenderableProvider, Disposable {
		MeshBuilder meshBuilder;
		Mesh mesh;
		Renderable renderable;
		
		public CardBatch(Material material) {
			final int maxNumberOfCards = 52;
			final int maxNumberOfVertices = maxNumberOfCards * 8;
			final int maxNumberOfIndices = maxNumberOfCards * 12;
			mesh = new Mesh(false, maxNumberOfVertices, maxNumberOfIndices,
					VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
			meshBuilder = new MeshBuilder();
			
			renderable = new Renderable();
			renderable.material = material;
		}
		
		@Override
		public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
			meshBuilder.begin(mesh.getVertexAttributes());
			meshBuilder.part("cards", GL20.GL_TRIANGLES, renderable.meshPart);
			for (Card card : this) {
				meshBuilder.setVertexTransform(card.transform);
				meshBuilder.addMesh(card.vertices, card.indices);
			}
			meshBuilder.end(mesh);
			
			renderable.shader = null;
			
			renderables.add(renderable);
		}
		
		@Override
		public void dispose() {
			mesh.dispose();
		}
	}
	
	public static class CardAction {
		public CardActions parent;
		public Card card;
		public final Vector3 fromPosition = new Vector3();
		public float fromAngle;
		public final Vector3 toPosition = new Vector3();
		public float toAngle;
		public float speed;
		public float alpha;
		
		public CardAction(CardActions parent) {
			this.parent = parent;
		}
		
		public void reset(Card card) {
			this.card = card;
			fromPosition.set(card.position);
			fromAngle = card.angle;
			alpha = 0f;
		}
		
		public void update(float delta) {
			alpha += delta * speed;
			if (alpha >= 1f) {
				alpha = 1f;
				parent.actionComplete(this);
			}
			card.position.set(fromPosition).lerp(toPosition, alpha);
			card.angle = fromAngle + alpha * (toAngle - fromAngle);
			card.update();
		}
	}
	
	public static class CardActions {
		Pool<CardAction> actionPool = new Pool<CardAction>() {
			protected CardAction newObject() {
				return new CardAction(CardActions.this);
			}
		};
		Array<CardAction> actions = new Array<CardAction>();
		
		public void actionComplete(CardAction action) {
			actions.removeValue(action, true);
			actionPool.free(action);
		}
		
		public void update(float delta) {
			for (CardAction action : actions) {
				action.update(delta);
			}
		}
		
		public void animate(Card card, float x, float y, float z, float angle, float speed) {
			CardAction action = actionPool.obtain();
			action.reset(card);
			action.toPosition.set(x, y, z);
			action.toAngle = angle;
			action.speed = speed;
			actions.add(action);
		}
	}
	
	TextureAtlas atlas;
	Sprite front;
	Sprite back;
	PerspectiveCamera cam;
	CardDeck deck;
	CardBatch cards;
	CameraInputController camController;
	ModelBatch modelBatch;
	Model tableTopModel;
	ModelInstance tableTop;
	Environment environment;
	DirectionalShadowLight shadowLight;
	ModelBatch shadowBatch;
	CardActions actions;

	@Override
	public void create() {
		modelBatch = new ModelBatch();
		atlas = new TextureAtlas(data + "/carddeck.atlas");
		Material material = new Material(
				TextureAttribute.createDiffuse(atlas.getTextures().first()),
				new BlendingAttribute(false, 1f),
				FloatAttribute.createAlphaTest(0.2f));
		cards = new CardBatch(material);
		
		deck = new CardDeck(atlas, 3);
		
		Card card = deck.getCard(Suit.Spades, Pip.King);
		card.position.set(3.5f, -2.5f, 0.01f);
		card.angle = 180f;
		card.update();
		cards.add(card);
		
		cam = new PerspectiveCamera();
		cam.position.set(0, 0, 10);
		cam.lookAt(0, 0, 0);
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
		
		ModelBuilder builder = new ModelBuilder();
		builder.begin();
		builder.node().id = "top";
		builder.part("top", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(new Color(0x63750A))))
			.box(0f, 0f, -0.5f, 20f, 20f, 1f);
		tableTopModel = builder.end();
		tableTop = new ModelInstance(tableTopModel);
		
		shadowBatch = new ModelBatch(new DepthShaderProvider());
		shadowLight = new DirectionalShadowLight(1024, 1024, 10f, 10f, 1f, 20f);
		shadowLight.set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f);
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
		environment.add(shadowLight);
		environment.shadowMap = shadowLight;
		
		actions = new CardActions();
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

	private float spawnTimer = -1f;
	@Override
	public void render() {
		final float delta = Math.min(1/30f, Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camController.update();
		
		if (spawnTimer < 0) {
			if (Gdx.input.justTouched())
				spawnTimer = 1f;
		} else if ((spawnTimer -= delta) <= 0f) {
			spawnTimer = 0.25f;
			spawn();
		}
		
		actions.update(delta);
		
		shadowLight.begin(Vector3.Zero, Vector3.Zero);
		shadowBatch.begin(shadowLight.getCamera());
		shadowBatch.render(cards);
		shadowBatch.end();
		shadowLight.end();
		
		modelBatch.begin(cam);
		modelBatch.render(tableTop, environment);
		modelBatch.render(cards, environment);
		modelBatch.end();
	}
	
	int pipIdx = -1;
	int suitIdx = 0;
	float spawnX = -0.5f;
	float spawnY = 0f;
	float spawnZ = 0f;
	public void spawn() {
		if (++pipIdx >= Pip.values().length) {
			pipIdx = 0;
			suitIdx = (suitIdx + 1) % Suit.values().length;
		}
		Suit suit = Suit.values()[suitIdx];
		Pip pip = Pip.values()[pipIdx];
		Gdx.app.log("Spawn", suit + " - " + pip);
		Card card = deck.getCard(suit, pip);
		card.position.set(3.5f, -2.5f, 0.01f);
		card.angle = 180;
		if (!cards.contains(card))
			cards.add(card);
		spawnX = (spawnX + 0.5f);
		if (spawnX > 6f) {
			spawnX = 0f;
			spawnY = (spawnY + 0.5f) % 2f;
		}
		spawnZ += 0.001f;
		actions.animate(card, -3.5f + spawnX, 2.5f - spawnY, 0.01f + spawnZ, 0f, 1f);
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
		cards.dispose();
		tableTopModel.dispose();
		shadowBatch.dispose();
		shadowLight.dispose();
	}
}
