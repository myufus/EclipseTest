import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

/**
 * A city to hold and model the zombie outbreak. Some functionality based on the original simulation, http://kevan.org/proce55ing/zombies/.
 */
public class City {
	// constants
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	public static final int POPULATION = 5000;
	public static final Color ROAD_COLOR = Color.BLACK;
	public static final Color WALL_COLOR = Color.GRAY;

	private BufferedImage map; // hold the drawing of the map

	protected boolean[][] walls; // a two-dimension array of whether a particular pixel is a wall or not
	protected boolean agents[][]; // a two-dimensional array of whether a certain pixel contains an Agent
	protected ArrayList<Agent> agentsList; // a list of all Agents in the City

	private BufferedImage image; // holds the drawing of the whole city (re-used to speed rendering)
	private Graphics2D img2d; // a graphics context of the image that we draw on
	private Random gen = new Random();

	/**
	 * Creates a new City object with walls, people, and a single zombie...
	 */
	public City() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB); // initialize the image canvas thing
		img2d = image.createGraphics();
		generateWalls();
		
		//set up agents location tracker
		agents = new boolean[WIDTH][HEIGHT];
		agentsList = new ArrayList<Agent>();
		for (int i = 0; i < agents.length; i++) {
			for (int j = 0; j < agents[0].length; j++) {
				agents[i][j] = false;
			}
		}
		
		generateAgents();
	}

	/**
	 * A method that creates walls and rooms in the city. In effect initializes the map.
	 */
	public void generateWalls() {
		map = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB); // our current drawing
		Graphics2D mapg2d = map.createGraphics();
		mapg2d.setPaint(ROAD_COLOR);
		mapg2d.fillRect(0, 0, WIDTH, HEIGHT);
		mapg2d.setPaint(WALL_COLOR); // default color
		mapg2d.fillRect(0, 0, WIDTH, HEIGHT); // empty the city

		mapg2d.setPaint(ROAD_COLOR);
		// draw some "rooms"
		for (int i = 0; i < 50; i++)
			mapg2d.fillRect(gen.nextInt(WIDTH), gen.nextInt(HEIGHT), 20 + gen.nextInt(40), 20 + gen.nextInt(20));

		// draw some "hallways"
		for (int i = 0; i < 100; i++)
			mapg2d.drawRect(gen.nextInt(WIDTH), gen.nextInt(HEIGHT), 10 + gen.nextInt(60), 10 + gen.nextInt(60));

		// add a border around the edge, so people don't wander outside
		mapg2d.setPaint(WALL_COLOR);
		mapg2d.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

		walls = new boolean[WIDTH][HEIGHT];

		// go through the image and calculate walls (for speed later)
		int wallRGB = WALL_COLOR.getRGB(); // we use color to determine if there is a wall
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (map.getRGB(i, j) == wallRGB)
					walls[i][j] = true;
			}
		}
	}

	/**
	 * A method that adds Humans and Zombies to the City
	 */
	private void generateAgents() {
		// add humans
		for (int i = 0; i < POPULATION; i++) {
			int x = gen.nextInt(WIDTH);
			int y = gen.nextInt(HEIGHT);
			if (!agents[x][y] && !walls[x][y]) {
				agentsList.add(new Human(x, y, this));
			} else {
				i--;
			}
		}
		
		// add one zombie
		while (agentsList.size() < POPULATION + 1) {
			int x = gen.nextInt(WIDTH);
			int y = gen.nextInt(HEIGHT);
			if (!agents[x][y] && !walls[x][y]) {
				agentsList.add(new Zombie(x, y, this));
			}
		}
		
		// update agent location tracker
		for (Agent a : agentsList) {
			agents[a.getLocation().x][a.getLocation().y] = true;
		}
	}

	/**
	 * Renders and returns an image of the current state of the city (map and
	 * people)
	 * 
	 * @return an Image of the city that can be drawn somewhere.
	 */
	public Image getMap() {
		// add Agent pixels to map image
		for (Agent a : agentsList) {
			if (a instanceof Human) {
				map.setRGB(a.getLocation().x, a.getLocation().y, Color.BLUE.getRGB());
			} else if (a instanceof Zombie) {
				map.setRGB(a.getLocation().x, a.getLocation().y, Color.GREEN.getRGB());
			}
			if (a instanceof PanickedHuman) {
				map.setRGB(a.getLocation().x, a.getLocation().y, Color.RED.getRGB());
			}
		}
		img2d.drawImage(map, 0, 0, null); // draw the map onto this new image. Will overwrite old stuff, so creates a new frame
		return image;
	}

	/**
	 * Progress the city through one turn
	 */
	protected void tick() {
		for (Agent a : agentsList) {
			map.setRGB(a.getLocation().x, a.getLocation().y, ROAD_COLOR.getRGB()); // reset old location's color
			agents[a.getLocation().x][a.getLocation().y] = false; // update old location in agent tracker
			a.move();
			agents[a.getLocation().x][a.getLocation().y] = true; // update agent location tracker
		}
	}
}
