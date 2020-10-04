package Graphics;

import java.util.Arrays;

/**
 * @author J. Kent Wirant
 * @Version June 10, 2019
 */
public class PolygonDrawable implements Drawable {

	private double[][] points;
	private double[] origin;
	private int[] pixels;
	private int color;
	private int boundLength;
	private Object pointsLock;
	private Object pixelsLock;
	
	public PolygonDrawable(double[][] points, double[] origin, int color) {
		this(points, origin, color, -1);
	}
	
	public PolygonDrawable(double[][] points, double[] origin, int color, int boundsSize) {
		this.points = ArrayUtility.deepCopy(points);
		this.origin = Arrays.copyOf(origin, origin.length);
		this.color = color;
		pointsLock = new Object();
		pixelsLock = new Object();
		
		if(boundsSize > 0) {
			generateBounds(boundsSize);
		} else {
			generateBounds();
		}
		
		generatePixels();
	}
	
	public void generateBounds(int size) {
		synchronized(pixelsLock) {
			boundLength = size;
			pixels = new int[size * size];
		}
	}
	
	public void generateBounds() {
		double diffX, diffY, maxValue = 0, value;
		int maxIndex = 0;
		boolean updated = true;
		
		synchronized(pointsLock) {
			while(updated) {
				updated = false;
				
				for(int i = 0; i < points.length; i++) {
					diffX = points[maxIndex][0] - points[i][0];
					diffY = points[maxIndex][1] - points[i][1];
					value = diffX * diffX + diffY * diffY;
					
					if(value > maxValue + 1e-6) {
						maxValue = value;
						maxIndex = i;
						updated = true;
						break;
					}
				}
			}
		}
		
		synchronized(pixelsLock) {
			boundLength = (int) Math.sqrt(maxValue) + 5;
			pixels = new int[boundLength * boundLength];
		}
	}
	
	public void generatePixels() {
		synchronized(pointsLock) {
			double shiftX = points[0][0], shiftY = points[0][1];
			int[] intersections = new int[points.length];
			int tempValue = 0, tempIndex = 0, x;
			
			for(int i = 1; i < points.length; i++) {
				if(points[i][0] < shiftX) {
					shiftX = points[i][0];
				}
				
				if(points[i][1] < shiftY) {
					shiftY = points[i][1];
				}
			}
			
			synchronized(pixelsLock) {
				for(int y = 0; y < boundLength; y++) {
					for(int i = 0; i < intersections.length; i++) {
						tempIndex = (i + 1) % points.length;
		
						if(y > points[i][1] - shiftY && y < points[tempIndex][1] - shiftY
								|| y < points[i][1] - shiftY && y > points[tempIndex][1] - shiftY) {
							intersections[i] = (int) ((y - points[i][1] + shiftY) * (points[i][0] - points[tempIndex][0])
									/ (points[i][1] - points[tempIndex][1]) + points[i][0] - shiftX + 0.5);
						} else {
							intersections[i] = -1;
						}
					}
					
					x = 0;
					tempValue = -1;
					Arrays.sort(intersections);
					
					for(int i = 0; i < intersections.length; i++) {
						if(intersections[i] != -1) {
							while(x < intersections[i]) {
								pixels[boundLength * y + x] = tempValue;
								x++;
							}
							
							if(tempValue == -1) {
								tempValue = color;
							} else {
								tempValue = -1;
							}
						}
					}
					
					while(x < boundLength) {
						pixels[boundLength * y + x] = -1;
						x++;
					}
				}
			}
		}
	}
	
	@Override
	public void draw(Renderer renderer) {
		Display disp = renderer.getDisplay();
		int[] rendPixels = disp.getPixels();
		int rendWidth = disp.getImage().getWidth();
		int rendHeight = disp.getImage().getHeight();
		
		synchronized(pointsLock) {
			int minX = (int) points[0][0];
			int minY = (int) points[0][1];
			int maxX = (int) points[0][0];
			int maxY = (int) points[0][1];
			int value;
			
			for(int i = 1; i < points.length; i++) {
				if(points[i][0] < minX) {
					minX = (int) points[i][0];
				} else if(points[i][0] > maxX) {
					maxX = (int) points[i][0];
				}
				
				if(points[i][1] < minY) {
					minY = (int) points[i][1];
				} else if(points[i][1] > maxY) {
					maxY = (int) points[i][1];
				}
			}
			
			minX += (int) origin[0];
			minY += (int) origin[1];
			maxX += (int) origin[0];
			maxY += (int) origin[1];
			
			minX = Math.max(minX, 0);
			minY = Math.max(minY, 0);
			maxX = Math.min(maxX, rendWidth);
			maxY = Math.min(maxY, rendHeight);
			
			synchronized(pixelsLock) {
				for(int y = minY; y < maxY; y++) {
					for(int x = minX; x < maxX; x++) {
						value = pixels[boundLength * (y - minY) + x - minX];
						
						if(value != -1) {
							rendPixels[rendWidth * y + x] = value;
						}
					}	
				}
			}
		}
	}
	
	public double[][] getPoints() {
		return ArrayUtility.deepCopy(points);
	}
	
	public void setPoints(double[][] points) {
		synchronized(pointsLock) {
			this.points = ArrayUtility.deepCopy(points);
		}
	}
	
	public double[] getOrigin() {
		return Arrays.copyOf(origin, origin.length);
	}
	
	public void setOrigin(double[] origin) {
		synchronized(pointsLock) {
			this.origin = Arrays.copyOf(origin, origin.length);
		}
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
}
