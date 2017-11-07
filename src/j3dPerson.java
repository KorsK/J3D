import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.function.Predicate;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.universe.ViewingPlatform;


public class j3dPerson{

	Robot r;
	private TransformGroup viewTrans;
	private Transform3D viewt3d;
	private Point3d playerCoords;
	private Point3d playerLookAt;
	private Vector3d playerUp;
	private Vector3d collisionCoords;
	private double mouseX, mouseY;
	private double viewCircleTheta, upCircleTheta, viewCircleLength;
	private double sensitivityX, sensitivityY;
	private ArrayList<Integer> keyArr = new ArrayList<Integer>();
	private keyRemove<Integer> filter = new keyRemove<Integer>();
	private boolean collision, jumping, alreadyJumped;
	private int yDim;
	private double dy, maxY;
	private ArrayList<ArrayList<ArrayList<Integer>>> Arr;
	
	private boolean robotActive,cameraActive;
	
	public j3dPerson(ViewingPlatform vP, int y, int Maptype,boolean robotActive,boolean cameraActive){
		
		dy = 0;
		maxY = 1.0;
		this.robotActive = robotActive;
		this.cameraActive = cameraActive;
		
		if(Maptype ==0){
			Arr = j3dLoadHills.getColl();
		}
		else{
			Arr = j3dLoadSet.getColl();
		}
		
		
		jumping = alreadyJumped = false;
		yDim = y;
		try {
			r = new Robot();
		} catch (AWTException e) {}
		
		sensitivityX=sensitivityY=20;
		
		playerCoords = new Point3d(10,100,10);
		playerLookAt = new Point3d(0,0,0);
		playerUp = new Vector3d(0,1,0);
		collisionCoords = new Vector3d();
		
		viewCircleTheta = 0;
		upCircleTheta = 0;
		viewCircleLength = Math.abs(playerCoords.getZ() - playerLookAt.getZ());
		
		viewTrans = vP.getViewPlatformTransform();
		viewt3d = new Transform3D();
	}

	public void update() {
		collisionCoords.set((int)(playerCoords.getX()+1)/2,(int) (playerCoords.getY()-2)/2, (int)(playerCoords.getZ()+1)/2);
		checkCollisions();
		YUpdate();
		if(cameraActive){
			viewUpdate();
		}
		
		KeyUpdate();
		j3dMain.setViewt3d(viewt3d);
		j3dMain.setViewTG(viewTrans);
	}
	
	public void checkCollisions(){
		try{
			if(Arr.get((int) collisionCoords.getZ()).get((int) (yDim-collisionCoords.getY())).get((int) collisionCoords.getX())!=0||
			Arr.get((int) collisionCoords.getZ()).get((int) (yDim-collisionCoords.getY()+1)).get((int) collisionCoords.getX())!=0){
				collision = true;
			}
			else{
				collision = false;
			}
		}catch(Exception e){
			collision = false;
		}
	}
	
	public void YUpdate(){
		
		if(jumping){
			dy=maxY;
			jumping=false;
			alreadyJumped = true;
			
		}
		else{	
			if(collision){
				dy=0;	
				alreadyJumped = false;
				
			}
			else{
				 if(dy>-maxY){
					 
						dy-=0.1; 
					 
					 
				 }
				 else{
					 dy=-maxY;
				 }
			}	
		}
		playerCoords.add(new Point3d(0,dy,0));
		
	}
	
	public void viewUpdate(){
		
		mouseY = MouseInfo.getPointerInfo().getLocation().y;
		mouseX = MouseInfo.getPointerInfo().getLocation().x;
		if(robotActive){
			r.mouseMove(200, 200);
		}

		double deltaX = (mouseX-200) / sensitivityX;
		double deltaY = (mouseY-200) / sensitivityY;
		
		if(Math.abs(viewCircleTheta)> 2*Math.PI){
			viewCircleTheta =0;
		}
		else{
			viewCircleTheta += (double) (Math.atan((deltaX/viewCircleLength)));
		}
		if(Math.abs(upCircleTheta)> Math.PI/2){
			if(upCircleTheta>0){
				upCircleTheta =(float) (Math.PI/2);
			}
			else if(upCircleTheta<0){
				upCircleTheta = (float) (-Math.PI/2);
			}
		}
		
		upCircleTheta -=  (double) (Math.asin((deltaY/viewCircleLength)));
		
		
		double ZViewMove = (viewCircleLength-(viewCircleLength*Math.cos(viewCircleTheta)))+(playerCoords.getZ()-viewCircleLength);
		double XViewMove = (viewCircleLength*Math.sin(viewCircleTheta))+playerCoords.getX();
		double YViewMove = (viewCircleLength*Math.sin(upCircleTheta))+playerCoords.getY();
		
		playerLookAt.set(XViewMove,YViewMove, ZViewMove);
		viewt3d.lookAt(playerCoords, playerLookAt, playerUp);
		viewt3d.invert();
		viewTrans.setTransform(viewt3d);
	}
	
