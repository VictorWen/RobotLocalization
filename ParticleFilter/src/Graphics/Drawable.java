package Graphics;

/**
 * An interface supporting rendering capability. Instances of classes implementing 
 * Drawable are passed into a Renderer as an ArrayList, and each Drawable is then 
 * renderered on the Display.
 * 
 * @author J. Kent Wirant
 * @Version June 10, 2019
 * @see Renderer
 */
public interface Drawable {

	/**
	 * Draws the drawable to the Renderer's Display
	 * @param renderer the renderer that invokes the draw method
	 */
	public void draw(Renderer renderer);
	
}
