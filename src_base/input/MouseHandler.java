package input;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import planets.Game;
import planets.entities.Mission;
import planets.entities.Planet;
import planets.entities.Squad;
import planets.entities.ship.Ship;

public class MouseHandler implements EventHandler<MouseEvent> {

	@Override
	public void handle(MouseEvent e) {
        double mX = e.getX();
        double mY = e.getY();

        if (e.isPrimaryButtonDown()) {
            // Mouse1 pressed

            if (!Game.primaryHeld) {
                Game.primaryHeld = true;
                // Mouse1 unique click

                boolean clicked_a_planet = false;
                boolean clicked_a_squad = false;

                for (Planet p : Game.galaxy.getPlanets()) {
                    if (p.isOn(mX, mY)) {
                        clicked_a_planet = true;
                        if (p.getOwner().isMainPlayer()) {
                            if (!Game.selectedPlanets.contains(p)) {
                                if (Game.selectedPlanets.isEmpty()) {
                                    if (Game.selectedSquads.isEmpty()) {
                                        // Select this planet
                                        Game.setSelect(Game.selectedPlanets, p, true);
                                    } else {
                                        // Squads are selected, redirect them
                                        Game.changeMission(Game.selectedSquads, p, Mission.CONVOY);
                                    }
                                } else {
                                    // This is an friendly planet
                                    if (e.isControlDown()) {
                                        // Select this planet too
                                        Game.setSelect(Game.selectedPlanets, p, true);
                                    } else {
                                        // Start a convoy
                                        for (Planet o : Game.selectedPlanets) {
                                            Game.startMission(o, p, o.getNbShip(), o.getMaxLaunchShips(), Mission.CONVOY);
                                        }
                                        Game.selectedPlanets.clear();
                                    }
                                }
                            } else {
                                if (Game.selectedPlanets.size() == 1) {
                                    // Deselect a planet if clicked and already selected
                                    Game.setSelect(Game.selectedPlanets, p, false);
                                } else {
                                    // Keep only this one selected if others were selected
                                    for (Planet o : Game.selectedPlanets) {
                                        if (!o.equals(p)) {
                                            o.setSelected(false);
                                        }
                                    }
                                    Game.selectedPlanets.removeIf((Planet o) -> !o.equals(p));
                                }
                            }
                        } else {
                            // Enemy planet
                            if (!Game.selectedPlanets.isEmpty()) {
                                // Send ships from the selected planets
                                for (Planet o : Game.selectedPlanets) {
                                    Game.startMission(o, p, o.getNbShip(), o.getMaxLaunchShips(), Mission.ATTACK);
                                    o.setSelected(false);
                                }
                                Game.selectedPlanets.clear();
                            } else {
                                // No planet selected but clicked on enemy planet
                                if (!Game.selectedSquads.isEmpty()) {
                                    // Squads are selected, redirect them
                                    Game.changeMission(Game.selectedSquads, p, Mission.ATTACK);
                                    for (Squad s : Game.selectedSquads) {
                                        for (Ship sq : s.getShips()) {
                                            sq.setSelected(false);
                                        }
                                    }
                                    Game.selectedSquads.clear();
                                }
                            }
                        }
                    }
                }

                if (!clicked_a_planet) {
                    Game.selectedPlanets.clear();

                    for (Mission m : Game.missions) {
                        for (Squad s : m.getSquads()) {
                            if (s.isOn(mX, mY)) {
                                clicked_a_squad = true;
                                // Cannot select enemy squads
                                if (m.getOriginPlanet().getOwner().isMainPlayer()) {
                                    if (!Game.selectedSquads.contains(s)) {
                                        if (Game.selectedSquads.isEmpty()) {
                                            // Select this Squad
                                            Game.setSelectSquad(s, true);
                                        } else {
                                            if (e.isControlDown()) {
                                                // Select this squad too
                                                Game.setSelectSquad(s, true);
                                            } else {
                                                // Select this squad instead
                                                for (Squad sq : Game.selectedSquads) {
                                                    for (Ship sh : sq.getShips()) {
                                                        sh.setSelected(false);
                                                    }
                                                }
                                                Game.selectedSquads.clear();
                                                Game.setSelectSquad(s, true);
                                            }
                                        }
                                    } else {
                                        // Do nothing
                                    }
                                }
                            }
                        }
                    }
                } 

                if (!clicked_a_squad) {
                	for(Squad s : Game.selectedSquads) {
                		for(Ship sh : s.getShips()) {
                			sh.setSelected(false);
                		}
                	}
                    Game.selectedSquads.clear();
                }
            }
        }
    }

}
