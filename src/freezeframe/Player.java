package freezeframe;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.GRectangle;
import freezeframe.screens.FFWorldMap;
import starter.AudioPlayer;

//made changes to player class
public class Player extends PhysicsObject {
	private static GImage playerSprite = new GImage("wizzard_m_idle_anim_f0.png");
	private int counter = 0;

	private AudioPlayer play = AudioPlayer.getInstance();

	// ====================================================

	public Player(double startX, double startY) // added starting x and y coordinates
	{
		super(playerSprite, new Position(startX, startY));

		// save original Positions
		originalPosition = new Position(startX, startY);

	}
	// ====================================================

	public boolean getIsMoving() {
		return moving;
	}

	// ====================================================

	public void KeyPressed(KeyEvent e) {// switch through key presses
										// ESC to go back to world map
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Driver.getInstance().switchToScreen(new FFWorldMap());
			Driver.getInstance().resetCameraPos();
			play.stopSound("sounds", "POL-illusion-castle-short.wav");
			return;
		}
		switch (("" + e.getKeyChar()).toLowerCase().charAt(0)) {
		// compares key code values to find right key
		// ----------------------------------------------------
		case 'd': // when right key is pressed

			setPlayerState(PlayerState.RUNNING); // set playerstate to running

			if (isFrozen()) {
				return;
			}
			setFlipped(false); // images were originally facing right

			moving = true;
			airRight = true;
			moveRight();
			break;
		// ----------------------------------------------------
		case 'a': // when left key is pressed

			setPlayerState(PlayerState.RUNNING); // set playerstate to running

			if (isFrozen()) {
				return;
			}
			setFlipped(true); // nextImage() checks and flips images accordingly

			moving = true;
			airLeft = true;
			moveLeft();

			break;
		// ----------------------------------------------------
		case ' ': // when space key is pressed

			setPlayerState(PlayerState.RUNNING); // there is no JUMPING animation
			if (isFrozen()) {
				return;
			}
			if (inAir) // should not be able to press space while in air
			{
				return;
			}

			jump();
			break;
		// ----------------------------------------------------
		case 'p': // freeze & unfreeze

			// counter is initialized at zero, but actually starts at 1 when
			// the game is loaded

			// for some reason counter increments by 2 each time you press p
			if (counter >= 5)
				counter = 1;
			counter++;

			// if isFrozen is true and counter is between 4 and 5
			if (isFrozen() == true && counter <= 5 && counter >= 4) {
				unfreeze();
			}
			// if isFrozen is false and counter is less than or equal 3
			else if (isFrozen() == false && counter <= 3) {
				freeze();

				break;
			}
			// ----------------------------------------------------
		default:
			// not a valid key input

		}
	}
	// ====================================================

	public void KeyReleased(KeyEvent e) {// key Released
		switch (("" + e.getKeyChar()).toLowerCase().charAt(0)) {
		case 'd':
			// when right key is released
			moving = false;
			airRight = false;
			setPlayerState(PlayerState.STANDING); // when no keys are pressed, go back to idle
			break;
		case 'a':
			// left key is released
			moving = false;
			airLeft = false;
			setPlayerState(PlayerState.STANDING); // when no keys are pressed, go back to idle
			break;
		case ' ':
			if (inAir == true)
				setPlayerState(PlayerState.RUNNING);
		default:
			// nothing
		}

	}
	// ====================================================

	@Override
	public void updatePosition() {
		super.updatePosition();
		// x is update in ffGame
		updateY();
	}
}
