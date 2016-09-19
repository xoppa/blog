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

package com.xoppa.blog.libgdx;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

public class Main extends JFrame {
	private static final long serialVersionUID = -4296204662394260962L;

	public static String data;
	
	public static class AppDesc {
		public Class<? extends ApplicationListener> clazz;
		public String title;
		
		public int width;
		public int height;
		public String data;
		
		public AppDesc(String title, int width, int height, Class<? extends ApplicationListener> clazz, String data) {
			this.clazz = clazz;
			this.title = title;
			this.width = width;
			this.height = height;
			this.data = data;
		}
		
		public AppDesc(String title, int width, int height, Class<? extends ApplicationListener> clazz) {
			this(title, width, height, clazz, null);
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	public final static Object[] apps = {
		"Tutorials",
		new Object[] {
				"Basic 3D using libgdx",
				new AppDesc("step 1: render a cube", 640, 480, com.xoppa.blog.libgdx.g3d.basic3d.step1.Basic3DTest.class),
				new AppDesc("step 2: lights", 640, 480, com.xoppa.blog.libgdx.g3d.basic3d.step2.Basic3DTest.class),
				new AppDesc("step 3: camera controller", 640, 480, com.xoppa.blog.libgdx.g3d.basic3d.step3.Basic3DTest.class)			
		},
		new Object[] {
				"Load models using libgdx",
				new AppDesc("step 1: load a wavefrom model", 640, 480, com.xoppa.blog.libgdx.g3d.loadmodels.step1.LoadModelsTest.class, "loadmodels/data"),
				new AppDesc("step 2: use assetmanager", 640, 480, com.xoppa.blog.libgdx.g3d.loadmodels.step2.LoadModelsTest.class, "loadmodels/data"),
				new AppDesc("step 3: multiple instances", 640, 480, com.xoppa.blog.libgdx.g3d.loadmodels.step3.LoadModelsTest.class, "loadmodels/data"),
				new AppDesc("step 4: use fbx-conv", 640, 480, com.xoppa.blog.libgdx.g3d.loadmodels.step4.LoadModelsTest.class, "loadmodels/data")
		},
		new Object[] {
				"Loading a scene using libgdx",
				new AppDesc("step 1: coding a scene", 640, 480, com.xoppa.blog.libgdx.g3d.loadscene.step1.LoadSceneTest.class, "loadscene/data"),
				new AppDesc("step 2: combining models", 640, 480, com.xoppa.blog.libgdx.g3d.loadscene.step2.LoadSceneTest.class, "loadscene/data"),
				new AppDesc("step 3: loading a modeled scene", 640, 480, com.xoppa.blog.libgdx.g3d.loadscene.step3.LoadSceneTest.class, "loadscene/data")
		},
		new Object[] {
				"Behind the 3D scenes",
				new AppDesc("step 1: base code", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step1.BehindTheScenesTest.class, "behindscenes/data"),
				new AppDesc("step 2: using ModelLoader", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step2.BehindTheScenesTest.class, "behindscenes/data"),
				new AppDesc("step 3: change material by NodePart", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step3.BehindTheScenesTest.class, "behindscenes/data"),
				new AppDesc("step 4: change material by name", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step4.BehindTheScenesTest.class, "behindscenes/data"),
				new AppDesc("step 5: change material per ModelInstance", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step5.BehindTheScenesTest.class, "behindscenes/data"),
				new AppDesc("step 6: using a Renderable", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step6.BehindTheScenesTest.class, "behindscenes/data"),
				new AppDesc("step 7: using a Shader", 640, 480, com.xoppa.blog.libgdx.g3d.behindscenes.step7.BehindTheScenesTest.class, "behindscenes/data")
		},
		new Object[] {
				"Creating a shader with libgdx",
				new AppDesc("step 1: render a sphere", 640, 480, com.xoppa.blog.libgdx.g3d.createshader.step1.ShaderTest.class, "createshader/data"),
				new AppDesc("step 2: render points", 640, 480, com.xoppa.blog.libgdx.g3d.createshader.step2.ShaderTest.class, "createshader/data"),
				new AppDesc("step 3: customize default shader", 640, 480, com.xoppa.blog.libgdx.g3d.createshader.step3.ShaderTest.class, "createshader/data"),
				new AppDesc("step 4: implement shader", 640, 480, com.xoppa.blog.libgdx.g3d.createshader.step4.ShaderTest.class, "createshader/data"),
				new AppDesc("step 5: enable depth test", 640, 480, com.xoppa.blog.libgdx.g3d.createshader.step5.ShaderTest.class, "createshader/data"),
				new AppDesc("step 6: cache uniform locations", 640, 480, com.xoppa.blog.libgdx.g3d.createshader.step6.ShaderTest.class, "createshader/data")
		},
		new Object[] {
				"Using materials with libgdx",
				new AppDesc("step 1: using modelbatch", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step1.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 2: add a uniform", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step2.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 3: using userData", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step3.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 4: using ColorAttribute", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step4.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 5: implement canRender", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step5.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 6: add another color", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step6.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 7: use custom attribute", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step7.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 8: update canRender", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step8.MaterialTest.class, "usingmaterials/data"),
				new AppDesc("step 9: create custom attribute", 640, 480, com.xoppa.blog.libgdx.g3d.usingmaterials.step9.MaterialTest.class, "usingmaterials/data")
		},
		new Object[] {
				"3D Frustum culling",
				new AppDesc("step 1: no frustum culling", 640, 480, com.xoppa.blog.libgdx.g3d.frustumculling.step1.FrustumCullingTest.class, "loadscene/data"),
				new AppDesc("step 2: position culling", 640, 480, com.xoppa.blog.libgdx.g3d.frustumculling.step2.FrustumCullingTest.class, "loadscene/data"),
				new AppDesc("step 3: bounds culling", 640, 480, com.xoppa.blog.libgdx.g3d.frustumculling.step3.FrustumCullingTest.class, "loadscene/data"),
				new AppDesc("step 4: sphere culling", 640, 480, com.xoppa.blog.libgdx.g3d.frustumculling.step4.FrustumCullingTest.class, "loadscene/data")
		},
		new Object[] {
				"Ray picking",
				new AppDesc("step 1: selecting objects", 640, 480, com.xoppa.blog.libgdx.g3d.raypicking.step1.RayPickingTest.class, "loadscene/data"),
				new AppDesc("step 2: moving objects", 640, 480, com.xoppa.blog.libgdx.g3d.raypicking.step2.RayPickingTest.class, "loadscene/data"),
				new AppDesc("step 3: preciser selecting objects", 640, 480, com.xoppa.blog.libgdx.g3d.raypicking.step3.RayPickingTest.class, "loadscene/data")
		},
		new Object[] {
			"Shapes",
			new AppDesc("step 1: move code to GameObject", 640, 480, com.xoppa.blog.libgdx.g3d.shapes.step1.ShapeTest.class, "loadscene/data"),
			new AppDesc("step 2: Sphere shape", 640, 480, com.xoppa.blog.libgdx.g3d.shapes.step2.ShapeTest.class, "loadscene/data"),
			new AppDesc("step 3: Box shape", 640, 480, com.xoppa.blog.libgdx.g3d.shapes.step3.ShapeTest.class, "loadscene/data"),
			new AppDesc("step 4: Disc shape", 640, 480, com.xoppa.blog.libgdx.g3d.shapes.step4.ShapeTest.class, "loadscene/data")
		},
		new Object[] {
			"Bullet: Collision detection",
			new AppDesc("step 1: no collision detection", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step1.BulletTest.class),
			new AppDesc("step 2: using a collision algorithm", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step2.BulletTest.class),
			new AppDesc("step 3: using a collision dispatcher", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step3.BulletTest.class),
			new AppDesc("step 4: add more objects", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step4.BulletTest.class),
			new AppDesc("step 5: using a ContactListener", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step5.BulletTest.class),
			new AppDesc("step 6: optimize the callback method", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step6.BulletTest.class),
			new AppDesc("step 7: using a collision world", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step7.BulletTest.class),
			new AppDesc("step 8: using collision filtering", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.collision.step8.BulletTest.class)
		},
		new Object[] {
			"Bullet: Dynamics",
			new AppDesc("step 1: add physics properties", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.dynamics.step1.BulletTest.class),
			new AppDesc("step 2: add a dynamics world", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.dynamics.step2.BulletTest.class),
			new AppDesc("step 3: color objects on the ground", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.dynamics.step3.BulletTest.class),
			new AppDesc("step 4: add motion state", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.dynamics.step4.BulletTest.class),
			new AppDesc("step 5: contact callback filtering", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.dynamics.step5.BulletTest.class),
			new AppDesc("step 6: kinematic body", 640, 480, com.xoppa.blog.libgdx.g3d.bullet.dynamics.step6.BulletTest.class)
		},
		new Object[] {
				"A simple card game",
				new AppDesc("step 1: initial setup", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step1.CardGame.class, "cardgame/data"),
				new AppDesc("step 2: use meaningfull units", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step2.CardGame.class, "cardgame/data"),
				new AppDesc("step 3: structure the code", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step3.CardGame.class, "cardgame/data"),
				new AppDesc("step 4: controlling the camera", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step4.CardGame.class, "cardgame/data"),
				new AppDesc("step 5: add perspective", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step5.CardGame.class, "cardgame/data"),
				new AppDesc("step 6: switch to ModelBatch", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step6.CardGame.class, "cardgame/data"),
				new AppDesc("step 7: reduce render calls", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step7.CardGame.class, "cardgame/data"),
				new AppDesc("step 8: add a table", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step8.CardGame.class, "cardgame/data"),
				new AppDesc("step 9: keep the cards on the table", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.step9.CardGame.class, "cardgame/data"),
				new AppDesc("step A: animating the cards", 640, 480, com.xoppa.blog.libgdx.g3d.cardgame.stepa.CardGame.class, "cardgame/data")
		}
	};
	
	LwjglAWTCanvas currentTest = null;
	public boolean runApp(final AppDesc appDesc) {
		ApplicationListener listener;
		try {
			listener = appDesc.clazz.newInstance();
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		data = (appDesc.data == null || appDesc.data.isEmpty()) ? "data" : appDesc.data;
		
		Container container = getContentPane();
		if (currentTest != null) {
			currentTest.stop();	
			container.remove(currentTest.getCanvas());
		}
		
		currentTest = new LwjglAWTCanvas(listener);
		currentTest.getCanvas().setSize(appDesc.width, appDesc.height);
		container.add(currentTest.getCanvas(), BorderLayout.CENTER);
		pack();
		return true;
	}
	
	public static void main(String[] args) throws Throwable {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new Main();
	}
	
	public Main() throws HeadlessException {
		super("Xoppa Libgdx Tutorials");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = getContentPane();
		JPanel appList = new AppList();
		appList.setSize(250, 600);
		container.add(appList, BorderLayout.LINE_START);
		pack();
		setSize(900, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (currentTest != null)
					currentTest.exit();
			}
		});
	}
	
	class AppList extends JPanel {
		private static final long serialVersionUID = 1582559224991888475L;

		public AppList () {
			setLayout(new BorderLayout());

			final JButton button = new JButton("Run Test");

			DefaultMutableTreeNode root = processHierarchy(apps);
			final JTree tree = new JTree(root);
			JScrollPane pane = new JScrollPane(tree);

			DefaultTreeSelectionModel m = new DefaultTreeSelectionModel();
			m.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.setSelectionModel(m);

			tree.addMouseListener(new MouseAdapter() {
				public void mouseClicked (MouseEvent event) {
					if (event.getClickCount() == 2) button.doClick();
				}
			});

			tree.addKeyListener(new KeyAdapter() {
				public void keyPressed (KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) button.doClick();
				}
			});

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed (ActionEvent e) {
					Object obj = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject();
					if (obj instanceof AppDesc) {
						AppDesc app = (AppDesc)obj;
						//dispose();
						runApp(app);
					}
				}
			});

			add(pane, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
		}
		
		private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
			DefaultMutableTreeNode child;
			for(int i=1; i<hierarchy.length; i++) {
				Object nodeSpecifier = hierarchy[i];
				if (nodeSpecifier instanceof Object[])
					child = processHierarchy((Object[])nodeSpecifier);
				else
					child = new DefaultMutableTreeNode(nodeSpecifier);
				node.add(child);
			}
			return node;
		}
	}
}
