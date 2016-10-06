import java.awt.Point;

/**
 * An abstract class that provides a framework for all agents in the apocalypse
 * 
 * @author Muhammad Yusuf
 */
public abstract class Agent {
	// define directions
	public enum Direction {
		NORTH, SOUTH, EAST, WEST
	}

	public Direction randomDirection() {
		int random = (int) (Math.random() * 4);
		return Direction.values()[random];
	}

	// define various obstacle types
	enum Obstruction {
		WALL, AGENT, NONE
	}

	protected Point location;
	protected Direction direction;
	protected int speed;
	protected final City city;
	protected int obstructionDistance;
	protected Obstruction ahead;

	public Agent(Point l, int s, City c) {
		location = l;
		speed = s;
		direction = randomDirection();
		city = c;
		lookAhead();
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point p) {
		location = p;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction d) {
		direction = d;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int s) {
		speed = s;
	}

	/**
	 * Check if there's an obstruction hindering movement and either move or turn in a random direction
	 */
	public void move() {
		// iff you have room to move
		if (obstructionDistance > speed) {
			int steps = (int)(Math.random() * speed) + 1; // move random number of steps up to given speed
			// move if you'll stay on map, otherwise turn in random direction
			switch (direction) {
			case NORTH: {
				if (location.y - steps >= 0) {
					location.translate(0, -steps);
				} else {
					direction = randomDirection();
				}
				break;
			}
			case SOUTH: {
				if (location.y + steps < City.HEIGHT) {
					location.translate(0, steps);
				} else {
					direction = randomDirection();
				}
				break;
			}
			case EAST: {
				if (location.x - steps >= 0) {
					location.translate(-steps, 0);
				} else {
					direction = randomDirection();
				}
				break;
			}
			case WEST: {
				if (location.x + steps < City.WIDTH) {
					location.translate(steps, 0);
				} else {
					direction = randomDirection();
				}
				break;
			}
			}
		} else {
			// not enough room to move so turn
			direction = randomDirection();
		}
		// identify what's ahead of you now
		ahead = lookAhead();
	}

	/**
	 * Looks ahead on map and determines whether there is obstruction within ten spaces and what it is
	 * @return
	 */
	public Obstruction lookAhead() {
		for (int i = 1; i < 10; i++) {
			/*
			 * - if space i units away is a wall obstruction is a wall i spaces away
			 * - if space i units away is an agent obstruction is an agent i units away
			 */
			switch (direction) {
			case NORTH: {
				if (location.y - i > 0 && city.walls[location.x][location.y - i]) {
					obstructionDistance = i;
					return Obstruction.WALL;
				} else if (location.y - i > 0 && city.agents[location.x][location.y - i]) {
					obstructionDistance = i;
					return Obstruction.AGENT;
				}
				break;
			}
			case SOUTH: {
				if (location.y + i < City.HEIGHT && city.walls[location.x][location.y + i]) {
					obstructionDistance = i;
					return Obstruction.WALL;
				} else if (location.y + i < City.HEIGHT && city.agents[location.x][location.y + i]) {
					obstructionDistance = i;
					return Obstruction.AGENT;
				}
				break;
			}
			case EAST: {
				if (location.x - i > 0 && city.walls[location.x - i][location.y]) {
					obstructionDistance = i;
					return Obstruction.WALL;
				} else if (location.x - i > 0 && city.agents[location.x - i][location.y]) {
					obstructionDistance = i;
					return Obstruction.AGENT;
				}
				break;
			}
			case WEST: {
				if (location.x + i < City.WIDTH && city.walls[location.x + i][location.y]) {
					obstructionDistance = i;
					return Obstruction.WALL;
				}
				if (location.x + i < City.WIDTH && city.agents[location.x + i][location.y]) {
					obstructionDistance = i;
					return Obstruction.AGENT;
				}
				break;
			}
			}
		}
		//nothing's directly ahead within line of sight
		obstructionDistance = 11;
		return Obstruction.NONE;
	}
}