package freezeframe.screens;

import static freezeframe.Tile.TILE_SIZE;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.graphics.GRectangle;
import freezeframe.Driver;
import freezeframe.Level;
import freezeframe.PhysicsObject;
import freezeframe.Player;
import freezeframe.PlayerState;
import freezeframe.SpriteObject;
import freezeframe.Tile;
import freezeframe.Tile.TileType;
import starter.AudioPlayer;
import starter.GButton;

public class FFGame extends FFScreen {
	// ====================================================
	// ====================================================
	private boolean escape = false;

	// idea is to have variables to constantly change starting positions with each
	// level
	private Player player1 = new Player(400, 150);

	// somehow we need to make it so Player variables declared in the future
	// will be able to be easily changed

	// might be something to do with level generator and loading the level and stuff

	// variables for escape screen
	private ArrayList<GButton> escapeButtons = new ArrayList<GButton>();
	private GRect escapeBackground;
	private GButton button1;
	private GButton button3;
	private GButton button4;

	// ====================================================
	// ====================================================

	private Level level;
	private GImage background = new GImage("gamebackground.png");

	private SpriteObject currentlyMoving;
	private GLabel lives = new GLabel("Lives: " + player1.getPlayerLives(), 40, 40);
	private GRect livesBox = new GRect(40, 40, 40, 40);

	private AudioPlayer play = AudioPlayer.getInstance();

	private int timer = 0;

	private ArrayList<SpriteObject> spikes = new ArrayList<SpriteObject>();

	public FFGame() {// empty constructor
		Player.setAlive(true);
		PhysicsObject.setLevelFinished(false);
		background.setSize(Driver.WINDOW_WIDTH, Driver.WINDOW_HEIGHT);
		level = new Level(new File("default.level"));
		player1.init(); // pass PlayerState into SpriteObject
		createButtons();
	}

	public FFGame(Level level) {
		Player.setAlive(true);
		PhysicsObject.setLevelFinished(false);
		background.setSize(Driver.WINDOW_WIDTH, Driver.WINDOW_HEIGHT);
		player1.init(); // pass PlayerState into SpriteObject
		Driver.getInstance().add(background);
		this.level = level; // set the instance variable level to the value passed in the arguments of the
							// constructor
		level.getTiles().forEach(t -> objects.add(t));

		createButtons();
		if (FFSettingsMenu.isSoundOff() == false)
			play.playSound("sounds", "POL-illusion-castle-short.wav", true);
	}

	public void keyReleased(KeyEvent e) {// key Released

		if (escape)
			return; // in escape menu

		player1.KeyReleased(e);
//		System.out.println("Key Released!");
//		System.out.printf("Current Position, %f %f %n", player1.getX(), player1.getY());
	}

	public void keyPressed(KeyEvent e) {// key is pressed

		if (escape)
			return; // in escape menu
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			showEscapeScreen();
			escape = true;
			// Driver.getInstance().switchToScreen(new FFWorldMap());
			// Driver.getInstance().resetCameraPos();
			return;
		}

