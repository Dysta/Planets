package planets.entities;

import planets.entities.planet.Planet;
import java.util.ArrayList;
import planets.Game;
import planets.utils.GameUtils;
import planets.utils.Point;

import planets.entities.ship.Ship;

public class Mission {

    public static final String ATTACK = "ATTACK";
    public static final String CONVOY = "CONVOY";

    private ArrayList<Squad> squads;

    private int addQueue;
    
    private Player owner;
    private final Planet origin;
    private final Planet destination;

    private String mission;

    private int squadSize;

    public Mission(Planet p1, Planet p2, int addQueue, int squadSize, String mission) {
        this.addQueue = addQueue;
        this.squads = new ArrayList<>();
        this.owner = p1.getOwner();
        this.origin = p1;
        this.destination = p2;
        this.mission = mission;
        this.squadSize = squadSize;
    }

    public void handle() {
        this.send_squad();
        this.move_squads();
        this.clearSquads();
    }

    public void send_squad() {
        int squadsCount = this.squads.size();
        if (squadsCount == 0 || this.origin.freeToLaunch()) {
            if (this.addQueue > 0) {
                int mobilize = this.squadSize;

                if (this.addQueue < this.squadSize) {
                    mobilize = this.addQueue;
                }

                ArrayList<Ship> squadMembers = this.origin.flyShips(mobilize);
                this.squads.add(new Squad(this.origin, this.destination, squadMembers, this));
                this.addQueue -= mobilize;
            }
        }
    }

    public void move_squads() {
        ArrayList<Ship> arrivers = new ArrayList<>();

        for (Squad s : this.squads) {
            arrivers.addAll(s.progress());
        }

        switch (this.mission) {
            case Mission.ATTACK:
                if (this.owner == this.destination.getOwner()) {
                    // The planet will gladly receive the ships, 
                    // as it's been conquered since the order to attack was given
                    this.mission = Mission.CONVOY;
                }
                break;
            case Mission.CONVOY:
                if (this.owner != this.destination.getOwner()) {
                    // Abort escort mission ! Units will have to fight
                    this.mission = Mission.ATTACK;
                }
                break;
        }

        switch (this.mission) {
            case Mission.ATTACK:
                if (this.destination.defend(arrivers)) {
                    this.destination.setOwner(this.owner);
                }
                break;
            case Mission.CONVOY:
                this.destination.landShips(arrivers);
                arrivers.clear();
                break;
        }
    }

    public void clearSquads() {
        /*
        for(Squad s : this.squads) {
            if(s.isEmpty() && Game.selectedSquads.contains(s)) {
                Game.selectedSquads.remove(s);
            }
        }*/
        this.squads.removeIf((Squad s) -> s.isEmpty());
    }
    
    public ArrayList<Squad> getSquads() {
        return this.squads;
    }
    
    public void addQuad(Squad s) {
        this.squads.add(s);
    }
    
    public void cancelSquad(Squad s) {
        if(this.squads.contains(s)) {
            this.squads.remove(s);
        }
    }

    public Planet getOriginPlanet() {
        return this.origin;
    }

    public Planet getDestinationPlanet() {
        return this.destination;
    }
    
    public boolean isEmpty() {
        return this.squads.size() <= 0 && this.addQueue == 0;
    }
}
