package freezeframe;

public class Position {
	
	
	private double x, y;
	
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a copy of another position
	 * @param other
	 */
	public Position(Position other) {
		x = other.getX();
		y = other.getY();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public boolean equals(Position other) {
		return x == other.getX() && y == other.getY();
	}

}
