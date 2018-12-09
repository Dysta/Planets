package input;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import planets.Game;
import planets.entities.Mission;
import planets.entities.planet.Planet;
import planets.entities.Squad;
import planets.entities.ship.Ship;

public class MouseHandler implements EventHandler<MouseEvent> {

    private MouseEvent e;
    double mX;
    double mY;
    private boolean clicked_a_planet;
    private boolean clicked_a_squad;

    @Override
    public void handle(MouseEvent evt) {
        this.e = evt;
        mX = e.getX();
        mY = e.getY();

        if (e.isPrimaryButtonDown()) {
            // Mouse1 pressed

            if (!Game.primaryHeld) {
                Game.primaryHeld = true;
                // Mouse1 unique click

                clicked_a_planet = false;
                clicked_a_squad = false;

                for (Planet p : Game.galaxy.getPlanets()) {
                    this.handlePlanetClick(p);
                }

                if (!clicked_a_planet) {
                    Game.deselect(Game.selectedPlanets);

                    for (Mission m : Game.missions) {
                        for (Squad s : m.getSquads()) {
                            this.handleSquadClick(m, s);
                        }
                    }
                }

                if (!clicked_a_squad) {
                    Game.deselectSquads();
                }
            }
        }
    }

    private void handlePlanetClick(Planet p) {
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
                        // This is a friendly planet
                        if (e.isControlDown()) {
                            // Select this planet too
                            Game.setSelect(Game.selectedPlanets, p, true);
                        } else {
                            // Start a convoy
                            Game.startConvoy(p);
                        }
                    }
                } else {
                    // Planet is currently selected
                    if (Game.selectedPlanets.size() == 1) {
                        // Deselect a planet if clicked and already selected
                        Game.setSelect(Game.selectedPlanets, p, false);
                    } else {
                        // Keep only this one selected if others were selected < Replaced by convoy
                        /*
                        for (Planet o : Game.selectedPlanets) {
                            if (!o.equals(p)) {
                                o.setSelected(false);
                            }
                        }
                        Game.selectedPlanets.removeIf((Planet o) -> !o.equals(p));
                         */

                        Game.startConvoy(p);
                    }
                }
            } else {
                // Enemy planet
                if (!Game.selectedPlanets.isEmpty()) {
                    // Send ships from the selected planets
                    for (Planet o : Game.selectedPlanets) {
                        Game.startAttack(o, p);
                    }
                    Game.deselect(Game.selectedPlanets);
                } else {
                    // No planet selected but clicked on enemy planet
                    if (!Game.selectedSquads.isEmpty()) {
                        // Squads are selected, redirect them
                        Game.changeMission(Game.selectedSquads, p, Mission.ATTACK);
                        Game.deselectSquads();
                    }
                }
            }
        }
    }

    private void handleSquadClick(Mission m, Squad s) {
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
                            Game.deselectSquads();
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
