package planets.entities.AIs;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import planets.Game;
import planets.entities.Galaxy;
import planets.entities.Player;
import planets.entities.planet.Planet;

abstract public class AI extends Player {
    
    private int aggressivity;
    private int cautiousness;
    private int wisdom;
    private int doubt;
    
    public AI(Color color, int aggressivity, int cautiousness,int wisdom, int doubt) {
        super(color);
        this.aggressivity = aggressivity;
        this.cautiousness = cautiousness;
        this.wisdom = wisdom;
        this.doubt = doubt;
    }
    
    @Override
    public void handle() {
        for(Planet p : Galaxy.getPlanets()) {
            if(p.getOwner() == this) {
                boolean orderGiven = false;
                
                for(Planet t : Galaxy.getPlanets()) {
                    if(orderGiven) break;
                    if(t.getOwner() == this) continue;
                    
                    if(shouldInvade(p,t)) {
                        System.out.println("AI attack !");
                        Game.startAttack(p, t);
                        orderGiven = true;
                    }
                }
                
            }
        }
    }
    
    private double forceCompare(double me, double enemy) {
        return (me  + (this.aggressivity/this.wisdom)) / (enemy + this.cautiousness);
    }
    
    private boolean decide(double odds) {
        return (odds - (this.doubt/100)) > 1;
    }
    
    private double percentInvade(Planet p, Planet t) {
        return forceCompare(p.getPower(),t.getPower());
    }
    
    private double percentGroupInvade(ArrayList<Planet> ps, Planet t) {
        int forces = 0;
        
        for(Planet p : ps) {
            forces += p.getPower();
        }
        
        return forceCompare(forces,t.getPower());
    }
    
    private boolean shouldInvade(Planet p, Planet t) {
        return decide(percentInvade(p, t));
    }
}
