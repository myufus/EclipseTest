import java.awt.Point;

/**
 * A clas to model zombies in the apocalypse
 * 
 * @author Muhammad Yusuf
 */
public class Zombie extends Agent {
	public Zombie(int x, int y, City c) {
		super(new Point(x, y), 3, c);
	}

	/**
	 * Make move or bite someone
	 */
	public void move() {
		super.lookAhead();
		boolean pursuit = false; // whether a human is in your sight
		Agent toBite = null; // if in pursuit Agent you're pursuing
		if (ahead == Obstruction.AGENT) {
			/*
			 * - go through list of agents
			 * - check if in your line of view
			 * - check if human or pacnicked human
			 * - if so go into pursuit anf set target
			 */
			switch (direction) {
			case NORTH: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x == this.location.x
							&& a.getLocation().y + obstructionDistance == this.location.y && (a instanceof Human || a instanceof PanickedHuman)){
						pursuit = true;
						toBite = a;
					}
				}
				break;
			}
			case SOUTH: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x == this.location.x
							&& a.getLocation().y - obstructionDistance == this.location.y && (a instanceof Human || a instanceof PanickedHuman)){
						pursuit = true;
						toBite = a;
					}
				}
				break;
			}
			case EAST: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x + obstructionDistance == this.location.x
							&& a.getLocation().y == this.location.y && (a instanceof Human || a instanceof PanickedHuman)){
						pursuit = true;
						toBite = a;
					}
				}
				break;
			}
			case WEST: {
				for (Agent a : city.agentsList) {
					if (a.getLocation().x - obstructionDistance == this.location.x
							&& a.getLocation().y == this.location.y && (a instanceof Human || a instanceof PanickedHuman)){
						pursuit = true;
						toBite = a;
					}
				}
				break;
			}
			}
		}
		
		// if in pursuit and target is within reach bite, otherwise move
		if(pursuit && obstructionDistance <= speed)
		{
			bite(toBite);
		}
		else
		{
			super.move();
		}
	}

	/**
	 * Replaces Human or PanickedHuman in City with Zombie
	 * @param a Agent to be infected
	 */
	public void bite(Agent a) {
		city.agentsList.set(city.agentsList.indexOf(a), new Zombie(a.getLocation().x, a.getLocation().y, city));
	}
}