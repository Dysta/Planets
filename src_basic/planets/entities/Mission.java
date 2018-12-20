package planets.entities;

import planets.entities.planet.Planet;
import java.util.ArrayList;

import planets.entities.ship.Ship;

/**
 * A mission links two planets and serves the purpose of managing squadrons in order to achieve itself.
 * 
 * @author Adri
 */
public class Mission {

    /**
     * Used to give a unique incremental mission ID.
     */
    private static int last_id = 0;
    /**
     * This missions's ID.
     */
    private final int id;

    /**
     * Represents an Attack Mission, whose goal is to reduce the enemy's ship stock and eventually conquering the target planet.
     */
    public static final String ATTACK = "ATTACK";
    /**
     * Represents a Convoy Mission, whose goal is to transport all ships from one destination to another.
     */
    public static final String CONVOY = "CONVOY";

    /**
     * A collection of currently active squads.
     */
    private ArrayList<Squad> squads;

    /**
     * The number of ships left to deploy into squads.
     */
    private int addQueue;

    /**
     * The player who ordered this mission.
     */
    private final Player owner;
    
    /**
     * The origin planet of this mission.
     */
    private final Planet origin;
    
    /**
     * The destination planet of this mission.
     */
    private final Planet destination;

    /**
     * The current objective.
     */
    private String mission;

    /**
     * The maximum ships a squad can deploy at a time, it is linked to the planet's size.
     */
    private final int squadSize;

    /**
     * Creates a new mission
     * 
     * @param p1 The origin planet
     * @param p2 The destination planet
     * @param addQueue The number of ships to deploy in total
     * @param squadSize The number of ships to deploy at a time
     * @param mission The goal of this mission
     */
    public Mission(Planet p1, Planet p2, int addQueue, int squadSize, String mission) {
        Mission.last_id++;
        this.id = Mission.last_id;

        this.addQueue = addQueue;
        this.squads = new ArrayList<>();
        this.owner = p1.getOwner();
        this.origin = p1;
        this.destination = p2;
        this.mission = mission;
        this.squadSize = squadSize;
    }

    /**
     * Executed each game tick.
     */
    public void handle() {
        this.send_squad();
        this.move_squads();
        this.clearSquads();
    }

    /**
     * If possible, creates a new squad and launches it.
     */
    public void send_squad() {
        if (this.origin.freeToLaunch()) {
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

    /**
     * Handles the movement and arrival of each squad.
     */
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
                break;
        }
        arrivers.clear();
    }

    /**
     * Remove all squads without any ship.
     */
    public void clearSquads() {
        this.squads.removeIf((Squad s) -> s.isEmpty());
    }

    /**
     * Resets the deploy queue to stop squads from being created.
     */
    public void clearQueue() {
        this.addQueue = 0;
    }

    /**
     * Provides a reference to this mission's squads collection.
     * 
     * @return a squad collection
     */
    public ArrayList<Squad> getSquads() {
        return this.squads;
    }

    /**
     * Replaces this missions's squad collection by the given parameter.
     * 
     * @param squads The new squad collection
     */
    public void setSquads(ArrayList<Squad> squads) {
        this.squads = squads;
    }

    /**
     * Transfers a squad to this mission
     * 
     * @param s the squad to add
     */
    public void addSquad(Squad s) {
        this.squads.add(s);
    }

    /**
     * Performs a clean remove of a squad from this mission, if found.
     * 
     * @param s the squad to clean.
     */
    public void cancelSquad(Squad s) {
        if (this.squads.contains(s)) {
            this.squads.remove(s);
        }
    }

    /**
     * Returns a reference to this mission's origin planet.
     * 
     * @return a planet object
     */
    public Planet getOriginPlanet() {
        return this.origin;
    }

    /**
     * Returns a reference to this mission's destination planet.
     * 
     * @return a planet object
     */
    public Planet getDestinationPlanet() {
        return this.destination;
    }

    /**
     * Checks whether this missions contains any squad or is planning on adding more.
     * 
     * @return true if this squad is empty and won't be filled again
     */
    public boolean isEmpty() {
        return this.squads.size() <= 0 && this.addQueue == 0;
    }

    /**
     * Returns the number of ships left to deploy for this mission.
     * 
     * @return the addQueue attribute
     */
    public int getAddQueue() {
        return this.addQueue;
    }

    /**
     * Returns a reference to this mission's owner.
     * 
     * @return a player object
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Returns the topic of this mission.
     * 
     * @return a mission string compatible with the format Mission.MISSION
     */
    public String getMission() {
        return this.mission;
    }

    /**
     * Returs the maximum capacity of a squad for this mission.
     * 
     * @return the squadSize attribute
     */
    public int getSquadSize() {
        return this.squadSize;
    }

    /**
     * Get this mission's unique identifier
     * 
     * @return the id attribute
     */
    public int getId() {
        return this.id;
    }
}
