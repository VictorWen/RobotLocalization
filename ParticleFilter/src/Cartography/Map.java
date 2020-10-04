package Cartography;

import Graphics.Display;
import Graphics.Drawable;
import Graphics.Renderer;

/**
 * Class that represents a map of ones and zeros.
 * 
 * @author Victor Wen, Alec Benton, Aryan Singh
 *
 */
public class Map implements Drawable {

	/**
	 * Width of the map
	 */
	private final int WIDTH;

	/**
	 * Height of the map
	 */
	private final int HEIGHT;

	/**
	 * 2d Array that represents a map
	 */
	protected int[][] data;

	private final int gridSize = 3;
	
	private Map particles;

	/**
	 * Constructor to create a map object of all zeros.
	 * 
	 * @param width  The width of the map
	 * @param height The height of the map
	 */
	public Map(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		randomizeMap(WIDTH / 10);
	}

	/**
	 * Constructor that creates a map object and fills it with a specified number.
	 * 
	 * @param width  Width of the map
	 * @param height Height of the map
	 * @param fill   Number that the map is being filled with
	 */
	public Map(int width, int height, int fill) {
		this.WIDTH = width;
		this.HEIGHT = height;
		data = new int[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				data[y][x] = fill;
			}
		}
	}

	/**
	 * Copy constructor for a map object.
	 * 
	 * @param map Map object that is being copied
	 */
	public Map(Map map) {
		this.WIDTH = map.WIDTH;
		this.HEIGHT = map.HEIGHT;
		data = new int[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				data[y][x] = map.getPoint(x, y);
			}
		}
	}

	/**
	 * Takes in the data field variable and creates a copy.
	 * 
	 * @param data 2d array being copied
	 */
	public Map(int[][] data) {
		this.WIDTH = data[0].length;
		this.HEIGHT = data.length;
		this.data = data;
	}

	/**
	 * Creates a random map of ones and zeros
	 * 
	 * @param paths Number of times the loop runs
	 */
	public void randomizeMap(int paths) {
		data = new int[HEIGHT][WIDTH];

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				data[y][x] = 1;
			}
		}

		for (int i = 0; i < paths; i++) {
			int y = HEIGHT / 2;
			int x = WIDTH / 2;
//			int count = 0;
			while (i > 0 && data[y][x] != 0) {
				y = (int) (Math.random() * HEIGHT);
				x = (int) (Math.random() * WIDTH);
//				count++;
//				if (count > 400) {
//					System.out.println(y + " " + x);
//					display();
//				}
			}
			while (y > 0 && y < HEIGHT - 1 && x > 0 && x < WIDTH - 1) {
				data[y][x] = 0;
				int dir = (int) (Math.random() + 0.5);
				int dr = 0;
				int dc = 0;
				if (dir == 1)
					dr = (int) (Math.random() * 3) - 1;
				else
					dc = (int) (Math.random() * 3) - 1;
				y += dr;
				x += dc;
			}
		}
	}

	/**
	 * Displays the map as a grid
	 */
	public void display() {
		System.out.print("    ");
		for (int c = 0; c < WIDTH; c++) {
			System.out.printf("[" + String.format("%2s", c) + "]");
		}
		System.out.println();
		for (int r = 0; r < HEIGHT; r++) {
			System.out.print("[" + String.format("%2s", r) + "]" + " ");
			for (int c = 0; c < WIDTH; c++) {
				System.out.printf("%-4d", data[r][c]);
			}
			System.out.println();
		}
	}
	
	public void setParticles(Map particles) {
		this.particles = particles;
	}

	public Renderer draw() {
		Display d = new Display(WIDTH * gridSize, HEIGHT * gridSize);
		Renderer r = new Renderer(d, 24);
		draw(r);
		return r;
	}

	@Override
	public void draw(Renderer r) {
		Display disp = r.getDisplay();
		int[] rendPixels = disp.getPixels();
		int rendWidth = disp.getImage().getWidth();
		// int rendHeight = disp.getImage().getHeight();
		

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						//rendPixels[rendWidth * (y * gridSize + i) + x * gridSize + j ] = ;
						int red = 0;
						int green = 0;
						int blue = 0;
						int fill = 0;
						if (data[y][x] == -1) {
							blue = 0xFF;
						}
						if (particles != null && particles.getPoint(x, y) > 0) {
							red = 0xFF;
							//System.out.println(red);
						}
						if (red + green <= 0) {
							fill = (1 - data[y][x]) * 0x00FFFF;
						}
						rendPixels[rendWidth * (y * gridSize + i) + x * gridSize + j ] = mergeColors(red, green, blue) + fill;
					}
				}
			}
		}
	}
	
	public int mergeColors(int r, int g, int b) {
		return 0x010000 * r + 0x000100 * g + 0x000001 * b;
	}
	
	public int mergeColors(int rgb, int rgb2) {
		return (rgb + rgb2) / 2;
	}

	/**
	 * Gets a value at a specified point
	 * 
	 * @param p Point where the value is being gotten.
	 * @return Returns the value at that point
	 */
	public int getPoint(Point p) {
		return data[p.getY()][p.getX()];
	}

	/**
	 * Gets the value at specified coordinates.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Returns the value at the coordinates
	 */
	public int getPoint(int x, int y) {
		return data[y][x];
	}

	/**
	 * Sets a value at specified coordinates.
	 * 
	 * @param x     X coordinate
	 * @param y     Y coordinate
	 * @param value Value that is being set
	 */
	public void setPoint(int x, int y, int value) {
		data[y][x] = value;
	}

	/**
	 * Sets a value at a specified point
	 * 
	 * @param p     Point where value is being set
	 * @param value Value that is being set
	 */
	public void setPoint(Point p, int value) {
		data[p.y][p.x] = value;
	}

	/**
	 * Finds whether or not the point exists in the map.
	 * 
	 * @param p Point
	 * @return Returns true if the point exists, false if not
	 */
	public boolean contains(Point p) {
		return p.x >= 0 && p.x < WIDTH && p.y >= 0 && p.y < HEIGHT;
	}

	/**
	 * Finds whether or not the point of the specifeid coordinates exists in the
	 * map.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Returns true if the point exists, false if not
	 */
	public boolean contains(int x, int y) {
		return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
	}

	/**
	 * Gets the height of the map.
	 * 
	 * @return The height of the map
	 */
	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * Gets the width of the map.
	 * 
	 * @return The width of the map
	 */
	public int getWidth() {
		return WIDTH;
	}

	@Deprecated
	public void setArea(int x, int y, int[][] area) {
		for (int i = 0; i < area.length; i++) {
			for (int j = 0; j < area[0].length; j++) {
				if (contains(x + j, y + i))
					data[y + i][x + j] = area[i][j];
			}
		}
	}

	@Deprecated
	public void shift(int dx, int dy, int blank) {
		int[][] temp = new int[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (contains(x - dx, y - dy) && data[y - dy][x - dx] != blank)
					temp[y][x] = data[y - dy][x - dx];
				else
					temp[y][x] = blank;
			}
		}
		data = temp;
	}

