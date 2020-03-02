package freezeframe;

import acm.graphics.GImage;
import freezeframe.screens.FFLevelCleared;
import freezeframe.screens.FFScreen;
import freezeframe.screens.FFSettingsMenu;
import starter.AudioPlayer;

//Do we make this an abstract class?

public class PhysicsObject extends SpriteObject {

	private static final double HORIZONTAL_CLAMP = .25;
	private static final double HORIZONTAL_DECELERATE = .05;
	private static final double HORIZONTAL_ACCELERATION = .6;
	private static final double HORIZONTAL_CAP = 2.4;
	private double xVelocity = 0;
	private double yVelocity = 0;
	private Position playerPosition;
	private double gravity = .25; // change later?
	private static volatile boolean frozen = false;
	private boolean canMoveRight;
	private boolean canMoveLeft;
	private boolean canMoveUp;
	private boolean canMoveDown;
	protected boolean moving = false;
	public boolean inAir = false;
	private final int distance = 3;
	private int playerLives = 3;
	protected Position originalPosition;
	protected boolean airLeft;
	protected boolean airRight;
	private AudioPlayer play = AudioPlayer.getInstance();
	private static boolean alive = true;
	private static boolean levelFinished = false;

	public PhysicsObject(GImage playerSprite, Position pos) {
		super(playerSprite, pos);

		playerPosition = pos;

	}

	public static void setLevelFinished(boolean state) {
		levelFinished = state;
	}

	public static boolean getLevelFinished() {
		return levelFinished;
	}

	public static void setAlive(boolean state) {
		alive = state;
	}

	public static boolean getAlive() {
		return alive;
	}

	public int getPlayerLives() {
		return playerLives;
	}

	public void decrementPlayerLives() {
		playerLives -= 1;
	}

	public void constructVelocity(double x, double y) {
		setxVelocity(0);
		setyVelocity(0);
	}

	/**
	 * @return the xVelocity
	 */
	public double getxVelocity() {
		return xVelocity;
	}

	/**
	 * @param xVelocity the xVelocity to set
	 */
	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	/**
	 * @return the yVelocity
	 */
	public double getyVelocity() {
		return yVelocity;
	}

	/**
	 * @param yVelocity the yVelocity to set
	 */
	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public void moveRight() {
		if (xVelocity >= HORIZONTAL_CAP || canMoveDown) {// max speed to right
			xVelocity = HORIZONTAL_CAP;
			return;
		}
		// else
		xVelocity += HORIZONTAL_ACCELERATION;

	}

	public void moveLeft() {
		if (xVelocity <= -HORIZONTAL_CAP || canMoveDown) {// max speed to left
			xVelocity = -HORIZONTAL_CAP;
			return;
		}
		// else
		xVelocity -= HORIZONTAL_ACCELERATION;
	}

	public void jump() {
		if (yVelocity != 0) {
			return;
		}

		if (!FFSettingsMenu.isSoundOff())
			play.playSound("sounds", "maro-jump-sound-effect_1.wav");

		yVelocity = -11;

	}

	public void stopVelocity() {// resets all velocity
		xVelocity = 0;
		yVelocity = 0;
		airLeft = false;
		airRight = false;
	}

	public void playerCollide(double dist) {
		// tells if we are going to Collide with something

		// current position
		double x = playerPosition.getX();
		double y = playerPosition.getY();

		for (double i = 0; i < getImage().getHeight() - dist; i++) {// checks entire length of height to make sure we
																	// dont phase into block while jumping or moving
			if (Driver.getInstance().getScreen().isColliding(x + getImage().getWidth() + dist, y + i)) {// if Sprite
																										// approaches
				// block on right side
				canMoveRight = false;

			} else {// can still move right
				canMoveRight = true;
			}

			if (Driver.getInstance().getScreen().isColliding(x - dist, y + i)) {
				canMoveLeft = false;

			} else {
				// can still move left
				canMoveLeft = true;

			}

		}
		if (Driver.getInstance().getScreen().isColliding(x, y)
				|| Driver.getInstance().getScreen().isColliding(x + getImage().getWidth(), y)) {
			// checks both ends if we are going to phase into block
			canMoveUp = false;

		} else {// can still move up
			canMoveUp = true;
		}

		if (Driver.getInstance().getScreen().isColliding(x, y + getImage().getHeight() + dist) || Driver.getInstance()
				.getScreen().isColliding(x + getImage().getWidth(), y + getImage().getHeight() + dist)) {
			// Checks for a Block under the Sprite
			// this is used incase we are on an edge of a platform so we dont phase into it
			// if
			// we are to go a little over on either side
			canMoveDown = false;

		} else {// can still move Down
			canMoveDown = true;
		}

	}