	public Vector3d getCollPos(Vector3d vec1){
			
			int collX = (int) ((vec1.getX()+1)/2);
			int collY = (int) ((vec1.getY()-2)/2);
			int collZ = (int) ((vec1.getZ()+1)/2);
			Vector3d vec2 = new Vector3d(new Point3d(collX,collY,collZ));
			return vec2;
		}
		
	
	public void movePerson(int dim, double scale, int yDims){
		boolean checkNext;
		switch(dim){
		case 1:
	
			Vector3d nextMovement = new Vector3d(playerCoords.getX()+Math.cos(viewCircleTheta)*scale*3,playerCoords.getY(),
			playerCoords.getZ()+Math.sin(viewCircleTheta)*scale*3);
			Vector3d collNext = getCollPos(nextMovement);
			
			
		
			try{
				
				 if(Arr.get((int) collNext.getZ()).get((int) ((yDims-1)-collNext.getY())).get((int) collNext.getX())!=0){
			    	checkNext = false;
			    }
			    else{
			    	checkNext = true;
			    }	
			}
			catch(IndexOutOfBoundsException e){
				checkNext = true;
			}
	
			
			if(checkNext){
			
				
				playerCoords.add(new Point3d(Math.cos((viewCircleTheta))*scale,0,Math.sin((viewCircleTheta))*scale));
				viewt3d.lookAt(playerCoords, playerLookAt, playerUp);
				viewt3d.invert();
				viewt3d.setTranslation(new Vector3d(playerCoords.getX(),playerCoords.getY(),playerCoords.getZ()));
				viewTrans.setTransform(viewt3d);
				
			}
			break;
		
		case 3:
			
			Vector3d nextMovement2 = new Vector3d(playerCoords.getX()-Math.sin(viewCircleTheta)*scale*3,playerCoords.getY(),
					playerCoords.getZ()+Math.cos(viewCircleTheta)*scale*3);
					Vector3d collNext2 = getCollPos(nextMovement2);
					
					
				
					try{
						
						 if(Arr.get((int) collNext2.getZ()).get((int) ((yDims-1)-collNext2.getY())).get((int) collNext2.getX())!=0){
					    	checkNext = false;
					    }
					    else{
					    	checkNext = true;
					    }	
					}
					catch(IndexOutOfBoundsException e){
						checkNext = true;
					}
			
					
					if(checkNext){
					
						
						playerCoords.add(new Point3d(-Math.sin((viewCircleTheta))*scale,0,Math.cos((viewCircleTheta))*scale));
						viewt3d.lookAt(playerCoords, playerLookAt, playerUp);
						viewt3d.invert();
						viewt3d.setTranslation(new Vector3d(playerCoords.getX(),playerCoords.getY(),playerCoords.getZ()));
						viewTrans.setTransform(viewt3d);
						
					}
			break;
			}
		
	}
	
	public ArrayList<ArrayList<ArrayList<Integer>>> getCollisionArr(){return Arr;}
	
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(!keyArr.contains(k)){
			keyArr.add(k);
		}	
	}
	
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		filter.var1 = k;
		keyArr.removeIf(filter);
	}
	
	public class keyRemove<T> implements Predicate<T>{

		T var1;
		
		@Override
		public boolean test(T var) {
			if(var1.equals(var)){
				return true;
			}
			else{
				return false;
			}	
		}
	}

	public void KeyUpdate(){
		
		if(!(keyArr.size() ==0)){
			for(int i = 0; i < keyArr.size();i++){
				if(keyArr.get(i) == KeyEvent.VK_W){
					if(keyArr.contains(KeyEvent.VK_SHIFT)){
						movePerson(3,-1.4, yDim);
					}
					else{
						movePerson(3,-0.4, yDim);
					}
				}
				if(keyArr.get(i) == KeyEvent.VK_A){
					
					if(keyArr.contains(KeyEvent.VK_SHIFT)){
						movePerson(1,-1.4, yDim);
					}
					else{
						movePerson(1,-0.4, yDim);
					}
					
				}
				if(keyArr.get(i) == KeyEvent.VK_S){
					if(keyArr.contains(KeyEvent.VK_SHIFT)){
						movePerson(3,1.4, yDim);
					}
					else{
						movePerson(3,0.4, yDim);
					}
					
				}
				if(keyArr.get(i) == KeyEvent.VK_D){
					if(keyArr.contains(KeyEvent.VK_SHIFT)){
						movePerson(1,1.4, yDim);
					}
					else{
						movePerson(1,0.4, yDim);
					}
				}
				if(keyArr.get(i) == KeyEvent.VK_SPACE){
					
					if(!alreadyJumped){
						jumping = true;
					}
				}
			}
		}
	}	
}

