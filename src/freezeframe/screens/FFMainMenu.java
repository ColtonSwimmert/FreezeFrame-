package freezeframe.screens;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import freezeframe.Driver;
import starter.GButton;//importing button for button presses
import starter.AudioPlayer;


public class FFMainMenu extends FFScreen
{
	//Reference: Screen size is 800x600
	private ArrayList<GButton> menu_List = new ArrayList<GButton>(); 
	private GImage backGround = new GImage("freezeframebackGround.png");
	private GLabel title = new GLabel("FREEZE FRAME!",80,80);
	private AudioPlayer play = AudioPlayer.getInstance();
	
	public FFMainMenu() 
	{//creates base items in our main menu
		
		title.setFont("Arial-Bold-72");
		title.setColor(Color.cyan);
		
		GButton startGame = new GButton("Start Game",270,120,200,50,Color.cyan);
		GButton levelEditor = new GButton("Level Editor",270,220,200,50,Color.cyan);
		GButton settings = new GButton("Settings",270,320,200,50,Color.cyan);
		GButton exit = new GButton("Exit",270,420,200,50,Color.cyan);
		

		//add to list
		menu_List.add(startGame);
		menu_List.add(levelEditor);
		menu_List.add(settings);
		menu_List.add(exit);
		
		if (FFSettingsMenu.isSoundOff() == false)
			play.playSound("sounds", "Itty Bitty 8 Bit.mp3", true);
	}

	
	
	@Override
	public void showContents() 
	{//Adding 4 boxes
		
		Driver.getInstance().add(backGround);
		
		Driver.getInstance().add(title);
		
		for(GButton buttons: menu_List)
		{
			Driver.getInstance().add(buttons); //adds menu buttons
		}
		
		//THE ORDER OF CLICKABLES.ADD MUST BE SYNCED WITH BEHAVIORS.ADD
		
		clickable.add(menu_List.get(0).getBounds());	//START GAME
		clickable.add(menu_List.get(1).getBounds());	//LEVEL EDITOR
		clickable.add(menu_List.get(2).getBounds());	//SETTINGS
		clickable.add(menu_List.get(3).getBounds());	//EXIT GAME
		
		behaviors.put(menu_List.get(0).getBounds(), () -> Driver.getInstance().switchToScreen(new FFWorldMap()));	//START GAME
		behaviors.put(menu_List.get(1).getBounds(), () -> Driver.getInstance().switchToScreen(new FFLevelEditor()));	//LEVEL EDITOR
		behaviors.put(menu_List.get(2).getBounds(), () -> Driver.getInstance().switchToScreen(new FFSettingsMenu()));	//SETTINGS
		behaviors.put(menu_List.get(3).getBounds(), () -> Driver.getInstance().exitProgram());	//EXIT GAME

		if (FFSettingsMenu.isSoundOff() == false)
			play.playSound("sounds", "Itty Bitty 8 Bit.mp3", true);
	}
	
	@Override
	public void hideContents() 
	{
		Driver.getInstance().removeAll(); //removes menu
		clickable.clear();
		behaviors.clear();
		play.stopSound("sounds", "Itty Bitty 8 Bit.mp3");
	}
}
