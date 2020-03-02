package freezeframe.screens;

import static freezeframe.Tile.TILE_SIZE;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GRect;
import freezeframe.Driver;
import freezeframe.Level;
import freezeframe.Position;
import freezeframe.SpriteObject;
import freezeframe.Tile;
import freezeframe.Tile.TileType;
import starter.AudioPlayer;
import starter.GButton;

public class FFLevelEditor extends FFScreen {

	volatile Tile currentlyMoving;
	// Helpers,Buttons for escape screen
	private boolean escape = false;
	private GRect escapeBackground;
	private GButton backToEdit;
	private GButton saveGame;
	private GButton help, helpReturn;
	GButton buttonExample, buttonReturn;
	ArrayList<SpriteObject> tileIcons;
	private TileType draggedType;
	private boolean newBlock;
	private boolean newPress;
	private SpriteObject tilebackground; // TODO: rename this something that makes sense
	private SpriteObject brickIcon;
	private SpriteObject spikeIcon;
	private SpriteObject winFlagIcon;
	private SpriteObject crateIcon;
	
	private Level level;

	private GImage levelEditorBackground;
	private GImage helpMenu;
	private boolean helpScreen = false;// needed so I can update position of help screen only
	// used for when player moves item positions
	private int x = 0;
	private int y = 0;

	private AudioPlayer play = AudioPlayer.getInstance();

	public FFLevelEditor() {

		level = new Level();
		newBlock = false;
		tileIcons = new ArrayList<SpriteObject>();

		buttonReturn = new GButton("Return to Main Menu", 20, 500, 200, 50, Color.yellow);
		brickIcon = new SpriteObject(new GImage("brick.png"), new Position(20, 25));
		spikeIcon = new SpriteObject(new GImage("spike A.png"), new Position(120, 25));
		spikeIcon.getImage().setSize(50, 50);

		winFlagIcon = new SpriteObject(new GImage("winflag.png"), new Position(220, 30));
		tilebackground = new SpriteObject(new GImage("taskbar.png"), new Position(0, 0));
		crateIcon = new SpriteObject(new GImage("crate.png"), new Position(300, 30));

		objects.add(tilebackground);
		objects.add(brickIcon);
		objects.add(spikeIcon);
		objects.add(winFlagIcon);
		objects.add(crateIcon);
		tileIcons.add(brickIcon);
		tileIcons.add(spikeIcon);
		tileIcons.add(winFlagIcon);
		tileIcons.add(crateIcon);

		newPress = true;

		// Stuff for when escape is called
		saveGame = new GButton("Save Game", 250, 375, 200, 50, Color.yellow);
		help = new GButton("Help", 250, 275, 200, 50, Color.yellow);
		helpReturn = new GButton("Ok", 580, 100, 200, 50, Color.cyan);
		escapeBackground = new GRect(200, 150, 300, 400);
		escapeBackground.setFilled(true);
		escapeBackground.setColor(Color.GRAY);
		backToEdit = new GButton("Return to level Editor", 250, 175, 200, 50, Color.yellow);
		buttonReturn = new GButton("Return to Main Menu", 250, 475, 200, 50, Color.yellow);
		levelEditorBackground = new GImage("editorbackground2.jpg");
		levelEditorBackground.setSize(800, 800);
		helpMenu = new GImage("Help Menu.jpg");

		if (!FFSettingsMenu.isSoundOff())
			play.playSound("sounds", "KarmaNES.wav", true);
	}

	@Override
	public void showContents() {
		Driver.getInstance().add(levelEditorBackground);
		super.showContents();
		if (!FFSettingsMenu.isSoundOff())
			play.playSound("sounds", "KarmaNES.wav");
	}

