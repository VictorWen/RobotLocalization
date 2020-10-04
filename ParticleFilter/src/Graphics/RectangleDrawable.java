package Graphics;

/**
 * @author J. Kent Wirant
 * @Version June 10, 2019
 */
public class RectangleDrawable implements Drawable {

	private double[][] points; //the two opposite points
	private int color;
	private Object pointsLock; //used for synchronizing updates to points
	
	public RectangleDrawable(double[][] points, int color) {
		this.points = ArrayUtility.deepCopy(points);
		this.color = color;
		pointsLock = new Object();
	}
	
	@Override
	public void draw(Renderer renderer) {
		Display disp = renderer.getDisplay();
		int[] rendPixels = disp.getPixels();
		int rendWidth = disp.getImage().getWidth();
		int rendHeight = disp.getImage().getHeight();
		int minX, minY, maxX, maxY;
		
		synchronized(pointsLock) {
			minX = (int) Math.max(Math.min(points[0][0], points[1][0]), 0);
			minY = (int) Math.max(Math.min(points[0][1], points[1][1]), 0);
			maxX = (int) Math.min(Math.max(points[0][0], points[1][0]), rendWidth);
			maxY = (int) Math.min(Math.max(points[0][1], points[1][1]), rendHeight);
		}
		
		for(int y = minY; y < maxY; y++) {
			for(int x = minX; x < maxX; x++) {
				rendPixels[rendWidth * y + x] = color;
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
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
}
