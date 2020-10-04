package ParticleFilter;

/**
 * AP CmpSci 2018-2019
 * Robot Localization
 * Simulates the localization and pathfinding of a robot in a generated world.
 * 
 * @author Victor Wen, Aryan Singh, Alec Benton
 * @version May and June 2019
 */
public class WeightedDistribution {

	/**
	 * An array to hold a weighted distribution.
	 */
	private double[] distribution;

	/**
	 * Weighted distribution construtctor that is made with a Particle array.
	 * @param p The particle array to construct a weighted distribution
	 */
	public WeightedDistribution(Particle[] p) {
		distribution = new double[p.length];
		int sum = 0;
		for (int i = 0; i < p.length; i++) {
			if (p[i].getWeight() > 0)
				sum += p[i].getWeight();
			distribution[i] = sum;
		}

		for (int i = 0; i < distribution.length; i++) {
			distribution[i] /= sum;
			// System.out.println(distribution[i]);
		}
	}

	/**
	 * Picks from a weighted distribution.
	 * @return The index of the particle to be picked
	 */
	public int pick() {
		double rand = Math.random();
		if (distribution[distribution.length - 1] != 1.0)
			return -1;
		int i = 0;
		while (rand >= distribution[i]) {
			i++;
		}
		return i;
	}

}
