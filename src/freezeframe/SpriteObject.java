package freezeframe;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import acm.graphics.GImage;
import acm.graphics.GObject;
import starter.GraphicsPane;
import freezeframe.PlayerState;
import freezeframe.PhysicsObject;

public class SpriteObject {
	
	private GImage image;
	public Position pos;
	private int i = 0;
	public boolean flipped = false;
	public PlayerState state = PlayerState.STANDING;	//PlayerState initialized to STANDING
	//====================================================
			
	GImage	//PNG's for Animations
	
	//PLAYER CHARACTER
			//STANDING_IDLE
			id0 = new GImage("wizzard_m_idle_anim_f0.png"),
			id1 = new GImage("wizzard_m_idle_anim_f1.png"),
			id2 = new GImage("wizzard_m_idle_anim_f0.png"),
			id3 = new GImage("wizzard_m_idle_anim_f3.png"),
			
			//RUNNING
			rn0 = new GImage("wizzart_m_run_anim_f0.png"),
			rn1 = new GImage("wizzart_m_run_anim_f1.png"),
			rn2 = new GImage("wizzart_m_run_anim_f2.png"),
			rn3 = new GImage("wizzart_m_run_anim_f3.png"),
	
			//HIT
			ht0 = new GImage("wizzart_m_hit_anim_f0.png");
	//----------------------------------------------------		
	//ENEMIES
	
	//----------------------------------------------------		
	//OTHER

	//====================================================
	//ArrayList for different animations
	//Hit has ArrayList just for uniformity
	ArrayList<GImage> idleArr = new ArrayList<GImage>();
	ArrayList<GImage> runArr = new ArrayList<GImage>();
	ArrayList<GImage> hitArr = new ArrayList<GImage>();
	//====================================================
	public SpriteObject(Image image, Position pos) {
		this.image = new GImage(image);
		this.pos = pos;
	}
	//====================================================
	//super constructor
	public SpriteObject(GImage image, Position pos) {
		this.image = new GImage(image.getImage());
		image.getLocation();
		this.pos = pos;
	}
	//====================================================

	/**
	 * Creates a copy of another sprite object
	 * @param obj
	 */
	public SpriteObject(SpriteObject obj) {
		image = obj.getImage();
		pos = obj.getPos();
	}
	//====================================================

	//initializes Animation ArrayLists
	public void init() {
	//PLAYER CHARACTER
		//standing/idle && jumping/airborne
		idleArr.add(id0);
		idleArr.add(id1);
		idleArr.add(id2);
		idleArr.add(id3);
		//----------------------------------------------------		
		// moving left or right
		runArr.add(rn0);
		runArr.add(rn1);
		runArr.add(rn2);
		runArr.add(rn3);
		//----------------------------------------------------		
		//dies/touches an enemy
		hitArr.add(rn0);
		//----------------------------------------------------		
	//ENEMIES
		//----------------------------------------------------		
	//OTHER
		
	}
	//====================================================

	public void setPlayerState(PlayerState state) {
		this.state = state;
	}	
	//====================================================
	public PlayerState getPlayerState() {
		return state;
	}
	//====================================================

	//gets next image in array list
	public void nextImage(PlayerState state) {
		if (!PhysicsObject.isFrozen()) {
		i++;
		switch(state) {
		case STANDING:						//If STANDING load next idle image
			this.image = idleArr.get(i);
			checkFlipped();					//If player is flipped, flip the images
			
			if (i >= 3)						//reset i back to 0 when at end of idle ArrayList
				i = 0;
			break;
		case RUNNING:						//If RUNNING load next run image
			this.image = runArr.get(i);
			checkFlipped();					//If player is flipped, flip the images

			if (i >= 3)						//reset i back to 0 when at end of run ArrayList
				i = 0;
			break;
		case HIT:							//If Dies/Touch Enemy load next hit image
			this.image = hitArr.get(i);
			checkFlipped();					//If player is flipped, flip the images

			if (i >= 0)						//reset i back to 0 when at end of hit ArrayList
				i = 0;
			break;
		default:							//not a valid PlayerState
			break;
		}
		}
	}
	//====================================================
	public void checkFlipped() {			//If left key is pressed, flip it
		if (isFlipped() == true) {
			flipSprite();
		}
		else
			return;
	}
	//====================================================

	public void setImage(GImage image) {
		this.image = image;
	}
	//====================================================

	public GImage drawSprite() {
		Position cam = Driver.getInstance().getCameraPos();
		image.setLocation(pos.getX()-cam.getX(), pos.getY()-cam.getY());
		return image;
	}
	//====================================================

	public GImage getImage() {
		return image;
	}
	//====================================================

	//returns boolean flipped
	public boolean isFlipped() {
		return flipped;
	}
	//====================================================

	//sets boolean flipped
	public void setFlipped(boolean state) {
		this.flipped = state;
	}
	//====================================================

	public void flipSprite() {
		flipped = true;
		int[][] values = image.getPixelArray();
		int i, t, n = values[0].length;
		for(int r = 0; r < values.length; r++) {
			for (i = 0; i < n / 2; i++) { 
				t = values[r][i]; 
				values[r][i] = values[r][n - i - 1]; 
				values[r][n - i - 1] = t; 
			} 
		}
		GImage flipped = new GImage(values);
		flipped.setSize(50,75);
		image = flipped;
	}
	//====================================================

	public Position getPos() {
		return new Position(pos);
	}
	//====================================================

	public void setTint(Color c, double strength) {

	}
	//====================================================
	//this and some other things were commented out for my testing (ETHAN)
}