	public double getX() {
		return playerPosition.getX();
	}

	public double getY() {
		return playerPosition.getY();
	}

	public void freeze() {
		if (!FFSettingsMenu.isSoundOff())
			play.playSound("sounds", "Record_Scratch5.wav");
		frozen = true;

	}

	public void unfreeze() {
		play.stopSound("sounds", "Record_Scratch5.wav");
		frozen = false;
	}

	public static boolean isFrozen() {
		return frozen;
	}

	public void decreasexVelocity() {// decreases velocity if player is not moving
		if (xVelocity > 0) {// decrease until 0
			xVelocity -= HORIZONTAL_DECELERATE;
		}

		if (xVelocity < 0) {// increase until 0
			xVelocity += HORIZONTAL_DECELERATE;
		}

		if (xVelocity >= -HORIZONTAL_CLAMP && xVelocity <= HORIZONTAL_CLAMP) {// incase of getting between this range
			xVelocity = 0;
		}
	}

	public static SpriteObject getCollidableAt(double x, double y) {
		for (SpriteObject object : Driver.getInstance().getScreen().getCollidables()) {
			if (FFScreen.isInside(new Position(x, y), object.getImage().getBounds()))
				return object;
		}
		return null;
	}

	public void updatePosition() {// updates the players current position
		if (isFrozen()) {
			return;
		}
		playerCollide(distance);

		if ((!canMoveRight || !canMoveLeft) && (!canMoveUp || !canMoveDown)) {// means we are stuck in ground
			SpriteObject stuckObject = getCollidableAt(playerPosition.getX(),
					playerPosition.getY() + getImage().getHeight() + distance);
			if (stuckObject != null)
				playerPosition.setY(stuckObject.getPos().getY() - getImage().getHeight() - distance);

			playerCollide(distance);// This prevents clunkyness after falling

			stuckObject = getCollidableAt(playerPosition.getX() + getImage().getWidth(),
					playerPosition.getY() + getImage().getHeight() + distance);
			if (stuckObject != null)
				playerPosition.setY(stuckObject.getPos().getY() - getImage().getHeight() - distance);

		}
		if (!canMoveRight) {// if cant move right
			if (xVelocity >= 0) {// stop only velocity to right
				xVelocity = 0;
				// playerPosition.setX(playerPosition.getX() - 1); //sets slightly back
			}
		}

		if (!canMoveLeft) {// if cant move left
			if (xVelocity <= 0) {// stop velocity to left
				xVelocity = 0;

				// playerPosition.setX(playerPosition.getX() + 1); //sets slightly back
			}
		}

		if (!canMoveUp) {// stop velocity
			yVelocity = 3;
		}

		if (canMoveDown) {// apply gravity
			yVelocity += gravity;
			inAir = true;

			// removes clunkyness when on wall
			if (airRight && canMoveRight)
				setxVelocity(HORIZONTAL_CAP);
			if (airLeft && canMoveLeft)
				setxVelocity(-HORIZONTAL_CAP);

		} else {// on ground and cannot move down
			if (inAir) {
				inAir = false;
				yVelocity = 0;
			}

		}

		if (!moving) {// should decrease velocity as long as keys are not pressed
			decreasexVelocity();
		}
	}

	public void updateX() {
		if (isFrozen())
			return;
		double tempx = playerPosition.getX();
		playerPosition.setX(tempx + xVelocity);
	}

	public void updateY() {
		if (isFrozen())
			return;
		double tempy = playerPosition.getY();
		playerPosition.setY(tempy + yVelocity);
	}

	public void death() {// player is dead
		if (playerLives > 1) {// if player has lives reset position and camera
			Driver.getInstance().resetCameraPos();
			playerPosition.setX(originalPosition.getX());
			playerPosition.setY(originalPosition.getY());
			decrementPlayerLives();
		} else {
			// player is out of lives return to worldMap or death screen
			alive = !alive;
			levelFinished = true;
			Driver.getInstance().switchToScreen(new FFLevelCleared());
			Driver.getInstance().resetCameraPos();
			play.stopSound("sounds", "POL-illusion-castle-short.wav");
		}
	}

	public void resetPosition() {// if player died reset to beginning of level
		playerPosition.setX(originalPosition.getX());
		playerPosition.setY(originalPosition.getY());
	}
}
