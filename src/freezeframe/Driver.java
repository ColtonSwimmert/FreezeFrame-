package freezeframe;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import freezeframe.screens.FFMainMenu;
import freezeframe.screens.FFScreen;
import starter.AudioPlayer;
import starter.MainApplication;

public class Driver extends MainApplication {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	private FFScreen currentScreen;
	private Position cameraPosition;
	
	private static Driver instance;
	public static Driver getInstance() {
		return instance;
	}
	
	public void init() {
		instance = this;
		cameraPosition = new Position(0, 0);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		System.out.println("Initializing");
		cameraPosition = new Position(0,0);

	}
	
	
	public Position getCameraPos() {
		return cameraPosition;
	}
	
	public void setCameraPos(double x, double y)
	{//sets camera position
		cameraPosition.setX(x);
		cameraPosition.setY(y);
	}
	public void resetCameraPos()
	{//resets cameraPosition
		Driver.getInstance().getCameraPos().setX(0);
		Driver.getInstance().getCameraPos().setY(0);
	}
	public void switchToScreen(FFScreen screen) {
		cameraPosition = new Position(0,0);
		//TODO: someone who knows how, stop all sounds here
		if(currentScreen != null) {
			currentScreen.hideContents();
		}
		screen.showContents();
		currentScreen = screen;
	}
	
	public void exitProgram()
	{
		if (JOptionPane.showConfirmDialog(null,"Are you sure?","Exit Game",0,3) == 0)
		System.exit(0);
		else
			return;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		currentScreen.mousePressed(e);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		currentScreen.mouseClicked(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		currentScreen.mouseDragged(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		currentScreen.mouseReleased(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		currentScreen.mouseMoved(e);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		currentScreen.keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		//was keyPressed before
		currentScreen.keyReleased(e);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		currentScreen.keyTyped(e);
	}
	
	public FFScreen getScreen() {
		return currentScreen;
	}
	
	public void run() {
		switchToScreen(new FFMainMenu());
		addMouseListeners();
		addKeyListeners();
		MainTimer timer = new MainTimer();
		timer.start();
	}

}
