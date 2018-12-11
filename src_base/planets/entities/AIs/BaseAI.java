/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.entities.AIs;

import javafx.scene.paint.Color;

/**
 *
 * @author Adri
 */
public class BaseAI extends AI {

    public BaseAI(Color color) {
        super(color,10,10,10,10);
    }
    
    @Override
    public String AIclass() {
        return "planets.entities.AIs.BaseAI";
    }
}
