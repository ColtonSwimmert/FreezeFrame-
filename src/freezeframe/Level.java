package freezeframe;

import static freezeframe.Tile.TILE_SIZE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import freezeframe.Tile.TileType;
import freezeframe.screens.FFSettingsMenu;
import starter.AudioPlayer;

public class Level {

	private ArrayList<Tile> tileset;
	private ArrayList<PhysicsObject> physicsobjects;
	private File file;
	private boolean isRead;
	private Position spawnpos;
	
	private static AudioPlayer play = AudioPlayer.getInstance();

	public Level() {
		spawnpos = new Position(20,20);
		tileset = new ArrayList<Tile>();
		physicsobjects = new ArrayList<PhysicsObject>();
		isRead = true;
	}

	public Level(File file) {
		isRead = false;
		this.file = file;
	}
	
	private void defaultLevel() {
		try {
			load(new File("level1.level"));
		} catch (IOException e) {
			System.err.println("File load error, unable to load default level");
		}
	}
	private void addbrick(int x, int y) {
		tileset.add(new Tile(TileType.brick, new Position(x, y)));
	}
	
	public void moveTile(Tile tile, Position pos) {
		tile.getPos().setX(pos.getX());
		tile.getPos().setY(pos.getY());
	}
	public void addTile(Tile t) {
		tileset.add(t);
	}
	
	public void setSpawn(Position p) {
		spawnpos = p;
	}

	public static Level genDefaultLevel() {
		Level out = new Level();
		out.defaultLevel();
		return out;
	}

	public void save(File file) throws IOException {
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		write(out, (int) spawnpos.getX());
		write(out, (int) spawnpos.getY());
		for(Tile t: tileset) {
			write(out,       t.getType().toInt());
			write(out, (int) t.getPos().getX());
			write(out, (int) t.getPos().getY());
		}
		out.close();
	}
	
	private void write(FileOutputStream out, int i) throws IOException {
		char digit;
		System.out.println(i + ",");
		for(char c: (i + "").toCharArray()) {
			out.write(c);
		}
		out.write(',');
	}

	// File format: spawnX, spawnY, {type, X, Y}[*]
	public void load(File file) throws IOException {
		if (!file.canRead()) {
			System.err.println("ERROR:\nFile " + file.getPath() + " cannot be read");
			throw new IOException("Unable to read " + file.getAbsolutePath());
		}

		FileReader input = new FileReader(file);

		spawnpos = new Position(readNext(input), readNext(input));
		tileset = new ArrayList<Tile>();
		while(input.ready())
			tileset.add(new Tile(TileType.fromInt(readNext(input)), new Position(readNext(input), readNext(input))));
		
		input.close();
	}

	private int readNext(FileReader reader) throws IOException {
		System.out.print(".");
		char c = 0;
		int out = 0;
		while(reader.ready()) {
			c = (char) reader.read();
			if(c==',')
				break;
			out *= 10;
			out += (c - '0');
		}
		return out;
	}

	public Position getSpawn() {
		checkload();
		return spawnpos;
	}

	public ArrayList<Tile> getTiles() {
		checkload();
		return tileset;
	}
	
	private void checkload() {
		if(!isRead){
			try {
				load(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		isRead = true;
	}

	public Tile getTile(int x, int y) {
		checkload();
		for (Tile t : tileset) {
			if (t.getPos().equals(new Position(x, y)))
				return t;
		}
		return null;
	}
	
	public boolean equals(Level other) {
		return printLevel().equals(other.printLevel());
	}

	public String printLevel() {
		StringBuilder out = new StringBuilder();
		out.append("Player spawn: (" + spawnpos.getX() + "," + spawnpos.getY() + ")\n");
		for (Tile t : tileset) {
			out.append(t);
			out.append("\n");
		}
		return out.toString();
	}

	public void remove(Tile t) {
		tileset.remove(t);
	}
}
