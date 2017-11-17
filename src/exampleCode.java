



import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;



public class exampleCode{

	//defines variables
	SimpleUniverse universe;
	BranchGroup group;
	Box b;
	Transform3D cubeTransform ;
	TransformGroup tg;
	
	public exampleCode(){
		
		//initializes variables
		universe = new SimpleUniverse();
		group = new BranchGroup();
		
		
		//start with a bufferedImage of our texture
		
		BufferedImage bi = null;
		try{
			bi = ImageIO.read(new File("res/stars.jpg"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//a TextureLoader loads the bufferedImage as a texture, and 
		//a Texture gets the Texture of the TextureLoader
		
		TextureLoader star = new TextureLoader(bi);
		Texture starTex = star.getTexture();
		
		//TextureAttributes defines how the Texture will be loaded in
		//examples: .REPLACE replaces the texture of the box with the 
		//current texture, .BLEND blends the two textures.
		
		TextureAttributes starAtt = new TextureAttributes();
		starAtt.setTextureMode(TextureAttributes.MODULATE);
	
		//how our box will appear
		
		Appearance starApp = new Appearance();
		Color3f blue = new Color3f(0.1f,0.07f,0.6f);
		
		//the material is how the box will appear with no textures (blue)
		// as defined earlier, the texture will modulate with
		//the current texture of the box (blue)
		
		starApp.setMaterial(new Material(blue,blue,blue,blue,1f));
		starApp.setTexture(starTex);
		starApp.setTextureAttributes(starAtt);
		
		//dunno what this does to be honest but is needed for the texture to work orz
		
		 int primflags = Primitive.GENERATE_NORMALS +

				   Primitive.GENERATE_TEXTURE_COORDS;
		
		 //defines our box as 1x1x1 with the previously defined appearance and primflags
		 
		 b = new Box (1,1,1,primflags,starApp);
		 
		//creates the transform3d of the box
		 
		cubeTransform = new Transform3D();
		
		//translation defines how to move the object. This translation moves the object
		//in the x direction one unit in the positive direction (right)
		
		cubeTransform.setTranslation(new Vector3d(1,0,0));
		
		//rotates the object. vector3d defines axis of rotation and the number(45) 
		//defines how many degrees to rotate
		
		cubeTransform.setRotation(new AxisAngle4d(new Vector3d(1,1,0),45));
		
		//scales the object (makes object bigger or smaller) this scale doubles the size of the box
		
		cubeTransform.setScale(2d);
		
		//creates a light color with the defined rgb values
		
		Color3f light1Color = new Color3f(1.8f, 0.5f, 0.5f);
		
		//with lights, the boundingsphere defines when the light will be active.
		//the point3d is the origin of the sphere and the 100 is the radius of the sphere
		//this boundingsphere is a sphere of radius 100 units and centered at the origin
		
		BoundingSphere bounds =
		new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
		
		//which way the light will shine on the object. will always shine towards the origin
		//this particular light will shine from the bottom right towards the center.
		
		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, 0);
		
		//creates our directional light with the color and direction predefined.
		
		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		
		//if the viewPlatform is located within the predefined boundingsphere then this light will activate.
		
		light1.setInfluencingBounds(bounds);
		
		
		//create a transformGroup and sets the transform3d of the transformGroup.
		//the box is then added to the transformGroup and finally the transformGroup 
		//is added to the BranchGroup, which is added to the SimpleUniverse
		//(so much hierarchiness going on :V )
		
		tg = new TransformGroup();
		
		tg.setTransform(cubeTransform);
		tg.addChild(b);
		group.addChild(tg);
		group.addChild(light1);
		universe.addBranchGraph(group);
		
		
		//sets the Transform of the viewPlatform to be 10 units in the positive z direction
		//by default, the viewPlatform is always facing towards the origin
		
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(0,0,10));
		universe.getViewingPlatform().getViewPlatformTransform().setTransform(t3d);
		
	}
	
	
	
	public static void main(String[]args){
		@SuppressWarnings("unused")
		exampleCode j3d = new exampleCode();	
	}
}
