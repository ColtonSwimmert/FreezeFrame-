package freezeframe.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import freezeframe.Driver;
import freezeframe.Level;
import freezeframe.Player;
import freezeframe.Position;
import freezeframe.SpriteObject;
import starter.AudioPlayer;
import starter.GButton;
import acm.graphics.*;
import acm.program.*;

public class FFWorldMap extends FFScreen{
	private GImage background = new GImage("world_map.jpg");
	GButton buttonReturn;
	GImage id0,id1,id2,id3;
	Graphics g;
	GRect lvl_1, lvl_2, lvl_3, lvl_4, lvl_5, lvl_6, lvl_7, lvl_8, lvl_tutorial, lvl_user;
	GLabel txt_1, txt_2, txt_3, txt_4, txt_5, txt_6, txt_7, txt_8, txt_tutorial, txt_user;
	Level
		level_tutorial = new Level(new File("tutorial.level")),
		level_1 = new Level(new File("coltonlevellevellevel.level")),
		level_2 = new Level(new File("coltonlvl1.level")),
		level_3 = new Level(new File("coltonlvl2.level")), 
	    level_4 = new Level(new File("ShhSecret.level")),
		level_5 = new Level(new File("e10_1.level")), 
		level_6 = new Level(new File("Ryans.level")), 
		level_7 = new Level(new File("e10_2.level")), 
		level_8 = new Level(new File("ExtraHard.level")),
		level_user = new Level(new File("user.level"))
		;
	
	
	//IDEA: instead of having the player spawn at "start" all the time, save the last
	//known x and y coordinates and have them load there ??
	public double startX = 40, startY = 40;
	private AudioPlayer play = AudioPlayer.getInstance();
	
	
	
	
	
			
	public FFWorldMap()
	{
		background.setSize(800,600);	//sets the size of the background
		
		//-----------------------------------------------------------
		//-----------------------------------------------------------
		//add GRects for level entrances into an ArrayList
		//GRect(x,y,width,size)
	
		//lvl # GRect & lvl # GLabel
		
		//-----------------------------------------------------------
		
		lvl_tutorial = new GRect(350,30,70,70);
		lvl_tutorial.setVisible(true);
		lvl_tutorial.setFilled(true);
		lvl_tutorial.setFillColor(Color.red);
		level_List.add(lvl_tutorial.getBounds());
		
		txt_tutorial = new GLabel("Tutorial", 365, 70);
		
		//-----------------------------------------------------------

		
		lvl_1 = new GRect(550,115,70,70);
		lvl_1.setVisible(true);
		lvl_1.setFilled(true);
		lvl_1.setFillColor(Color.blue);
		level_List.add(lvl_1.getBounds());
		
		txt_1 = new GLabel("Level 1", 565, 155);
		
		//-----------------------------------------------------------

		lvl_2 = new GRect(340,175,70,70);
		lvl_2.setVisible(true);
		lvl_2.setFilled(true);
		lvl_2.setFillColor(Color.PINK);
		level_List.add(lvl_2.getBounds());
		
		txt_2 = new GLabel("Level 2", 355, 215);
		
		//-----------------------------------------------------------

		lvl_3 = new GRect(160,235,70,70);
		lvl_3.setVisible(true);
		lvl_3.setFilled(true);
		lvl_3.setFillColor(Color.yellow);
		level_List.add(lvl_3.getBounds());
		
		txt_3 = new GLabel("Level 3", 175, 275);
		
		//-----------------------------------------------------------

		lvl_4 = new GRect(240,325,70,70);
		lvl_4.setVisible(true);
		lvl_4.setFilled(true);
		lvl_4.setFillColor(Color.white);
		level_List.add(lvl_4.getBounds());
		
		txt_4 = new GLabel("Level 4", 255, 365);
		
		//-----------------------------------------------------------
		
		lvl_5 = new GRect(485,285,70,70);
		lvl_5.setColor(Color.green);
		lvl_5.setVisible(true);
		lvl_5.setFilled(true);
		level_List.add(lvl_5.getBounds());
		
		txt_5 = new GLabel("Level 5", 500, 325);
		
		//-----------------------------------------------------------

		lvl_6 = new GRect(310,460,70,70);
		lvl_6.setColor(Color.cyan);
		lvl_6.setVisible(true);
		lvl_6.setFilled(true);
		level_List.add(lvl_6.getBounds());
		
		txt_6 = new GLabel("Level 6", 325, 500);
		
		//-----------------------------------------------------------

		lvl_7 = new GRect(485,465,70,70);
		lvl_7.setColor(Color.magenta);
		lvl_7.setVisible(true);
		lvl_7.setFilled(true);
		level_List.add(lvl_7.getBounds());

		txt_7 = new GLabel("Level 7", 500, 500);
		
		//-----------------------------------------------------------

		lvl_8 = new GRect(655,435,70,70);
		lvl_8.setColor(Color.orange);
		lvl_8.setVisible(true);
		lvl_8.setFilled(true);
		level_List.add(lvl_8.getBounds());
		
		txt_8 = new GLabel("Level 8", 670, 470);
		
		//-----------------------------------------------------------

		lvl_user = new GRect(700, 30, 70,70);
		lvl_user.setColor(Color.LIGHT_GRAY);
		lvl_user.setVisible(true);
		lvl_user.setFilled(true);
		level_List.add(lvl_user.getBounds());
		
		txt_user = new GLabel("My Level", 712, 70);
		
		//-----------------------------------------------------------
		//-----------------------------------------------------------
		//button to return to menu
		
		buttonReturn = new GButton("Return to Main Menu", 10, 540, 200,50,Color.red);
		
		//-----------------------------------------------------------
		//sprite that walks around
		
		id0 = new GImage("wizzard_m_idle_anim_f0.png",40,40);
		id0.scale(2);
		sprites.add(id0);

		//-----------------------------------------------------------
		//add the menu button to the clickable arrayList
		
		clickable.add(buttonReturn.getBounds());
		
		//-----------------------------------------------------------
		//Return to menu button returns you to menu
		
		behaviors.put(clickable.get(0), () -> Driver.getInstance().switchToScreen(new FFMainMenu()));
	
		//-----------------------------------------------------------
		//rectangle (level_list) loads a specific level; called in FFScreen in KeyPressed
		
		lvl_exe.put(level_List.get(0), () -> Driver.getInstance().switchToScreen(new FFGame(level_tutorial)));
		lvl_exe.put(level_List.get(1), () -> Driver.getInstance().switchToScreen(new FFGame(level_1)));
		lvl_exe.put(level_List.get(2), () -> Driver.getInstance().switchToScreen(new FFGame(level_2)));
		lvl_exe.put(level_List.get(3), () -> Driver.getInstance().switchToScreen(new FFGame(level_3)));
		lvl_exe.put(level_List.get(4), () -> Driver.getInstance().switchToScreen(new FFGame(level_4)));
		lvl_exe.put(level_List.get(5), () -> Driver.getInstance().switchToScreen(new FFGame(level_5)));
		lvl_exe.put(level_List.get(6), () -> Driver.getInstance().switchToScreen(new FFGame(level_6)));
		lvl_exe.put(level_List.get(7), () -> Driver.getInstance().switchToScreen(new FFGame(level_7)));
		lvl_exe.put(level_List.get(8), () -> Driver.getInstance().switchToScreen(new FFGame(level_8)));		
		lvl_exe.put(level_List.get(9), () -> Driver.getInstance().switchToScreen(new FFGame(level_user)));
		
		//-----------------------------------------------------------

		
		if (FFSettingsMenu.isSoundOff() == false)
			play.playSound("sounds", "Overworld.mp3", true);
	}
	
