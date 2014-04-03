/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.xoppa.blog.libgdx.g3d.usingmaterials.step4;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

/**
 * See: http://blog.xoppa.com/using-materials-with-libgdx
 * @author Xoppa
 */
public class MaterialTest implements ApplicationListener {
	public PerspectiveCamera cam;
    public CameraInputController camController;
    public Shader shader;
    public Model model;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch;
     
    @Override
    public void create () {
    	cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	cam.position.set(0f, 8f, 8f);
    	cam.lookAt(0,0,0);
    	cam.near = 1f;
    	cam.far = 300f;
    	cam.update();

    	camController = new CameraInputController(cam);
    	Gdx.input.setInputProcessor(camController);

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createSphere(2f, 2f, 2f, 20, 20,
        		new Material(),
        		Usage.Position | Usage.Normal | Usage.TextureCoordinates);
         
        for (int x = -5; x <= 5; x+=2) {
        	for (int z = -5; z<=5; z+=2) {
        		ModelInstance instance = new ModelInstance(model, x, 0, z);
        		ColorAttribute attr = ColorAttribute.createDiffuse((x+5f)/10f, (z+5f)/10f, 0, 1);
        		instance.materials.get(0).set(attr);
        		instances.add(instance);
        	}
        }

        shader = new TestShader();
        shader.init();

        modelBatch = new ModelBatch();
    }
     
    @Override
    public void render () {
        camController.update();
         
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(cam);
        for (ModelInstance instance : instances)
        	modelBatch.render(instance, shader);
        modelBatch.end();
    }
     
    @Override
    public void dispose () {
        shader.dispose();
        model.dispose();
        modelBatch.dispose();
    }

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}