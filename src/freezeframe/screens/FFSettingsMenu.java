package freezeframe.screens;

import java.awt.Color;

import acm.graphics.GImage;
import freezeframe.Driver;
import starter.AudioPlayer;
import starter.GButton;

public class FFSettingsMenu extends FFScreen{

	private AudioPlayer play = AudioPlayer.getInstance();
	GButton buttonSound, buttonReturn, buttonSoundOff;
	private static volatile boolean soundOff = false, isClicked = false;
	private GImage backGround = new GImage("freezeframebackGround.png");
	
	public FFSettingsMenu()
	{
		
		buttonSound = new GButton("Sound:On",250,140,250,100,Color.orange);
		buttonSoundOff = new GButton("Sound:Off",250,140,250,100,Color.orange);
		
		buttonReturn = new GButton("Return to Main Menu", 275, 350, 200,50,Color.orange);
		clickable.add(buttonReturn.getBounds());
		behaviors.put(clickable.get(0), () -> Driver.getInstance().switchToScreen(new FFMainMenu()));
		clickable.add(buttonSound.getBounds());
		
		behaviors.put(clickable.get(1), () -> pushbutton());
		
		if (!soundOff) {
			play.playSound("sounds", "POL-8-ball-cafe-short.wav", true);
			buttonSoundOff.setVisible(false);
		}
	}
	
	void pushbutton() {
		if(isClicked) {
		    if(!soundOff) {
			    play.pauseSound("sounds", "POL-8-ball-cafe-short.wav");
				clickable.remove(1);
				clickable.add(buttonSoundOff.getBounds());
				buttonSoundOff.setVisible(true);
		    }else {
			    play.playSound("sounds", "POL-8-ball-cafe-short.wav");
			    buttonSoundOff.setVisible(false);
			    clickable.remove(1);
			    clickable.add(buttonSound.getBounds());
		    }
		    soundOff = !soundOff;
		}
		isClicked = !isClicked;
	}
	
	@Override
	public void showContents()
	{
		Driver.getInstance().add(backGround);
		Driver.getInstance().add(buttonSound);
		Driver.getInstance().add(buttonSoundOff);
		Driver.getInstance().add(buttonReturn);
		
		super.showContents();
		
		if (!soundOff) {
			play.playSound("sounds", "POL-8-ball-cafe-short.wav", true);
		}
	}
	@Override
	public void hideContents()
	{
		clickable.clear();
		behaviors.clear();
		Driver.getInstance().removeAll();	//removes the world map
		play.stopSound("sounds", "POL-8-ball-cafe-short.wav");
	}

	public static boolean isSoundOff() {
		return soundOff;
	}
	
	public static void setSoundOff(boolean soundOff) {
		FFSettingsMenu.soundOff = soundOff;
	}
	
	public static boolean isClicked() {
		return isClicked;
	}

	public static void setClicked(boolean isClicked) {
		FFSettingsMenu.isClicked = isClicked;
	}
}