		player1.KeyPressed(e);// passes key event
//		System.out.println("Key Pressed!");
//		System.out.printf("Current Position, %f %f %n", player1.getX(), player1.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (Player.isFrozen()) {
			SpriteObject obj = PhysicsObject.getCollidableAt(e.getX(), e.getY());
			if (obj != null && (obj instanceof Tile && ((Tile) obj).getType() == TileType.crate)) {
				currentlyMoving = obj;

			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (Player.isFrozen()) {
			if (currentlyMoving != null)
				currentlyMoving.getImage().setLocation(
						((int) (e.getX() + Driver.getInstance().getCameraPos().getX() % TILE_SIZE) / TILE_SIZE)
								* TILE_SIZE,
						((int) (e.getY() + Driver.getInstance().getCameraPos().getY() % TILE_SIZE) / TILE_SIZE)
								* TILE_SIZE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (Player.isFrozen()) {
			if (currentlyMoving != null) {
				// move it there

				/*
				 * Position temp = new Position(currentlyMoving.getPos());
				 * currentlyMoving.getPos().setX((e.getX() / TILE_SIZE) * TILE_SIZE);
				 * currentlyMoving.getPos().setY((e.getY() / TILE_SIZE) * TILE_SIZE);
				 * level.moveTile(temp, currentlyMoving.getPos());
				 */

				currentlyMoving.pos
						.setX(((int) (e.getX() + Driver.getInstance().getCameraPos().getX()) / TILE_SIZE) * TILE_SIZE);
				currentlyMoving.pos
						.setY(((int) (e.getY() + Driver.getInstance().getCameraPos().getY()) / TILE_SIZE) * TILE_SIZE);
				currentlyMoving.drawSprite();
				// currentlyMoving.getImage().setLocation(currentlyMoving.getPos().getX(),currentlyMoving.getPos().getY());

			}
		}
		currentlyMoving = null;
	}

	@Override
	public boolean isColliding(double x, double y) {
		boolean out = false;
		for (SpriteObject obj : getCollidables()) {
			GRectangle grect = obj.getImage().getBounds();
			double dx = x - grect.getX();
			double dy = y - grect.getY();
			// if the user clicks inside the rectangle
			out = ((dx >= 0 && dx <= grect.getWidth()) && (dy >= 0 && dy <= grect.getHeight()));
			if (out)
				break;
		}
		return out;
	}

	@Override
	public void update() {// my update function for FFGame

		timer++;
		updatePlayer();
		updateScreen();
		updateLives();

	}

	public void updateLives() {
		if (PhysicsObject.getAlive() == false || PhysicsObject.getLevelFinished() == true)
			Driver.getInstance().remove(lives);

		else if (PhysicsObject.getAlive()) {
			Driver.getInstance().remove(livesBox);
			Driver.getInstance().remove(lives);

			lives = new GLabel("Lives: " + player1.getPlayerLives(), 40, 40);
			livesBox = new GRect(35, 22, 52, 25);
			livesBox.setColor(Color.black);
			livesBox.setFilled(true);
			lives.setColor(Color.red);

			Driver.getInstance().add(livesBox);
			Driver.getInstance().add(lives);
		}
	}

	public void loadNextImage() {
		if (timer > 20) { // if the timer is over 20, set the next Image
			player1.nextImage(player1.getPlayerState());
			timer = 0;
		}
	}

	// Die will go through all spikes on screen and detect whether player has
	// stepped on a spike
	// and will also detect if player has fallen outside of bounds of map
	public void die() {
		SpriteObject obj = PhysicsObject.getCollidableAt(player1.getX(),player1.getY() + player1.getImage().getHeight() + 3);
		SpriteObject obj2 = PhysicsObject.getCollidableAt(player1.getX() + player1.getImage().getWidth() ,player1.getY() + player1.getImage().getHeight() + 3);
		if ((obj instanceof Tile && ((Tile) obj).getType() == TileType.spike) || (obj2 instanceof Tile && ((Tile) obj2).getType() == TileType.spike)) 
		{
			player1.death();
		}

		if (player1.getY() > 1500) {
			player1.death();
		}
	}

	
	public void win()
	{//win condition
		SpriteObject obj = PhysicsObject.getCollidableAt(player1.getX(),player1.getY() + player1.getImage().getHeight() + 3);
		SpriteObject obj2 = PhysicsObject.getCollidableAt(player1.getX() + player1.getImage().getWidth() ,player1.getY() + player1.getImage().getHeight() + 3);
		SpriteObject obj3 = PhysicsObject.getCollidableAt(player1.getX() - 3, player1.getY() + player1.getImage().getHeight());
		SpriteObject obj4 = PhysicsObject.getCollidableAt(player1.getX() + player1.getImage().getWidth() + 3, player1.getY() + player1.getImage().getHeight());
		if ((obj instanceof Tile && ((Tile) obj).getType() == TileType.winflag) || (obj2 instanceof Tile && ((Tile) obj2).getType() == TileType.winflag)) 
		{
			player1.resetPosition();
			Driver.getInstance().resetCameraPos();
			Driver.getInstance().switchToScreen(new FFLevelCleared());
			play.stopSound("sounds", "POL-illusion-castle-short.wav");
		}
		else if ((obj3 instanceof Tile && ((Tile) obj3).getType() == TileType.winflag) || (obj4 instanceof Tile && ((Tile) obj4).getType() == TileType.winflag)) 
		{
			player1.resetPosition();
			Driver.getInstance().resetCameraPos();
			Driver.getInstance().switchToScreen(new FFLevelCleared());
			play.stopSound("sounds", "POL-illusion-castle-short.wav");
		}
	}

	// possibly refactor win/lose into here
	public void didIWinOrLose() {

	}

	public void updateScreen() {
		if (PhysicsObject.isFrozen())
			return; // ignore camera position updates

		// update camera based on player Velocity
		double xCam = Driver.getInstance().getCameraPos().getX();
		double yCam = Driver.getInstance().getCameraPos().getY();
		double x = player1.getxVelocity();
		double y = (player1.getY() < 0) ? player1.getY() * 1.25 : 0;
		Driver.getInstance().setCameraPos(xCam + x, yCam);
		for (SpriteObject obj : objects) {
			obj.drawSprite();
		}

		for (SpriteObject obj : spikes) {
			obj.drawSprite();
		}
	}

	public void updatePlayer() {// updates player

		if (escape)
			return; // disregard updating player positon while in escape screen
		Driver.getInstance().remove(player1.getImage());
		loadNextImage();
		player1.getImage().setSize(50, 75);
		player1.updatePosition();

		double spriteX = player1.getPos().getX();
		double spriteY = player1.getPos().getY();

		if (player1.inAir == false && player1.getIsMoving() == false)
			player1.setPlayerState(PlayerState.STANDING);

		Driver.getInstance().add(player1.getImage());
		player1.getImage().setLocation(spriteX, spriteY);
		win();
		die();

	}

	public void showEscapeScreen() {
		escape = true;

		player1.stopVelocity();

		Driver.getInstance().add(escapeBackground);
		for (GButton button : escapeButtons) {
			Driver.getInstance().add(button);
		}
		addClickables();
	}

	public void hideContents() {
		Driver.getInstance().removeAll();
		play.stopSound("sounds", "POL-illusion-castle-short.wav");
	}

	public void hideEscapeScreen() {
		escape = false;
		Driver.getInstance().remove(escapeBackground);
		for (GButton button : escapeButtons) {
			Driver.getInstance().remove(button);
		}
		// removeClickables();
	}

	public void showHelpScreen() {

	}

	public void removeHelpScreen() {

	}

	public void deathScreen() {

	}

	@Override
	public void showContents() {
		Driver.getInstance().add(background);
		super.showContents();
	}

	public void createButtons() {// create buttons for escape screen
		escapeBackground = new GRect(200, 150, 300, 400);
		escapeBackground.setFilled(true);
		escapeBackground.setColor(Color.gray);

		button1 = new GButton("Return to Game", 250, 175, 200, 50, Color.cyan);
		button3 = new GButton("Exit Level", 250, 330, 200, 50, Color.cyan);
		button4 = new GButton("Return to Menu", 250, 475, 200, 50, Color.cyan);

		escapeButtons.add(button1);
		escapeButtons.add(button3);
		escapeButtons.add(button4);

	}

	public void addClickables() {// adds clickables bounds to screen
		clickable.add(button1.getBounds());
		behaviors.put(clickable.get(0), () -> hideEscapeScreen());
		clickable.add(button3.getBounds());
		behaviors.put(clickable.get(1), () -> Driver.getInstance().switchToScreen(new FFWorldMap()));
		clickable.add(button4.getBounds());
		behaviors.put(clickable.get(2), () -> Driver.getInstance().switchToScreen(new FFMainMenu()));
	}

	public void removeClickables() { // removes clickables
		clickable.clear();
		behaviors.clear();
	}

}
