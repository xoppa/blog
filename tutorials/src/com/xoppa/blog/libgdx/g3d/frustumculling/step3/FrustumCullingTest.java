package com.xoppa.blog.libgdx.g3d.frustumculling.step3;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

/** See: http://blog.xoppa.com/3d-frustum-culling-with-libgdx
 * @author Xoppa */
public class FrustumCullingTest implements ApplicationListener {
	public static class GameObject extends ModelInstance {
		public final Vector3 center = new Vector3();
		public final Vector3 dimensions = new Vector3();
		
		private final static BoundingBox bounds = new BoundingBox();
		
		public GameObject(Model model, String rootNode, boolean mergeTransform) {
			super(model, rootNode, mergeTransform);
			calculateBoundingBox(bounds);
			bounds.getCenter(center);
			bounds.getDimensions(dimensions);
		}
	}
	
	protected PerspectiveCamera cam;
	protected CameraInputController camController;
	protected ModelBatch modelBatch;
	protected AssetManager assets;
	protected Array<GameObject> instances = new Array<GameObject>();
	protected Environment environment;
	protected boolean loading;
     
	protected Array<GameObject> blocks = new Array<GameObject>();
	protected Array<GameObject> invaders = new Array<GameObject>();
	protected ModelInstance ship;
	protected ModelInstance space;
    
	protected Stage stage;
	protected Label label;
	protected BitmapFont font;
	protected StringBuilder stringBuilder;
     
    @Override
    public void create () {
		stage = new Stage();
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();
    	
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
         
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 7f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
 
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
         
        assets = new AssetManager();
        assets.load(data+"/invaderscene.g3db", Model.class);
        loading = true;
    }
 
    private void doneLoading() {
        Model model = assets.get(data+"/invaderscene.g3db", Model.class);
        for (int i = 0; i < model.nodes.size; i++) {
            String id = model.nodes.get(i).id;
            GameObject instance = new GameObject(model, id, true);

            if (id.equals("space")) {
                space = instance;
                continue;
            }

            instances.add(instance);

            if (id.equals("ship"))
                ship = instance;
            else if (id.startsWith("block"))
                blocks.add(instance);
            else if (id.startsWith("invader"))
                invaders.add(instance);
        }
     
        loading = false;
    }
     
    private int visibleCount;
    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
        camController.update();
         
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(cam);
        visibleCount = 0;
        for (final GameObject instance : instances) {
        	if (isVisible(cam, instance)) {
        		modelBatch.render(instance, environment);
        		visibleCount++;
        	}
        }
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
        
        stringBuilder.setLength(0);
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        label.setText(stringBuilder);
        stage.draw();
    }
    
    private Vector3 position = new Vector3();
    protected boolean isVisible(final Camera cam, final GameObject instance) {
    	instance.transform.getTranslation(position);
    	position.add(instance.center);
    	return cam.frustum.boundsInFrustum(position, instance.dimensions);
    }
     
    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}