//	public int compare(Map map, int blank) {
//		int h = Math.max(HEIGHT, map.HEIGHT);
//		int w = Math.max(WIDTH, map.WIDTH);
//
//		int count = 0;
//		for (int y = 0; y < h; y++) {
//			for (int x = 0; x < w; x++) {
//				if (contains(x, y) && map.contains(x, y) && data[y][x] != blank && data[y][x] == map.getPoint(x, y))
//					count++;
//			}
//		}
//		return count;
//	}
//
	@Deprecated
	public double compare(Point pivot, Point pivot2, Map map, int blank) {
		int dx = pivot2.x - pivot.x;
		int dy = pivot2.y - pivot.y;
		double error = 0;

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (map.contains(x + dx, y + dy) && data[y][x] != blank) {
					error += (data[y][x] - map.getPoint(x + dx, y + dy));
				}
			}
		}

		// System.out.print(error + " ");
		return error;
	}

	/**
	 * Gets a random open space in the map.
	 * 
	 * @param open What counts as open
	 * @return Returnt
	 */
	public Point randomOpen(int open) {
		int x = 0;
		int y = 0;

		do {
			x = (int) (Math.random() * WIDTH);
			y = (int) (Math.random() * HEIGHT);
		} while (data[y][x] != 0);

		return new Point(x, y);
	}
	
	public int getGridSize() {
		return gridSize;
	}
	
//	public void merge(Map fore) {
//		for (int y = 0; y < fore.HEIGHT; y++) {
//			for (int x = 0; x < fore.WIDTH; x++) {
//				data[y][x] = fore.getPoint(x, y);
//			}
//		}
//	}

//	public double[][] filter() {
//		int k = 2;
//
//		double[][] filtered = new double[HEIGHT][WIDTH];
//		int[][] border = new int[HEIGHT + 2 * k][WIDTH + 2 * k];
//
//		// Copy
//		for (int y = 0; y < HEIGHT; y++) {
//			for (int x = 0; x < WIDTH; x++) {
//				border[y + k][x + k] = data[y][x];
//			}
//		}
//
//		// Reflect top and bottom
//		for (int i = 0; i < k; i++) {
//			for (int x = 0; x < WIDTH; x++) {
//				border[k - i - 1][x + k] = data[i][x];
//				border[HEIGHT + i - 1 + k][x + k] = data[HEIGHT - i - 1][x];
//			}
//
//		}
//
//		// Reflect sides
//		for (int i = 0; i < k; i++) {
//			for (int y = 0; y < HEIGHT + 2 * k; y++) {
//				border[y][k - i - 1] = border[y][i + k];
//				border[y][WIDTH + i - 1 + k] = border[y][WIDTH - i - 1 + k];
//			}
//		}
//
//		double[][] kernel = { { 2.0, 4.0, 5.0, 4.0, 2.0 }, { 4.0, 9.0, 12.0, 9.0, 4.0 }, { 5.0, 12.0, 15.0, 12.0, 5.0 },
//				{ 4.0, 9.0, 12.0, 9.0, 4.0 }, { 2.0, 4.0, 5.0, 4.0, 2.0 } };
//
//		for (int y = 0; y < HEIGHT; y++) {
//			for (int x = 0; x < WIDTH; x++) {
//				double avg = 0;
//				double sum = 0;
//				for (int i = -k; i <= k; i++) {
//					for (int j = -k; j <= k; j++) {
//						if (border[y + i + k][x + j + k] != -1) {
//							avg += kernel[i + k][j + k] * border[y + i + k][x + j + k];
//							sum += kernel[i + k][j + k];
//						}
//					}
//				}
//				if (sum > 0)
//					filtered[y][x] = avg / sum;
//			}
//		}
//
//		return filtered;
//
//	}

}