

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;

public class j3dAppearance {

	Appearance app;
	TextureLoader loader;
	Texture texture;
	int primflags;
	
	public j3dAppearance(Color3f col1, Color3f col2, Color3f col3, Color3f col4, float shiny, String file) throws IOException{
		app = new Appearance();
		BufferedImage br = ImageIO.read(new File(file));
		loader = new TextureLoader(br);
		texture = loader.getTexture();
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
		
		app.setMaterial(new Material(col1,col2,col3,col4,shiny));
		app.setTexture(texture);
		app.setTextureAttributes(texAttr);
		
	}
	
	public Appearance getApp(){
		return app;
	}
	
	public int getFlags(){
		return primflags;
	}
	
	
}
