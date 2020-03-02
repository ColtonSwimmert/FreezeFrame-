package freezeframe.screens;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import acm.program.*;
import acm.graphics.*;
import freezeframe.Driver;
import freezeframe.PhysicsObject;
import freezeframe.Player;
import freezeframe.Position;
import freezeframe.SpriteObject;
import freezeframe.Tile;
import starter.GraphicsPane;

public class FFScreen extends GraphicsPane {

	protected LinkedList<SpriteObject> objects = new LinkedList<SpriteObject>();
	protected ArrayList<GRectangle> clickable = new ArrayList<GRectangle>(); // <-Every clickable button/rectangle
	protected HashMap<GRectangle, Runnable> behaviors = new HashMap<GRectangle, Runnable>(); // <-what happens when you
																								// click said rectangle

	protected ArrayList<GImage> sprites = new ArrayList<GImage>();
	protected HashMap<GRectangle, Runnable> lvl_exe = new HashMap<GRectangle, Runnable>();
	protected ArrayList<GRectangle> level_List = new ArrayList<GRectangle>();
	

	public void add(SpriteObject obj) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		for (GImage id : sprites) {
			// For some reason location updates by 4 each time a key is pressed
			// This caused a problem with implementing setLocation
			// out of bounds check for X
			if (id.getBounds().getLocation().getX() + id.getWidth() > 800)
				id.setLocation(800 - id.getWidth(), id.getY());
			if (id.getBounds().getLocation().getX() < 0)
				id.setLocation(0, id.getY());

			// out of bounds check for Y
			if (id.getBounds().getLocation().getY() + id.getHeight() > 600)
				id.setLocation(id.getX(), 600 - id.getHeight());
			if (id.getBounds().getLocation().getY() < 0)
				id.setLocation(id.getX(), 0);

			switch (("" + e.getKeyChar()).toLowerCase().charAt(0)) {
			// swapped to key code for consistency with level editor and ffgame
			case 'd':
				id.move(5, 0);
				break;
			case 'a':
				id.move(-5, 0);
				break;
			case 's':
				id.move(0, 5);
				break;
			case 'w':
				id.move(0, -5);
				break;
			case ' ':
					for (GRectangle grect : level_List) {
						if (id.getBounds().intersects(grect)) {
							lvl_exe.get(grect).run();
						}
					}
					break;
			}
			return;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (GRectangle grect : clickable) {
			// for every rectangle in clickable
			// if the user clicks inside the rectangle
			if (isInside(e, grect)) {
				// run the behavior associated with that rectangle
				behaviors.get(grect).run();
			}
		}
	}

	public boolean isColliding(double x, double y) {
		return false;
	}

	public ArrayList<SpriteObject> getCollidables() {
		ArrayList<SpriteObject> out = new ArrayList<SpriteObject>();
		for (SpriteObject o : objects) {
			if (o instanceof PhysicsObject || o instanceof Tile) {
				out.add(o);
			}
		}
		return out;
	}

	@Override
	public void showContents() {
		objects.forEach(o -> Driver.getInstance().add(o.drawSprite()));

	}

	@Override
	public void hideContents() {
		// TODO Auto-generated method stub

	}

	public void update() {
		objects.forEach(obj -> obj.drawSprite());
		// Anything else that needs to be called when update
	}

	public static boolean isInside(MouseEvent e, GRectangle grect) {
		return isInside(new Position(e.getX(), e.getY()), grect);
	}

	public static boolean isInside(Position pos, GRectangle grect) {
		double dx = pos.getX() - grect.getX();
		double dy = pos.getY() - grect.getY();
		// if the user clicks inside the rectangle
		return (dx >= 0 && dx <= grect.getWidth()) && (dy >= 0 && dy <= grect.getHeight());
	}
}
