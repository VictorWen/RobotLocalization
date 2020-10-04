package Graphics;

import java.util.ArrayList;

/**
 * 
 * @author J. Kent Wirant
 * @Version June 10, 2019
 * @see Drawable, Display
 */
public class Renderer implements Runnable {

	private Display display;
	private ArrayList<Drawable> renderQueue;
	private ArrayList<Drawable> tempQueue;
	private boolean shouldRun = false;
	private double frameRate;
	private Object queueLock;
	
	public Renderer(Display display, double frameRate) {
		this.display = display;
		this.frameRate = frameRate;
		tempQueue = new ArrayList<Drawable>();
		queueLock = new Object();
	}
	
	@Override
	public void run() {
		double framePeriod = 1e9 / frameRate;
		long milliseconds = (int) (framePeriod / 1e7);
		int nanoseconds = (int) (framePeriod - 1e7 * milliseconds) / 10;
		long before, now, elapsed;
		
		shouldRun = true;
		now = 0;
		before = 0;
		elapsed = 0;
		
		while(shouldRun) {
			now = System.nanoTime();
			elapsed += now - before;
			before = now;
			
			if(elapsed > framePeriod) {
				elapsed = 0;
				
				synchronized(queueLock) {
					renderQueue = tempQueue;
				}
				
				for(Drawable d : renderQueue) {
					synchronized(d) {
						d.draw(this);
					}
				}
				
				display.drawImage();
			} else {
				try {
					Thread.sleep(milliseconds, nanoseconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<Drawable> getQueue() {
		synchronized(queueLock) {
			ArrayList<Drawable> copy = new ArrayList<Drawable>(renderQueue.size());
			copy.addAll(renderQueue);
			return copy;
		}
	}
	
	public synchronized void setQueue(ArrayList<Drawable> queue) {
		synchronized(queueLock) {
			tempQueue = new ArrayList<Drawable>(queue.size());
			tempQueue.addAll(queue);
		}
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public void setDisplay(Display display) {
		this.display = display;
	}
	
	public boolean shouldRun() {
		return shouldRun;
	}
	
	public void setShouldRun(boolean shouldRun) {
		this.shouldRun = shouldRun;
	}

}