	//============================================================================

	
	@Override
	public void showContents(){
		Driver.getInstance().add(background);
		
		Driver.getInstance().add(lvl_tutorial);
		Driver.getInstance().add(txt_tutorial);
		
		Driver.getInstance().add(lvl_1);
		Driver.getInstance().add(txt_1);
		
		Driver.getInstance().add(lvl_2);
		Driver.getInstance().add(txt_2);
		
		Driver.getInstance().add(lvl_3);
		Driver.getInstance().add(txt_3);
		
		Driver.getInstance().add(lvl_4);
		Driver.getInstance().add(txt_4);
		
		Driver.getInstance().add(lvl_5);
		Driver.getInstance().add(txt_5);
		
		Driver.getInstance().add(lvl_6);
		Driver.getInstance().add(txt_6);
		
		Driver.getInstance().add(lvl_7);
		Driver.getInstance().add(txt_7);
		
		Driver.getInstance().add(lvl_8);
		Driver.getInstance().add(txt_8);
		
		Driver.getInstance().add(lvl_user);
		Driver.getInstance().add(txt_user);

		Driver.getInstance().add(buttonReturn);
		Driver.getInstance().add(id0);

		
		//Driver.getInstance().Animation.animate());
		super.showContents();
		
		if (!FFSettingsMenu.isSoundOff())
			play.playSound("sounds", "Overworld.mp3", true);
	}
	
	@Override
	public void hideContents()
	{
		Driver.getInstance().removeAll();	//removes the world map
		play.stopSound("sounds", "Overworld.mp3");
	}
	
}


