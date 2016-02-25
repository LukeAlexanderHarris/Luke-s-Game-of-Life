package gameOfLife;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 * @author LAH
 *
 */
public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	static String title = "Game of Life";
	private boolean running = false;
	private Thread thread;
	private JFrame frame;
	private GameLogic grid;
	private int cell = 7;          // The size in pixels across a cell and the gap between each cell.						   
	private int gridPixels = 2;    // The space in pixels between each cell.
	private int evolveTime = 100;  // The time in Milliseconds for each evolution.					       
	private int chance = 33;       // The chance % any cell will start alive.
	private int gridLimit = 250;   // The number of evolution steps until the grid
								   // is reset and randomized.

	// Get the display size for resizable frame and grid up to full screen.
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int width = (int) screenSize.getWidth();
	static int height = (int) screenSize.getHeight();

	public Main() {
		setPreferredSize(new Dimension(width/2, height/2)); // initial display size depending on screen size
		frame = new JFrame();
		grid = new GameLogic(chance, cell, gridPixels, gridLimit);    // initialize
																			// game
																			// grid
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		requestFocus();
		long lastTime = 0;
		while (running) {
			long time = System.currentTimeMillis();
			if (time - lastTime >= evolveTime) { //Update time of each generation evolution
				lastTime = time;
				update();
				render();
			}
		}
		stop();
	}

	public void update() {
		grid.update();
	}

	// Render grid 
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		grid.render(g);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.frame.setResizable(true); //Resizable up to full screen 
		main.frame.setTitle(title);
		main.frame.add(main);
		main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.frame.setVisible(true);
		main.frame.requestFocus();
		main.frame.pack();
		main.start();
	}
}
