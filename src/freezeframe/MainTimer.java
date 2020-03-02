package freezeframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class MainTimer extends Timer{
	
	public MainTimer() {
		super(5, e -> function(e));
	}
	
	public static void function(ActionEvent e) {
		Driver.getInstance().getScreen().update();
	}
}
