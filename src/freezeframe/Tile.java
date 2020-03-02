package freezeframe;

import java.awt.Image;

import javax.swing.ImageIcon;

//now extends SpriteObject
public class Tile extends SpriteObject {

	public static final int TILE_SIZE = 50;
	private TileType type;

	@Deprecated
	public Tile(Image image, Position pos) {
		super(image, pos);
		System.err.println("Do not use the constructor Tile(Image, Position)\nUse Tile(TileType, Position)");
	}

	public Tile(TileType t, Position pos) {
		super(t.getImage(), pos);
		type = t;
	}

	public TileType getType() {
		return type;
	}

	public static enum TileType {
		brick, spike, winflag, crate, spawn;
		public static TileType fromInt(int i) {
			switch (i) {
			case 0:
				return brick;
			case 1:
				return spike;
			case 2:
				return winflag;
			case 3:
				return crate;
			case 4:
				return spawn;
			default:
				System.err.println("File load error, tried to construct invalid tile type");
				return null;
			}
		}

		public int toInt() {
			switch (this) {
			case brick:
				return 0;
			case spike:
				return 1;
			case winflag:
				return 2;
			case crate:
				return 3;
			case spawn:
				return 4;
			default:
				System.err.println("out of bounds");
				return -1;
			}
		}

		public Image getImage() {
			switch (this) {
			case brick:
				return new ImageIcon("brick.png").getImage();
			case spike:
				return new ImageIcon("spike A.png").getImage();
			case winflag:
				return new ImageIcon("winflag.png").getImage(); // now reads win flag
			case crate:
				return new ImageIcon("crate.png").getImage();
			case spawn:
				return new ImageIcon("spawnPoint.png").getImage();
			default:
				return null;
			}
		}
	}
}
