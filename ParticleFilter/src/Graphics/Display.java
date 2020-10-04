package Graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

/**
 * A window with rendering capabilities. It consists of a JFrame, java.awt.Canvas, and
 * BufferedImage. The BufferedImage is manipulated to change the appearance of the
 * display, and the appearance is updated by the drawImage() method.
 * 
 * @author J. Kent Wirant
 * @Version June 10, 2019
 */
public class Display extends JFrame {

	/** The serial ID */
	private static final long serialVersionUID = -8472134406504992587L;
	/** Where the rendered image is drawn */
	private Canvas screen;
	/** The image that is updated and drawn to the display */
	private BufferedImage image;
	
	/**
	 * Creates a new Display object with the specified width and height. Note that the
	 * display must be set to visible after instantiation.
	 * @param width the width in pixels of the display
	 * @param height the height in pixels of the display
	 */
	public Display(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		screen = new Canvas();
		screen.setSize(width, height);
		add(screen, BorderLayout.CENTER);
		pack();
		setTitle("Display");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		screen.createBufferStrategy(2);
	}
	
	/**
	 * Draws the buffered image onto the display using double buffering.
	 */
	public void drawImage() {
		BufferStrategy bufStrat = screen.getBufferStrategy();
		Graphics g = bufStrat.getDrawGraphics();
		g.drawImage(image, 0, 0, screen.getWidth(), screen.getHeight(), null);
		bufStrat.show();
		g.dispose();
	}

	public Canvas getScreen() {
		return screen;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * @return a shallow copy of the BufferedImage pixels
	 */
	public int[] getPixels() {
		return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

}
