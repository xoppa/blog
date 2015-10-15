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

package com.xoppa.blog.libgdx.g3d.usingmaterials.step9;

import static com.xoppa.blog.libgdx.Main.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * See: http://blog.xoppa.com/creating-a-shader-with-libgdx
 * 
 * @author Xoppa
 */
public class TestShader implements Shader {
	public static class DoubleColorAttribute extends Attribute {
		public final static String DiffuseUVAlias = "diffuseUVColor";
		public final static long DiffuseUV = register(DiffuseUVAlias);

		public final Color color1 = new Color();
		public final Color color2 = new Color();

		protected DoubleColorAttribute (long type, Color c1, Color c2) {
			super(type);
			color1.set(c1);
			color2.set(c2);
		}

		@Override
		public Attribute copy () {
			return new DoubleColorAttribute(type, color1, color2);
		}

		@Override
		protected boolean equals (Attribute other) {
			DoubleColorAttribute attr = (DoubleColorAttribute) other;
			return type == other.type && color1.equals(attr.color1)
					&& color2.equals(attr.color2);
		}

		@Override
		public int compareTo (Attribute other) {
			if (type != other.type)
				return (int) (type - other.type);
			DoubleColorAttribute attr = (DoubleColorAttribute) other;
			return color1.equals(attr.color1)
					? attr.color2.toIntBits() - color2.toIntBits()
					: attr.color1.toIntBits() - color1.toIntBits();
		}
	}

	ShaderProgram program;
	Camera camera;
	RenderContext context;
	int u_projTrans;
	int u_worldTrans;
	int u_colorU;
	int u_colorV;

	@Override
	public void init() {
		String vert = Gdx.files.internal(data + "/uvcolor.vertex.glsl")
				.readString();
		String frag = Gdx.files.internal(data + "/uvcolor.fragment.glsl")
				.readString();
		program = new ShaderProgram(vert, frag);
		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());
		u_projTrans = program.getUniformLocation("u_projTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
		u_colorU = program.getUniformLocation("u_colorU");
		u_colorV = program.getUniformLocation("u_colorV");
	}

	@Override
	public void dispose() {
		program.dispose();
	}

	@Override
	public void begin(Camera camera, RenderContext context) {
		this.camera = camera;
		this.context = context;
		program.begin();
		program.setUniformMatrix(u_projTrans, camera.combined);
		context.setDepthTest(GL20.GL_LEQUAL);
		context.setCullFace(GL20.GL_BACK);
	}

	@Override
	public void render(Renderable renderable) {
		program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
		DoubleColorAttribute attribute = ((DoubleColorAttribute) renderable.material
				.get(DoubleColorAttribute.DiffuseUV));
		program.setUniformf(u_colorU, attribute.color1.r, attribute.color1.g,
				attribute.color1.b);
		program.setUniformf(u_colorV, attribute.color2.r, attribute.color2.g,
				attribute.color2.b);
		renderable.meshPart.render(program);
	}

	@Override
	public void end() {
		program.end();
	}

	@Override
	public int compareTo(Shader other) {
		return 0;
	}

	@Override
	public boolean canRender(Renderable renderable) {
		return renderable.material.has(DoubleColorAttribute.DiffuseUV);
	}
}
