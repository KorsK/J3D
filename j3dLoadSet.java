import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;


public class j3dLoadSet {
	
	static ArrayList<ArrayList<ArrayList<Integer>>> collArr;
	
	public static void loadSet(int xDims, int yDims, int zDims,BranchGroup group) throws Exception {
		
		collArr = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		Color3f black = new Color3f(0.0f,0.0f,0.0f);
		Color3f red = new Color3f(0.9f,0.2f,0.2f);
		Color3f green = new Color3f(0.0f,0.5f,0.0f);
		
		j3dAppearance grassApp = new j3dAppearance(green,green,green,green,1f, "res/grassTex.png");
		j3dAppearance woodApp = new j3dAppearance(red,black,red,black,1f, "res/woodTex.png");
		j3dAppearance leavesApp = new j3dAppearance(green,green,green,green,1f, "res/leavesTex.png");
		
		FileReader fr;
	
		fr = new FileReader(new File("res/mapGrid"));
		BufferedReader br = new BufferedReader(fr);
		
		String s;
		ArrayList<String> setMapLines = new ArrayList<String>();
		while((s = br.readLine())!=null){
			setMapLines.add(s); 
		}
		br.close();
		
	
		for(int z = 0; z < zDims; z++){
			ArrayList<ArrayList<Integer>> setMapGrid = zero2dArr(xDims,yDims);
			for(int y = 0; y < yDims; y++){
				String st = setMapLines.get(0);
				for (int i = 0; i < st.length(); i++) {
					setMapGrid.get(y).set(i,Integer.parseInt(String.valueOf(st.charAt(i))));
					
				}
				setMapLines.remove(0);
			}
			collArr.add(setMapGrid);
			setMapLines.remove(0);
		}
		
		
		
		
		
		
		for(int z = 0; z < zDims; z++){
			for(int y = 0; y < yDims; y++){
				for(int x = 0; x < xDims; x++){
					if(collArr.get(z).get((yDims-1)-y).get(x)==1){
						
						Box b = new Box(1,1,1,grassApp.getFlags(),grassApp.getApp());
						
						Transform3D boxTrans = new Transform3D();
						boxTrans.setTranslation(new Vector3d(x*2,y*2,z*2));
						
						TransformGroup boxTG = new TransformGroup();
						boxTG.setTransform(boxTrans);
						boxTG.addChild(b);
						
						group.addChild(boxTG);
					}
					else if(collArr.get(z).get((yDims-1)-y).get(x)==2){
						
						Box b = new Box(1,1,1,woodApp.getFlags(),woodApp.getApp());
						
						Transform3D boxTrans = new Transform3D();
						boxTrans.setTranslation(new Vector3d(x*2,y*2,z*2));
						
						TransformGroup boxTG = new TransformGroup();
						boxTG.setTransform(boxTrans);
						boxTG.addChild(b);
						
						group.addChild(boxTG);
					}
					else if(collArr.get(z).get((yDims-1)-y).get(x)==3){
						
						Box b = new Box(1,1,1,leavesApp.getFlags(),leavesApp.getApp());
						
						Transform3D boxTrans = new Transform3D();
						boxTrans.setTranslation(new Vector3d(x*2,y*2,z*2));
						
						TransformGroup boxTG = new TransformGroup();
						boxTG.setTransform(boxTrans);
						boxTG.addChild(b);
						
						group.addChild(boxTG);
					}
				}
			}
		}
	}
	
//--------------------------------------------------------------------------------------------
	public static ArrayList<ArrayList<ArrayList<Integer>>> getColl(){return collArr;}
	
	//--------------------------------------------------------------------------------------------	
	public static ArrayList<ArrayList<Integer>> zero2dArr(int xDim, int yDim){
		
		ArrayList<ArrayList<Integer>>endArr2d = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < yDim; i++){
			ArrayList<Integer> holdArr = new ArrayList<Integer>();
			for(int j = 0; j < xDim; j++){
				int num = 0;
				holdArr.add(num);
			}
			endArr2d.add(holdArr);
			
		}
		return endArr2d;
	}
	
}
