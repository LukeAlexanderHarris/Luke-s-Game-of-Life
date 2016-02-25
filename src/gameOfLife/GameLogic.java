package gameOfLife;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * @author LAH
 *
 */
public class GameLogic {
	private int cell, gap, shift, chance, gridLimit, evolve;
	int gridWidth;
	double gridHeight;
	private int[][] grid = new int[gridWidth][(int) gridHeight];
	private int[][] gridNext = new int[gridWidth][(int) gridHeight];
	private Color colorGrid, colorIsAlive;
	private Random rand = new Random();

	public GameLogic(int c, int cPix, int g, int gLife) {
		evolve = 0;
		chance = c;
		cell = cPix;
		gridLimit = gLife;
		gridWidth = Main.width / cell;
		gridHeight = Main.height / cell;
		grid = new int[gridWidth][(int) gridHeight];
		gridNext = new int[gridWidth][(int) gridHeight];
		gap = g;  //Gap around living cell to create grid graphics impression
		shift = gap / 2;
	}


	/**
	 * Update grid each evolution step
	 * Once the set grid evolution limit 
	 * is reached initiate and randomize grid
	 */
	public void update() {
		calculateNext();
		setNext();
		Colors();
		if (evolve % gridLimit == 0)
			randomizeGrid(); // Reset grid randomization
		evolve++;
	}

	public void Colors() {
		colorGrid = new Color(105, 105, 105); // Color grid and Dead cells
		colorIsAlive = new Color(255, 250, 105); // Cell color when alive

	}

	/**
	 * Application of the Game of Life rules on all cells dead or alive
	 * 
	 * Scenario: Underpopulation Given a game of life When a live cell has fewer
	 * than two neighbours Then this cell dies. 
	 * 
	 * Scenario: Overcrowding Given a game of life When a live cell has more
	 * than three neighbours Then this cell dies
	 * 
	 * Scenario: Survival Given a game of life When a live cell has two or three
	 * neighbours Then this cell stays alive.
	 * 
	 * Scenario: Creation of Life Given a game of life When a dead cell has
	 * exactly three neighbours Then this cell becomes alive
	 * 
	 * Scenario: No live cells Given a game of life When the initial state
	 * consists of all dead cells Then the next state is also all dead cells
	 */

	public void calculateNext() {
		int neighbors;
		for (int i = 0; i < gridWidth; i++)
			for (int j = 0; j < gridHeight; j++) {
				neighbors = getNeighbors(i, j);
				if (neighbors == 3 || (neighbors == 2 && grid[i][j] > 0))
					gridNext[i][j] = 1; // Cell stays alive or is Born
				else
					gridNext[i][j] = 0; //Cell stays dead or is was alive it dies
			}
	}

	/**
	 * @param Check around all cells 
	 * @return Number of living neighbor cells
	 */
	public int getNeighbors(int x, int y) {
		int num = 0; // Neighbor counter
		x += gridWidth;
		y += gridHeight;
		if (grid[(x + 1) % gridWidth][(int) (y % gridHeight)] > 0)
			num++;
		if (grid[(x - 1) % gridWidth][(int) (y % gridHeight)] > 0)
			num++;
		if (grid[x % gridWidth][(int) ((y + 1) % gridHeight)] > 0)
			num++;
		if (grid[x % gridWidth][(int) ((y - 1) % gridHeight)] > 0)
			num++;
		if (grid[(x + 1) % gridWidth][(int) ((y + 1) % gridHeight)] > 0)
			num++;
		if (grid[(x - 1) % gridWidth][(int) ((y - 1) % gridHeight)] > 0)
			num++;
		if (grid[(x + 1) % gridWidth][(int) ((y - 1) % gridHeight)] > 0)
			num++;
		if (grid[(x - 1) % gridWidth][(int) ((y + 1) % gridHeight)] > 0)
			num++;
		return num; // returns the current number of living neighbors
	}

	// Set up next grid
	public void setNext() {
		for (int x = 0; x < gridWidth; x++)
			for (int y = 0; y < gridHeight; y++) {
				grid[x][y] = gridNext[x][y];
			}
	}

	public void render(Graphics g) {
		g.setColor(colorGrid);
		g.fillRect(0, 0, Main.width, Main.height);
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (grid[x][y] != 0) {
					g.setColor(colorIsAlive);
					g.fillRect(x * cell + shift, y * cell + shift, cell - gap, cell - gap); // Colors Living cells 
					        																// minus the gap for grid impression
				}
			}
		}
	}

	// Randomization of the initial grid cells with chance % of a cell living
	private void randomizeGrid() {
		for (int x = 0; x < gridWidth; x++)
			for (int y = 0; y < gridHeight; y++) {
				if (rand.nextInt(100) < chance)
					grid[x][y] = 1;
				else
					grid[x][y] = 0;
			}
	}

}