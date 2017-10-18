

import java.io.IOException;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import org.apache.commons.math3.distribution.NormalDistribution;
import com.sun.j3d.utils.geometry.Box;


public class j3dLoadHills {
	
	static ArrayList<ArrayList<ArrayList<Integer>>> collArr;
	
	public  static void loadHills(int xDim, int yDim, int zDim, int Hills, double HeightMean, double HeightDeviation, double SlopeMean,
			double SlopeDeviation, double RadMean, double RadDeviation, 
			double ovalMean, double ovalDeviation,double deltaSLMean, double deltaSLDeviation, BranchGroup group) throws IOException{
		
		collArr = new ArrayList<ArrayList<ArrayList<Integer>>>();

		NormalDistribution heightDist =new NormalDistribution(HeightMean,HeightDeviation);
		NormalDistribution slopeDist =new NormalDistribution(SlopeMean,SlopeDeviation);
		NormalDistribution radDist = new NormalDistribution(RadMean,RadDeviation);
		//NormalDistribution deltaSLDist = new NormalDistribution(deltaSLMean, deltaSLDeviation);
		//NormalDistribution ovalDist = new NormalDistribution(ovalMean, ovalDeviation);
		
		Color3f green = new Color3f(0f,0.5f,0f);
		
		j3dAppearance grassApp = new j3dAppearance(green,green,green,green,1f, "res/grassTex.png");
		
		
		int hNum = Hills;
		ArrayList <ArrayList<Integer>> mapGrid = new ArrayList<ArrayList<Integer>>();
		for(int i =0; i < zDim;i++){
			ArrayList <Integer> hold = new ArrayList<Integer>();
			for(int j = 0; j <xDim; j++){
				hold.add(0);
			}
			mapGrid.add(hold);
		}
		
		
		
		collArr = zero3dArr(xDim,yDim,zDim);
		
		
		
		for(int i =0; i<hNum;i++){
			int XPos = (int) (Math.random()*xDim);
			
			int ZPos = (int)(Math.random()*zDim);
			
			int Height =  (int) heightDist.inverseCumulativeProbability(Math.random());
			
			int Radius = (int) radDist.inverseCumulativeProbability(Math.random());
			
			if(Radius%2==0){
				Radius-=1;
			}
			
			if(Radius <=0){
				Radius = 1;
			}
			
			
			for(int j = (int) -(Math.floor(Radius/2)); j < (Math.ceil(Radius/2));j++){
				
				for(int k = (int) -(Math.floor(Radius/2)); k < (Math.ceil(Radius/2));k++){
					
					try{
						
						if(mapGrid.get(ZPos+k).get(XPos+j)<Height){
							mapGrid.get(ZPos+k).set(XPos+j, Height);
						}
						
					}catch(Exception e){
						
					}
					
					
				}
				
			}
			
			double Slope =  slopeDist.inverseCumulativeProbability(Math.random());
			
			if(Slope<=0){
				Slope=.1;
			}
			
			int hillStretch = (int) Math.floor(Height/Slope);
			
			
			for(int j = 0; j < hillStretch; j++){
				
				int newRadius = Radius +j;
				
				
				for(int l = (int) -(Math.floor(newRadius/2)); l < (Math.ceil(newRadius/2));l++){
					
					for(int k = (int) -(Math.floor(newRadius/2)); k < (Math.ceil(newRadius/2));k++){
						
						try{
							
							if(mapGrid.get(ZPos+k).get(XPos+l)<=(int)Math.round((Height-(j*Slope)))){
								mapGrid.get(ZPos+k).set(XPos+l, (int) Math.round((Height-(j*Slope))));
							}
							
						}catch(Exception e){
							
						}
					
					}
					
				}
				
			}
			
		//	int Ovality = (int) ovalDist.inverseCumulativeProbability(Math.random());
			
		//	int deltaSlope = (int) deltaSLDist.inverseCumulativeProbability(Math.random());
			
			
			
			mapGrid.get(ZPos).set(XPos, Height);	
		}
		
		
		
		
		int Tcount = 0;
		for(int i = 0 ; i < mapGrid.size();i++){
			Tcount = 0;
			for(int h:mapGrid.get(i)){
				for(int j = (h+1); j>0; j--){
					collArr.get(i).get(yDim-(j)).set(Tcount, 1);
				}
				Tcount++;
				
			}
		}
		
		
		
		for(int x = 0; x < xDim; x++){
			for(int y = 0; y < yDim; y++){
				for(int z = 0; z < zDim; z++){
					
					if(collArr.get(z).get(y).get(x)==1){
						Box b = new Box(1,1,1,grassApp.getFlags(),grassApp.getApp());
				
						Transform3D boxTrans = new Transform3D();
						boxTrans.setTranslation(new Vector3d(x*2,2*(yDim-y),2*z));
						
						TransformGroup boxTG = new TransformGroup();
						boxTG.setTransform(boxTrans);
						boxTG.addChild(b);
						
						group.addChild(boxTG);
					}
				}
			}
		}	
	}
	
	//-------------------------------------------------------------------------------------------------------------
	public static ArrayList<ArrayList<ArrayList<Integer>>> getColl(){return collArr;}
	
//-------------------------------------------------------------------------------------------------------	
	public static ArrayList<ArrayList<ArrayList<Integer>>> zero3dArr(int xDim, int yDim, int zDim){
		ArrayList<ArrayList<ArrayList<Integer>>>endArr3d = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for(int i = 0; i < zDim; i++){
			ArrayList<ArrayList<Integer>> holdArr = new ArrayList<ArrayList<Integer>>();
			for(int j = 0; j < yDim; j++){
				ArrayList<Integer> holdArr2 = new ArrayList<Integer>();
				for(int k = 0; k < xDim; k++){
					holdArr2.add(0);
				}
				holdArr.add(holdArr2);
			}
			endArr3d.add(holdArr);
			
		}
		return endArr3d;
	}
}





	
	
	
