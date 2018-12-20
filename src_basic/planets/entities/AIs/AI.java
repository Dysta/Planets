package planets.entities.AIs;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import planets.windows.Game;
import planets.entities.Galaxy;
import planets.entities.Player;
import planets.entities.planet.Planet;
import planets.utils.GameUtils;

/**
 * This is a Player controlled by code
 * 
 * @author Adri
 */
abstract public class AI extends Player {

    /**
     * Risk and reward as well as overall courage.
     */
    private int aggressivity;
    
    /**
     * The contrary of aggressivity.
     */
    private int cautiousness;
    
    /**
     * The ability to assess a situation.
     */
    private int wisdom;
    
    /**
     * The amount of security the AI needs before validating its decision.
     */
    private int doubt;

    /**
     * Creates an AI according to minimalistic characterstics and its color.
     * 
     * @param color This player's color
     * @param aggressivity The aggressivity characteristic
     * @param cautiousness The cautiousness characteristic
     * @param wisdom The wisdom characteristic
     * @param doubt  The doubt characteristic
     */
    public AI(Color color, int aggressivity, int cautiousness, int wisdom, int doubt) {
        super(color, true);
        this.aggressivity = aggressivity;
        this.cautiousness = cautiousness;
        this.wisdom = wisdom;
        this.doubt = doubt;
    }

    /**
     * The actions performed at each AI_THINK tick
     */
    @Override
    public void handle() {
        for (Planet p : Galaxy.getPlanets()) {
            if (p.getOwner() == this) {
                boolean orderGiven = false;

                Planet t = Galaxy.getPlanets().get(GameUtils.getRandomIntegerBetweenRange(0, Galaxy.getPlanets().size() - 1));
                if (t.getOwner() == this) {
                    continue;
                }

                if (shouldInvade(p, t)) {
                    Game.startAttack(p, t);
                    orderGiven = true;
                }

            }
        }
    }

    /**
     * Compares two arbitrary forces according to its characteristics.
     * 
     * @param me The AIs strength
     * @param enemy The opposing strength
     * @return The difference in strength according to the AI
     */
    private double forceCompare(double me, double enemy) {
        return (me + (this.aggressivity / this.wisdom)) / (enemy + this.cautiousness);
    }

    /**
     * Takes a decision according to its characteristics.
     * 
     * @param odds The arbitrary odds for the decision
     * @return Whether this decision is taken or not
     */
    private boolean decide(double odds) {
        return (odds - (this.doubt / 100)) > 1;
    }

    /**
     * Makes the AI compare two planets' strength.
     * 
     * @param p Its own planet
     * @param t The potential target
     * @return The difference in strength according to the AI
     */
    private double percentInvade(Planet p, Planet t) {
        return forceCompare(p.getPower(), t.getPower());
    }


    /**
     * Makes the AI compare a group invasion against a single planet
     * 
     * @param ps The originating planets collection
     * @param t The target
     * @return The difference in strength according to the AI
     */
    private double percentGroupInvade(ArrayList<Planet> ps, Planet t) {
        int forces = 0;

        for (Planet p : ps) {
            forces += p.getPower();
        }

        return forceCompare(forces, t.getPower());
    }

    /**
     * Decides if the AI should start an invasion of a planet from one of his own
     * 
     * @param p Its own planet
     * @param t The target
     * @return Whether the decision is taken
     */
    private boolean shouldInvade(Planet p, Planet t) {
        return decide(percentInvade(p, t));
    }
    
    /**
     * Returns true, as opposed to a simple Player object.
     * 
     * @return true
     */
    @Override
    public boolean isAI() {
        return true;
    }
    
    /**
     * Forces the extending methods to make their classpath available.
     * 
     * @return its own classpath
     */
    abstract public String AIclass();
}
