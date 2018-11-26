package planets.Entities;

import java.util.ArrayList;

import ship.Ship;

public class Route {

	private ArrayList<Ship> ships;
	
	private Planet origin;
	private Planet destination;
	
	public Route(Planet p1, Planet p2, ArrayList<Ship> ships) {
		this.ships = new ArrayList<Ship>();
		
		int c = 0;
		int t = ships.size();
		while(c<t) {
			this.ships.add(ships.get(c));
			ships.remove(c);
			c++;
		}
		
		this.origin = p1;
		this.origin = p2;
	}
	
	public void move_ships() {
		ArrayList<Ship> attackers = new ArrayList<Ship>();

		int c = 0;
		int t = ships.size();
		while(c<t) {
			Ship s = this.ships.get(c);
			// TODO: fixed speed
			s.move((this.destination.getPosX() - s.getPosX()) / 10, (this.destination.getPosY() - s.getPosY()) / 10);
			
			if(destination.inOrbit(s)) {
				attackers.add(s);
				this.ships.remove(s);
			}
			
			c++;
		}
	}
}
