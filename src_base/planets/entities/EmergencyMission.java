/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.entities;

/**
 *
 * @author Adri
 */
public class EmergencyMission extends Mission {
    
    private EmergencyMission(Planet p1, Planet p2, int addQueue, int squadSize, String mission) {
        super(p1, p2, addQueue, squadSize, mission);
    }
    
    public EmergencyMission(Planet newDestination, String mission) {
        this(null, newDestination, 0, 0, mission);
    }
    
    @Override
    public void handle() {
        this.move_squads();
        this.clearSquads();
    }
}
