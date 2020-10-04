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
public class Filter {

	/**
	 * The array of particles in a particle filter.
	 */
	private Particle[] particles;
	
	/**
	 * The robot to move in the simulation.
	 */
	private Robot bot;
	
	/**
	 * The prediction of where the robot is located.
	 */
	private Point prediction;
	
	/**
	 * The certainty of the particle filter in its position.
	 */
	private double confidence;

	/**
	 * Filter constructor with number of particles, robot, and map.
	 * @param numParticles The number of particles in the filter
	 * @param map The map that the robot is in
	 * @param bot The robot to move to a location
	 */
	public Filter(int numParticles, Map map, Robot bot) {
		this.particles = new Particle[numParticles];
		for (int i = 0; i < numParticles; i++) {
			Point open = map.randomOpen(0);
			particles[i] = new Particle(open.getX(), open.getY());
		}

		this.bot = bot;
		this.prediction = calcWeightedMean();
		this.confidence = 0;
	}

	/**
	 * Updates the position of particles in a map.
	 * @param map The map that the robot is in
	 */
	// Run after bot has moved
	public void update(Map map) {
		// Update weights

		for (int i = 0; i < particles.length; i++) {
			double weight = Math.pow(Math.E,
					-(Math.pow(1 * (particles[i].sense(map) - bot.simpleSense(map)), 2) / (2 * Math.pow(0.9, 2))));
			/*
			 * bot .getState().compare(bot.getSLoc(), particles[i], map, -1)
			 */
			if (map.getPoint(particles[i]) == 1)
				weight = 0;
			particles[i].setWeight(weight);
		}

		// Replace bad particles

		Particle[] newParticles = new Particle[particles.length];
		WeightedDistribution w = new WeightedDistribution(particles);
		for (int i = 0; i < particles.length; i++) {
			int pick = w.pick();
			if (pick == -1) {
				newParticles[i] = new Particle(map.randomOpen(0));
			} else {
				newParticles[i] = new Particle(particles[w.pick()]);
			}
		}
		particles = newParticles;

		// Calculate prediction and confidence

		prediction = calcWeightedMean();

		int count = 0;
		for (Particle p : particles) {
			if (p.distance(prediction) <= 1)
				count++;
		}
		confidence = (double) count / particles.length;
	}

	/**
	 * Moves each particle in the filter based on robot surroudings.
	 * @param map The map the robot is in
	 */
	public void walk(Map map) {
		for (int i = 0; i < particles.length; i++) {
			if (particles[i].sense(map) - bot.simpleSense(map) >= 1) {
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

				} while (!(map.contains(particles[i].getX() + dx, particles[i].getY() + dy)
						&& map.getPoint(particles[i].getX() + dx, particles[i].getY() + dy) == 0));

				particles[i].move(dx, dy);
			}
		}
	}

	/**
	 * Calculates a weighted mean given each particle weight.
	 * @return The weighted mean of the particle filter
	 */
	public Point calcWeightedMean() {
		double x = 0;
		double y = 0;
		double sum = 0;

		for (Particle p : particles) {
			x += p.getWeight() * p.getX();
			y += p.getWeight() * p.getY();
			sum += p.getWeight();
		}

		return new Point((int) (x / sum + 0.5), (int) (y / sum + 0.5));
	}

	/**
	 * Gets the prediction of the particle filter.
	 * @return The particle filter prediction
	 */
	public Point getPrediction() {
		return prediction;
	}

	/**
	 * Gets the error in the filter prediction.
	 * @return The error in the filter prediction
	 */
	public double getError() {
		return bot.distance(prediction);
	}

	/**
	 * Gets the confidence of the filter prediction.
	 * @return The confidence of the filter prediction
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * Generates a map of particles.
	 * @param m The map that the robot is in
	 * @return The map of particles
	 */
	public Map getParticleMap(Map m) {
		Map map = new Map(m.getWidth(), m.getHeight(), 0);
		for (Particle p : particles) {
			map.setPoint(p, map.getPoint(p) + 1);
		}
		return map;
	}

	/**
	 * Shifts the position of a particle.
	 * @param dx The change in x in particle position
	 * @param dy The change in y in particle position
	 * @param map The map the robot is in
	 */
	public void shift(int dx, int dy, Map map) {
		for (Particle p : particles) {
			if (map.contains(p.getX() + dx, p.getY() + dy)) {
				p.addX(dx);
				p.addY(dy);
			} else
				p.setWeight(0);
		}
	}

}
