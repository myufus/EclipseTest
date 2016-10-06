import java.awt.Point;

/**
 * Class that models panicked humans in apocalypse
 * 
 * @author Muhammad Yusuf
 */
public class PanickedHuman extends Agent {
	private int counter = 10; // counts how long person's been panicking

	public PanickedHuman(int x, int y, City c, Direction d) {
		super(new Point(x, y), 7, c);
		setDirection(d);
		move();
	}

	/**
	 * Moves, then checks if human should calm down by reverting to Human
	 */
	public void move() {
		super.move();
		counter--;
		if (counter == 0 && city.agentsList.indexOf(this) > -1) {
			city.agentsList.set(city.agentsList.indexOf(this),
					new Human(this.getLocation().x, this.getLocation().y, city));
		}
	}
}