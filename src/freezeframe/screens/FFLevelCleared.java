package freezeframe.screens;

import java.awt.Color;
import freezeframe.Driver;
import freezeframe.PhysicsObject;
import starter.AudioPlayer;
import starter.GButton;
import acm.graphics.*;

public class FFLevelCleared extends FFScreen {
	private GImage winScreen = new GImage("win.jpg");
	private GImage deathScreen = new GImage("death.jpg");
	GButton returnButton;
	private AudioPlayer play = AudioPlayer.getInstance();
	
	
	public FFLevelCleared() {
		if (PhysicsObject.getAlive()) { 
			winScreen.setSize(800, 600);
			if (!FFSettingsMenu.isSoundOff()) {
				play.playSound("sounds", "little_robot_sound_factory_Jingle_Win_00.mp3");
			}
		}
		else {
			deathScreen.setSize(800, 600);
			if (!FFSettingsMenu.isSoundOff())
				play.playSound("sounds", "Dark Souls - You Died Sound Effect.mp3");
		}
		
		returnButton = new GButton("Return to World Map", 300, 500, 200, 50, Color.red);
		
		clickable.add(returnButton.getBounds());
		behaviors.put(clickable.get(0), () -> Driver.getInstance().switchToScreen(new FFWorldMap()));
	}

	public void showContents() {
		if (PhysicsObject.getAlive())
			Driver.getInstance().add(winScreen);
		else 
			Driver.getInstance().add(deathScreen);
		Driver.getInstance().add(returnButton);
		super.showContents();
	}
	
	@Override
	public void hideContents() {
		Driver.getInstance().removeAll();
		if (PhysicsObject.getAlive()) {
			play.stopSound("sounds", "little_robot_sound_factory_Jingle_Win_00.mp3");
		}
		else {
			play.stopSound("sounds", "Dark Souls - You Died Sound Effect.mp3");
		}
		
	}
}
