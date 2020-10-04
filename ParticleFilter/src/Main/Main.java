package Main;

import Cartography.*;
import Graphics.Display;
import Graphics.Renderer;
import ParticleFilter.*;

public class Main {

	private static Filter filter;
	private static Robot rob;
	private static Map map;

	public static void main(String[] args) {
		map = new Map(250, 100);
//		map.display();
//		System.out.println();
		rob = new Robot(map.randomOpen(0), map);
		filter = new Filter(2000, map, rob);
//		Map particles = filter.getParticleMap(map);
//		System.out.println();
//		Map m = new Map(map);
//		map.setPoint(rob, -1);
//		m.display();
//		System.out.println();
////		run(0.95, 1, 3);
//		testRun(20, 20, 10000, 100, 0.95, 1, 15, 0);

//		
//		Display disp = new Display(10 * 10, 10 * 10);
//		Renderer rend = new Renderer(disp, 24);
//		disp.setVisible(true);
//		map.setParticles(particles);
//		Renderer r = map.draw();

		Renderer r = new Renderer(new Display(map.getWidth() * map.getGridSize(), map.getHeight() * map.getGridSize()), 24);
		
		// displayParticlesMap(r, particles);
		r.getDisplay().setVisible(true);
//		Thread t = new Thread(r);
//		t.start();
		run(0.95, 1, 100, r);

		// map.draw(rend);

	}

	public static void displayParticlesMap(Renderer r, Map particles) {
		Display disp = r.getDisplay();
		int[] rendPixels = disp.getPixels();
		int rendWidth = disp.getImage().getWidth();
		// int rendHeight = disp.getImage().getHeight();

		for (int y = 0; y < particles.getHeight(); y++) {
			for (int x = 0; x < particles.getWidth(); x++) {
				for (int i = 0; i < particles.getGridSize(); i++) {
					for (int j = 0; j < particles.getGridSize(); j++) {
						if (particles.getPoint(x, y) > 0)
							rendPixels[rendWidth * (y * particles.getGridSize() + i) + x * particles.getGridSize()
									+ j] = (int) (particles.getPoint(x, y) * 0xFFFF / 100 * Math.pow(16, 5));
					}
				}
			}
		}
	}

	/**
	 * Runs the particle filter
	 * 
	 * @param threshold How confident the particle filter must be to end
	 * @param walkTime  How often the filter mutates the particles
	 * @param waitTime  Amount of time the filter must remain confident
	 */
	public static void run(double threshold, int walkTime, int waitTime, Renderer r) {
		int count = 0;
		int wait = 0;
		while (wait < waitTime) {

			
			rob.walk(filter, map);
			if (count + 1 % walkTime == 0) {
				filter.walk(map);
			}
			filter.update(map);

			if (count % 1 == 0) {
				System.out.println(count + " " + filter.getPrediction() + " " + filter.getError() + " "
						+ filter.getConfidence() + " " + rob);
				System.out.println();
				// filter.getParticleMap(map).display();
				System.out.println();
				Map m = new Map(map);
				m.setPoint(rob, -1);
				// m.display();
				System.out.println();
			}

			if (filter.getConfidence() >= threshold) {
				wait++;
			} else if (wait > 0) {
				wait = 0;
			}

			Map m = new Map(map);
			m.setParticles(filter.getParticleMap(map));
			m.setPoint(rob, -1);
			m.draw(r);
			r.getDisplay().drawImage();
			
			count++;
		}
		System.out.println(count - 1 + " " + filter.getPrediction() + " " + filter.getError() + " "
				+ filter.getConfidence() + " " + rob);
	}

	/**
	 * Tests the run method
	 * 
	 * @param width     Width of the map
	 * @param height    Height of the map
	 * @param runs      How many times it runs
	 * @param particles Number of particles
	 * @param threshold How confident the particle filter must be to end
	 * @param walkTime  How often the filter mutates the particles
	 * @param waitTime  Amount of time the filter must remain confident
	 * @param accurate  How close the predicted value must be to be considered
	 *                  correct
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
	 * 
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