	@Override
	public void hideContents() {
		Driver.getInstance().removeAll(); // removes the world map
		play.stopSound("sounds", "KarmaNES.wav");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (currentlyMoving != null) {
			currentlyMoving.pos
					.setX(((int) (e.getX() + Driver.getInstance().getCameraPos().getX()) / TILE_SIZE) * TILE_SIZE);
			currentlyMoving.pos
					.setY(((int) (e.getY() + Driver.getInstance().getCameraPos().getY()) / TILE_SIZE) * TILE_SIZE);
			currentlyMoving.drawSprite();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (escape) {// should not be able to move items while in escape menu
			return;
		}

		if (newPress) {
			for (SpriteObject obj : tileIcons) {
				// if the user clicks inside the rectangle
				if (isInside(e, obj.getImage().getBounds())) {
					draggedType = TileType.fromInt(tileIcons.indexOf(obj));
					currentlyMoving = new Tile(draggedType, new Position(e.getX(), e.getY()));
					level.addTile(currentlyMoving);
					Driver.getInstance().add(currentlyMoving.getImage());
					System.out.println(draggedType);
					newBlock = true;
				}
			}
			for (Tile obj : level.getTiles()) {
				// if the user clicks inside the rectangle
				if (isInside(e, obj.getImage().getBounds())) {
					currentlyMoving = obj;
					level.addTile(currentlyMoving);
					Driver.getInstance().add(currentlyMoving.getImage());
				}
				newBlock = false;
			}
		}
		newPress = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {// initial test, not considering bounds
		if (currentlyMoving != null) {
			// move it there
			currentlyMoving.pos
					.setX(((int) (e.getX() + Driver.getInstance().getCameraPos().getX()) / TILE_SIZE) * TILE_SIZE);
			currentlyMoving.pos
					.setY(((int) (e.getY() + Driver.getInstance().getCameraPos().getY()) / TILE_SIZE) * TILE_SIZE);

			// if it's inside the
			if (isInside(e, tilebackground.getImage().getBounds())) {
				Driver.getInstance().remove(currentlyMoving.getImage());
				level.remove(currentlyMoving);
				currentlyMoving = null;
			} else {
				if (newBlock) {
					System.out.println(draggedType);
					System.out.println(currentlyMoving.getPos());
					level.addTile(new Tile(draggedType, currentlyMoving.getPos()));
				} else {
					currentlyMoving.drawSprite();
					System.out.println("placed");
				}
				for (SpriteObject t : level.getTiles()) {
					t.drawSprite();
				}
			}
		}
		currentlyMoving = null;
		newBlock = false;
		newPress = true;
	}

	@Override
	public void keyPressed(KeyEvent e) {// our key presses

		double xBar = tilebackground.pos.getX();
		double yBar = tilebackground.pos.getY();
		double xBrick = brickIcon.pos.getX();
		double yBrick = brickIcon.pos.getY();
		double xSpike = spikeIcon.pos.getX();
		double ySpike = spikeIcon.pos.getY();
		double xWin = winFlagIcon.pos.getX();
		double yWin = winFlagIcon.pos.getY();
		double xCrate = crateIcon.pos.getX();
		double yCrate = crateIcon.pos.getY();

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {// goes to escape screen
			showEscapeScreen();
		}

		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {// removes the currently dragged item
			if (currentlyMoving != null) {
				level.remove(currentlyMoving);
				Driver.getInstance().remove(currentlyMoving.getImage());

			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {// moves screen to left
			x = 10;
			tilebackground.pos.setX(xBar + x);
			brickIcon.pos.setX(xBrick + x);
			spikeIcon.pos.setX(xSpike + x);
			winFlagIcon.pos.setX(xWin + x);
			crateIcon.pos.setX(xCrate + x);
			updateScreen();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// moves screen to right
			x = -10;
			tilebackground.pos.setX(xBar + x);
			brickIcon.pos.setX(xBrick + x);
			spikeIcon.pos.setX(xSpike + x);
			winFlagIcon.pos.setX(xWin + x);
			crateIcon.pos.setX(xCrate + x);
			updateScreen();
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			y = 10;
			tilebackground.pos.setY(yBar + y);
			brickIcon.pos.setY(yBrick + y);
			spikeIcon.pos.setY(ySpike + y);
			winFlagIcon.pos.setY(yWin + y);
			crateIcon.pos.setY(yCrate + y);
			updateScreen();
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			y = -10;
			tilebackground.pos.setY(yBar + y);
			brickIcon.pos.setY(yBrick + y);
			spikeIcon.pos.setY(ySpike + y);
			winFlagIcon.pos.setY(yWin + y);
			crateIcon.pos.setY(yCrate + y);
			updateScreen();
		}
	}

	public void updateScreen() {
		// and help with scrolling on help screen

		// update camera position
		double xCam = Driver.getInstance().getCameraPos().getX();
		double yCam = Driver.getInstance().getCameraPos().getY();
		Driver.getInstance().setCameraPos(xCam + x, yCam + y);
//		spawnpos.drawSprite();
		if (helpScreen) {// will scroll help screen and reset x/y
			scrollHelpScreen();
			x = 0;
			y = 0;
			return;
		}

		for (SpriteObject obj : level.getTiles()) {// This is where we would move tiles from key presses

			obj.drawSprite();

			// hides objects if they are over task bar
			if (obj.getImage().getBounds().intersects(tilebackground.getImage().getBounds())) {
				obj.getImage().setVisible(false);
			} else {
				obj.getImage().setVisible(true);
			}

		}

		// reset x and y
		x = 0;
		y = 0;

	}

	public void showEscapeScreen() {// Shows Escape Screen

		if (!helpScreen) {
			escape = true; // should not be able to move items while in escape menu
			Driver.getInstance().add(escapeBackground);
			Driver.getInstance().add(backToEdit);
			Driver.getInstance().add(help);
			Driver.getInstance().add(buttonReturn);
			Driver.getInstance().add(saveGame);
			addClickables();
		}
	}

	public void removeEscapeScreen() {// removes Escape Screen

		escape = false;// allows access to move items.
		Driver.getInstance().remove(escapeBackground);
		Driver.getInstance().remove(backToEdit);
		Driver.getInstance().remove(help);
		Driver.getInstance().remove(buttonReturn);
		Driver.getInstance().remove(saveGame);
		removeClickables();
	}

	public void showHelpScreen() {// Display Help Screen in Level Editor
		helpScreen = true;
		// we need one clickable on help screen

		removeClickables();
		clickable.add(helpReturn.getBounds());
		behaviors.put(clickable.get(0), () -> hideHelpSreen());

		hideContents();
		Driver.getInstance().add(helpMenu);
		Driver.getInstance().add(helpReturn);
		if (!FFSettingsMenu.isSoundOff())
			play.playSound("sounds", "Smooth Lovin.mp3");
		
	}

	public void scrollHelpScreen() {// helps with scrolling on help screen.
									// only goes up and down.
		if (helpMenu.getY() > 0) {// bounds for helpMenu movement.
			helpMenu.setLocation(helpMenu.getX(), 0);
			return;
		}
		if (helpMenu.getY() < -150) {// bounds for movement
			helpMenu.setLocation(helpMenu.getX(), -150);
			return;
		}
		helpMenu.setLocation(helpMenu.getX(), helpMenu.getY() + y);
		y = 0;
	}

	public void hideHelpSreen() {// hides the help screen and returns to escape screen
		helpScreen = false;
		removeClickables();
		hideContents();
		showContents();
		showEscapeScreen();
		addClickables();
		play.stopSound("sounds", "Smooth Lovin.mp3");
	}

	public void saveLevel() {// saves the current items on the screen
		try {
			level.save(new File("User.level")); // TODO: generate unique level names
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addClickables() {// adds clickables bounds to screen
		clickable.add(buttonReturn.getBounds());
		behaviors.put(clickable.get(0), () -> Driver.getInstance().switchToScreen(new FFMainMenu()));
		clickable.add(backToEdit.getBounds());
		behaviors.put(clickable.get(1), () -> removeEscapeScreen());
		clickable.add(help.getBounds());
		behaviors.put(clickable.get(2), () -> showHelpScreen());
		clickable.add(helpReturn.getBounds());
		behaviors.put(clickable.get(3), () -> hideHelpSreen());
		clickable.add(saveGame.getBounds());
		behaviors.put(clickable.get(4), () -> saveLevel());
	}

	public void removeClickables() {// removes clickables bounds from screen
		clickable.clear();
		behaviors.clear();
	}
}
