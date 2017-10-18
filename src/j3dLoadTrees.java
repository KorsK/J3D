import java.util.ArrayList;

import javax.media.j3d.BranchGroup;

import org.apache.commons.math3.distribution.NormalDistribution;

public class j3dLoadTrees {

	static ArrayList<ArrayList<ArrayList<Integer>>> collArr;
	
	public void loadTrees(int xDim, int yDim, int treeNum, int heightMean, 
			int heightDeviation, int areaMean, int areaDeviation, BranchGroup group){
		
		NormalDistribution heightDist = new NormalDistribution(heightMean, heightDeviation);
		NormalDistribution areaDist = new NormalDistribution(areaMean, areaDeviation);
		
		for(int i = 0; i < treeNum; i++){
			int randX = (int) (Math.random()*xDim);
			int randY = (int) (Math.random()*yDim);
			
		}
	}
	
}
