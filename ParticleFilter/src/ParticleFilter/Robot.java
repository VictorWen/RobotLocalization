package ParticleFilter;

import Cartography.Map;
import Cartography.Point;

/**
 * AP CmpSci 2018-2019
 * Robot Localization
 * Simulates the localization and pathfinding of a robot in a generated world.
 * 
 * @author Victor Wen, Aryan Singh, Alec Benton
 * @version May and June 2019
 */
public class Robot extends Point {

	/**
	 * Map that represents the robots environment.
	 */
	private Map state;
	
	/**
	 * Starting position of the robot.
	 */
	private int sx, sy;

	/**
	 * Robot constructor with location coordinates and map.
	 * @param x The x coordinate of the robots location
	 * @param y The y coordinate of the robot location
	 * @param map The map the robot is placed in
	 */
	public Robot(int x, int y, Map map) {
		super(x, y);
		state = new Map(map.getWidth() + 2, map.getHeight() + 2, -1);
		sx = map.getWidth() / 2;
		sy = map.getHeight() / 2;
//		int[][] data = sense(map);

//		for (int i = 0; i < data.length; i++) {
//			for (int j = 0; j < data[0].length; j++) {
//				System.out.print(data[i][j]);
//			}
//			System.out.println();
//		}
		state.setArea(sx - 1, sy - 1, sense(map));
	}

	/**
	 * Robot constructor with starting point and map.
	 * @param p The point to start at
	 * @param map The map the robot is placed in
	 */
	public Robot(Point p, Map map) {
		this(p.getX(), p.getY(), map);
	}

	/**
	 * Returns the data of the surroundings of points in a map.
	 * @param map The map to check
	 * @return The number of points around a point
	 */
	public int[][] sense(Map map) {
		int[][] data = new int[3][3];
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (map.contains(x + j, y + i))
					data[i + 1][j + 1] = map.getPoint(x + j, y + i);
				else
					data[i + 1][j + 1] = -1;
			}
		}
		return data;
	}

	/**
	 * Returns the data of the surroundings of points in a map.
	 * @param map The map to check
	 * @return The number of points around a point
	 */
	public double simpleSense(Map map) {
		int count = 0;

//		double[][] kernel = {{ 9.0 / 207.0, 12.0 / 207.0, 9.0 / 207.0 }, { 12.0 / 207.0, 15.0 / 207.0, 12.0 / 207.0 },
//				{ 9.0 / 207.0, 12.0 / 207.0, 9.0 / 207.0 }};

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (map.contains(x + j, y + i))
					count += map.getPoint(x + j, y + i);
			}
		}

		double noise = (int) (Math.random() * 2 - 1);
		return count + noise;
	}

	/**
	 * Moves the robot a certain points distance.
	 * @param p The point to move the robot by
	 */
	@Override
	public void move(Point p) {
		this.x += p.getX();
		this.y += p.getY();
		this.sx += p.getX();
		this.sy += p.getY();
	}

	/**
	 * Moves the robot by a certain x and y value.
	 * @param x The x value to move the robot by
	 * @param y The y value to move the robot by
	 */
	@Override
	public void move(int x, int y) {
		this.x += x;
		this.y += y;
		this.sx += x;
		this.sy += y;
	}

	/**
	 * Walks the robot in a random direction by a random amount.
	 * @param f The filter for any noise in walking the robot
	 * @param map The map the robot is in
	 */
	public void walk(Filter f, Map map) {
		int dx = 0;
		int dy = 0;
		do {

			int dir = (int) (Math.random() + 0.5);
			dx = 0;
			dy = 0;
			if (dir == 1)
				dx = (int) (Math.random() * 3) - 1;
			else
				dy = (int) (Math.random() * 3) - 1;

		} while (map.getPoint(x + dx, y + dy) != 0 || !map.contains(x + dx, y + dy));

		update(dx, dy, f, map);
	}

	/**
	 * Updates the robot position.
	 * @param dx The change in x for the robot
	 * @param dy The change in y for the robot
	 * @param f The filter for any noise in updating the robot
	 * @param map The map the robot is in
	 */
	public void update(int dx, int dy, Filter f, Map map) {
		move(dx, dy);
		f.shift(dx, dy, map);
		int[][] data = sense(map);

		// Test for shift
		if (sx == 0 || sx == state.getWidth() - 1 || sy == 0 || sy == state.getHeight() - 1) {
			state.shift(dx, dy, -1);
			sx -= dx;
			sy -= dy;
		}

		state.setArea(sx - 1, sy - 1, data);

	}

	/**
	 * Returns the state of the robot.
	 * @return The map representing the robot state
	 */
	public Map getState() {
		return state;
	}

	/**
	 * Creates a point at the robot location.
	 * @return A point at a starting location
	 */
	public Point getSLoc() {
		return new Point(sx, sy);
	}

}