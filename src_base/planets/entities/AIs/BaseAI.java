/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.entities.AIs;

import javafx.scene.paint.Color;

/**
 *  This is a very basic AI with basic behavior and a very neutral position towards the game.
 * 
 * @author Adri
 */
public class BaseAI extends AI {

    /**
     * Creates a normal AI with 10 in all attributes.
     * 
     * @param color The custom color for this AI
     */
    public BaseAI(Color color) {
        super(color,10,10,10,10);
    }
    
    /**
     * Returns its own classpath.
     * 
     * @return its own classpath
     */
    @Override
    public String AIclass() {
        return "planets.entities.AIs.BaseAI";
    }
}
