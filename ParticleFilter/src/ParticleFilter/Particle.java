package ParticleFilter;

import Cartography.*;

/**
 * AP CmpSci 2018-2019
 * Robot Localization
 * Simulates the localization and pathfinding of a robot in a generated world.
 * 
 * @author Victor Wen, Aryan Singh, Alec Benton
 * @version May and June 2019
 */
public class Particle extends Point{

	/**
	 * The weight of the assigned particle.
	 */
	private double weight;
	
	/**
	 * Constructor of a particle given its position.
	 * @param x The x position of the particle
	 * @param y The y position of the particle
	 */
	public Particle(int x, int y) {
		super(x, y);
		weight = 0;
	}
	
	/**
	 * Contructor of a particle given a point.
	 * @param p The point to represent particle position
	 */
	public Particle(Point p) {
		super(p);
		weight = 0;
	}
	
	/**
	 * Constructor of a particle given a parent point.
	 * @param p The particle to make a new particle from
	 */
	public Particle(Particle p) {
		super(p.x, p.y);
		weight = p.weight;
	}
	
	/**
	 * Gets the weight of a particle.
	 * @return The weight of the particle
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Sets the weight of a particle.
	 * @param weight The value to set the particle weight to
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Returns the data of the surroundings of points in a map.
	 * @param map The map to check
	 * @return The number of points around a point
	 */
	public int sense(Map map) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (map.contains(x + j, y + i))
					count += map.getPoint(x + j, y + i);
			}
		}
		return count;
	}
}