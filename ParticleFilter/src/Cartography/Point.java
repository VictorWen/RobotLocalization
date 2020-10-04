package Cartography;

public class Point {
	
	/**
	 * Filed variables for x and y coordinates. 
	 */
	protected int x, y;
	
	/**
	 * Constructor for a point object.
	 * @param x X coordinate of the point
	 * @param y Y coordinate of the point
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor for the point object
	 * @param p Point that is being copied
	 */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	/**
	 * Gets the X coordinate of the point.
	 * @return Returns the X coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the Y coordinate of the point.
	 * @return Returns the Y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the X coordinate of the point
	 * @param x The new X coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the Y coordinate of the point
	 * @param Y The new Y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Adds an amount to an x coordinate
	 * @param x Amount being added
	 */
	public void addX(int x) {
		this.x += x;
	}
	
	/**
	 * Adds an amount to an Y coordinate
	 * @param y Amount being added
	 */
	public void addY(int y) {
		this.y += y;
	}
	
	/**
	 * Moves the point a specified amount
	 * @param p Point
	 */
	public void move(Point p) {
		this.x += p.getX();
		this.y += p.getY();
	}
	
	/**
	 * Moves the point by a specified X and Y
	 * @param x Shift in the X coordinate
	 * @param y shift in the Y coordinate
	 */
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Finds the distance between two points
	 * @param p Point
	 * @return Returns the distance
	 */
	public double distance(Point p) {
		return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}