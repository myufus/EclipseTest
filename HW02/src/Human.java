import java.awt.Point;

/**
 * A class to model regular humans in the apocalypse
 * 
 * @author Muhammad Yusuf
 */
public class Human extends Agent {
	public Human(int x, int y, City c) {
		super(new Point(x, y), 5, c);
	}

	/**
	 * Make move or go into panic
	 */
	public void move() {
		//look for zombies or panicked humans
		super.lookAhead();
		boolean toPanic = false; // should this person go into panic or not
		Direction panicDirection = getDirection(); // if going into panic, which direction to face
		if (ahead == Obstruction.AGENT) {
			/*
			 * - go through list of agents
			 * - check if in your line of view
			 * - check if zombie or pacnicked human
			 * - if so go into panic and set direction appropriately
			 */
			switch (direction) {
			case NORTH: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x == this.location.x && a.getLocation().y + obstructionDistance == this.location.y && (a instanceof Zombie || a instanceof PanickedHuman)){
						panicDirection = Direction.SOUTH;
						toPanic = true;
					}
				}
				break;
			}
			case SOUTH: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x == this.location.x && a.getLocation().y - obstructionDistance == this.location.y && (a instanceof Zombie || a instanceof PanickedHuman)){
						panicDirection = Direction.NORTH;
						toPanic = true;
					}
				}
				break;
			}
			case EAST: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x + obstructionDistance == this.location.x && a.getLocation().y == this.location.y && (a instanceof Zombie || a instanceof PanickedHuman)){
						panicDirection = Direction.WEST;
						toPanic = true;
					}
				}
				break;
			}
			case WEST: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x - obstructionDistance == this.location.x && a.getLocation().y == this.location.y && (a instanceof Zombie || a instanceof PanickedHuman)){
						panicDirection = Direction.EAST;
						toPanic = true;
					}
				}
				break;
			}
			}
		}
		
		//either go into panic or make move
		if(toPanic)
		{
			panic(panicDirection);
		}
		else
		{
			super.move();
		}
	}

	/**
	 * Replaces this Human in City with PanickedHuman
	 * @param d The direction the new PanickedHuman should be facing
	 */
	public void panic(Direction d) {
		if (city.agentsList.indexOf(this) > -1) {
			city.agentsList.set(city.agentsList.indexOf(this),
					new PanickedHuman(this.getLocation().x, this.getLocation().y, city, d));
		}
	}
}