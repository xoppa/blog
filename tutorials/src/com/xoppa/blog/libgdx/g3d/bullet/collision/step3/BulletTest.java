
package com.xoppa.blog.libgdx.g3d.bullet.collision.step3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;

/** @see https://xoppa.github.io/blog/using-the-libgdx-3d-physics-bullet-wrapper-part1/
 * @author Xoppa */
public class BulletTest implements ApplicationListener {
	PerspectiveCamera cam;
	CameraInputController camController;
	ModelBatch modelBatch;
	Array<ModelInstance> instances;
	Environment environment;
	Model model;
	ModelInstance ground;
	ModelInstance ball;
	boolean collision;

	btCollisionShape groundShape;
	btCollisionShape ballShape;

	btCollisionObject groundObject;
	btCollisionObject ballObject;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;

	@Override
	public void create () {
		Bullet.init();

		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(3f, 7f, 10f);
		cam.lookAt(0, 4f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ground";
		mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)))
			.box(5f, 1f, 5f);
		mb.node().id = "ball";
		mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
			.sphere(1f, 1f, 1f, 10, 10);
		model = mb.end();

		ground = new ModelInstance(model, "ground");
		ball = new ModelInstance(model, "ball");
		ball.transform.setToTranslation(0, 5f, 0);

		instances = new Array<ModelInstance>();
		instances.add(ground);
		instances.add(ball);

		groundShape = new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f));
		ballShape = new btSphereShape(0.5f);

		groundObject = new btCollisionObject();
		groundObject.setCollisionShape(groundShape);
		groundObject.setCollisionFlags(groundObject.getCollisionFlags()
			| btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		groundObject.setWorldTransform(ground.transform);

		ballObject = new btCollisionObject();
		ballObject.setCollisionShape(ballShape);
		ballObject.setCollisionFlags(ballObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		ballObject.setWorldTransform(ball.transform);

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
	}

	@Override
	public void render () {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		if (!collision) {
			ball.transform.translate(0f, -delta, 0f);
			ballObject.setWorldTransform(ball.transform);

			collision = checkCollision(ballObject, groundObject);
		}

		camController.update();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	boolean checkCollision (btCollisionObject obj0, btCollisionObject obj1) {
		CollisionObjectWrapper co0 = new CollisionObjectWrapper(obj0);
		CollisionObjectWrapper co1 = new CollisionObjectWrapper(obj1);

		btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(co0.wrapper, co1.wrapper);

		btDispatcherInfo info = new btDispatcherInfo();
		btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

		algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

		boolean r = result.getPersistentManifold().getNumContacts() > 0;

		dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());
		result.dispose();
		info.dispose();
		co1.dispose();
		co0.dispose();

		return r;
	}

	@Override
	public void dispose () {
		groundObject.dispose();
		groundShape.dispose();

		ballObject.dispose();
		ballShape.dispose();

		dispatcher.dispose();
		collisionConfig.dispose();

		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void resize (int width, int height) {
	}
}
