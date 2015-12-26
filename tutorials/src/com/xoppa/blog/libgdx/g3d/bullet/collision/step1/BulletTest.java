
package com.xoppa.blog.libgdx.g3d.bullet.collision.step1;

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

	@Override
	public void create () {
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(3f, 7f, 10f);
		cam.lookAt(0, 4f, 0);
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
		ball.transform.setToTranslation(0, 9f, 0);

		instances = new Array<ModelInstance>();
		instances.add(ground);
		instances.add(ball);
	}

	@Override
	public void render () {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		if (!collision) {
			ball.transform.translate(0f, -delta, 0f);
			collision = checkCollision();
		}

		camController.update();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	boolean checkCollision () {
		return false;
	}

	@Override
	public void dispose () {
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
