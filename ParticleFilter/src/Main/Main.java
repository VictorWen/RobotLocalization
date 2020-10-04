package Main;

import Cartography.*;
import ParticleFilter.*;

public class Main {

	private static Filter filter;
	private static Robot rob;
	private static Map map;

	public static void main(String[] args) {
		map = new Map(10, 10);
		map.display();
		System.out.println();
		rob = new Robot(map.randomOpen(0), map);
		filter = new Filter(100, map, rob);
		filter.getParticleMap(map).display();
		System.out.println();
		Map m = new Map(map);
		m.setPoint(rob, -1);
		m.display();
		System.out.println();
//		run(0.95, 1, 3);
		testRun(20, 20, 10000, 100, 0.95, 1, 15, 0);
	}

	/**
	 * Runs the particle filter
	 * @param threshold How confident the particle filter must be to end
	 * @param walkTime How often the filter mutates the particles
	 * @param waitTime Amount of time the filter must remain confident
	 */
	public static void run(double threshold, int walkTime, int waitTime) {
		int count = 0;
		int wait = 0;
		while (wait < waitTime) {
			if (count + 1 % walkTime == 0) {
				filter.walk(map);
			}
			rob.walk(filter, map);
			filter.update(map);

			if (count % 1 == 0) {
				System.out.println(count + " " + filter.getPrediction() + " " + filter.getError() + " "
						+ filter.getConfidence() + " " + rob);
				System.out.println();
				filter.getParticleMap(map).display();
				System.out.println();
				Map m = new Map(map);
				m.setPoint(rob, -1);
				m.display();
				System.out.println();
			}

			if (filter.getConfidence() >= threshold) {
				wait++;
			} else if (wait > 0) {
				wait = 0;
			}

			count++;
		}
		System.out.println(count - 1 + " " + filter.getPrediction() + " " + filter.getError() + " "
				+ filter.getConfidence() + " " + rob);
	}

	/**
	 * Tests the run method
	 * @param width Width of the map
	 * @param height Height of the map
	 * @param runs How many times it runs
	 * @param particles Number of particles
	 * @param threshold How confident the particle filter must be to end
	 * @param walkTime How often the filter mutates the particles
	 * @param waitTime Amount of time the filter must remain confident
	 * @param accurate How close the predicted value must be to be considered correct
	 */
	public static void testRun(int width, int height, int runs, int particles, double threshold, int walktime,
			int waitTime, double accurate) {
		double error = 0;
		int correct = 0;
		for (int i = 0; i < runs; i++) {
			map = new Map(width, height);
			rob = new Robot(map.randomOpen(0), map);
			filter = new Filter(particles, map, rob);
			int count = 0;
			int wait = 0;
			while (wait < waitTime) {
				if (count + 1 % walktime == 0) {
					filter.walk(map);
				}
				rob.walk(filter, map);

				filter.update(map);

				if (filter.getConfidence() >= threshold) {
					wait++;
				} else if (wait > 0) {
					wait = 0;
				}

				count++;
			}
			System.out.println(i + 1 + ". " + filter.getError());
			error += filter.getError();
			if (filter.getError() <= accurate)
				correct++;
		}
		System.out.println("Percent correct: " + (double) correct / runs * 100 + "%");
		System.out.println("Error: " + error / runs);
	}

	/**
	 * Displays an array as a coordinate grid
	 * @param arr 2D array being displayed
	 */
	public static void display(double[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				System.out.printf("%1.2f", arr[i][j]);
			}
			System.out.println();
		}
	}
}
