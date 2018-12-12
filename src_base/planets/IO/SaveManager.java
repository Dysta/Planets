package planets.IO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import planets.App;
import static planets.App.game;
import planets.ResourcesManager;
import planets.Sprite;
import planets.entities.AIs.AI;
import planets.entities.AIs.BaseAI;
import planets.entities.Galaxy;
import planets.entities.Mission;

import planets.entities.Player;
import planets.entities.Squad;
import planets.entities.planet.Planet;
import planets.entities.ship.Ship;
import planets.utils.IteratableNodeList;
import planets.windows.Game;

/**
 *
 * @author Adri
 */
public class SaveManager {

    private final static String save_folder = "C:\\Users\\Adri\\Desktop\\test\\";
    private final static String save_ext = ".planets";
    private static Document doc;

    public static void save(Game game, String savename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);
            addAttribute("width", Double.toString(game.WIDTH), rootElement);
            addAttribute("height", Double.toString(game.HEIGHT), rootElement);

            Element galaxy = doc.createElement("Galaxy");
            rootElement.appendChild(galaxy);
            addAttribute("borderMargin", Double.toString(Galaxy.borderMargin), galaxy);

            Element players = doc.createElement("Players");
            rootElement.appendChild(players);
            addPlayers(players);

            Element planets = doc.createElement("Planets");
            rootElement.appendChild(planets);
            addPlanets(planets);

            Element missions = doc.createElement("Missions");
            rootElement.appendChild(missions);
            addMissions(missions);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(SaveManager.save_folder + savename + save_ext));

            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public static void load(String save_name) throws ParserConfigurationException, SAXException, IOException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Game.toggleFreeze();
        File file = new File(SaveManager.save_folder + save_name + save_ext);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        Node gameNode = document.getElementsByTagName("game").item(0);

        double width = Double.parseDouble(getAttribute(gameNode, "width"));
        double height = Double.parseDouble(getAttribute(gameNode, "height"));

        // Create new game
        App.startGame(1, 1);
        Game game = App.game;

        Node galaxyNode = gameNode.getChildNodes().item(0);

        double borderMargin = Double.parseDouble(getAttribute(galaxyNode, "borderMargin"));

        Node players = gameNode.getChildNodes().item(1);
        Node planets = gameNode.getChildNodes().item(2);
        Node missions = gameNode.getChildNodes().item(3);

        ArrayList<Player> playersArrayList = new ArrayList<>();
        ArrayList<Planet> planetsArrayList = new ArrayList<>();

        Map<String, Player> playersList = new HashMap<>();
        for (Node p : new IteratableNodeList(players.getChildNodes())) {
            boolean active = getAttribute(p, "active").equals("1");
            Player pl = new Player(
                    getAttribute(p, "shipType"),
                    getAttribute(p, "mainPlayer").equals("1"),
                    Double.parseDouble(getAttribute(p, "effectivesPercent")),
                    active ? Color.web(getAttribute(p, "color")) : Color.GREY,
                    active);

            if (!"0".equals(p.getAttributes().getNamedItem("AI").getTextContent())) {
                pl = new BaseAI(Color.web(getAttribute(p, "color")));
            }
            pl.setId(Integer.parseInt(getAttribute(p, "id")));

            playersList.put(Integer.toString(pl.getId()), pl);
            playersArrayList.add(pl);
        }

        Map<String, Planet> planetsList = new HashMap<>();
        for (Node p : new IteratableNodeList(planets.getChildNodes())) {
            String t = getAttribute(p, "type");

            String classRef = "planets.entities.planet." + Character.toUpperCase(t.charAt(0)) + t.substring(1);

            Player owner = playersList.get(getAttribute(p, "ownerId"));

            Planet pl = (Planet) Class.forName(classRef)
                    .getConstructor(Sprite.class, Player.class, double.class, double.class, double.class)
                    .newInstance(ResourcesManager.assets.get("basePlanet"),
                            owner,
                            Double.parseDouble(getAttribute(p, "x")),
                            Double.parseDouble(getAttribute(p, "y")),
                            Double.parseDouble(getAttribute(p, "size"))
                    );

            pl.setId(Integer.parseInt(p.getAttributes().getNamedItem("id").getTextContent()));
            if (owner.isActive()) {
                pl.setOwner(owner);
            }
            pl.loadPlanet(
                    getAttribute(p, "shipType"),
                    Double.parseDouble(getAttribute(p, "shipsPerTick")),
                    Integer.parseInt(getAttribute(p, "shipsCount")),
                    Integer.parseInt(getAttribute(p, "shipCapacity")),
                    Double.parseDouble(getAttribute(p, "productionProgression")));

            planetsList.put(Integer.toString(pl.getId()), pl);
            planetsArrayList.add(pl);
        }

        ArrayList<Mission> missionsList = new ArrayList<>();
        for (Node m : new IteratableNodeList(missions.getChildNodes())) {

            Planet origin = planetsList.get(getAttribute(m, "origin"));
            Planet destination = planetsList.get(getAttribute(m, "destination"));

            Mission mi = new Mission(
                    origin,
                    destination,
                    Integer.parseInt(getAttribute(m, "addQueue")),
                    Integer.parseInt(getAttribute(m, "squadSize")),
                    getAttribute(m, "mission")
            );

            ArrayList<Squad> squads = new ArrayList<>();
            for (Node s : new IteratableNodeList(m.getChildNodes().item(0).getChildNodes())) {
                // Squad

                ArrayList<Ship> shipsArrayList = new ArrayList<>();
                for (Node ship : new IteratableNodeList(s.getChildNodes().item(0).getChildNodes())) {
                    // Squad's ships

                    Ship sh = (Ship) Class.forName(getAttribute(ship, "type"))
                            .getConstructor(Sprite.class, double.class, double.class)
                            .newInstance(ResourcesManager.assets.get("baseShip"),
                                    Double.parseDouble(getAttribute(ship, "x")),
                                    Double.parseDouble(getAttribute(ship, "y")));

                    sh.setCurrentSpeed(Double.parseDouble(getAttribute(ship, "currentSpeed")));
                    shipsArrayList.add(sh);
                }

                Squad sq = new Squad(origin, destination, shipsArrayList, mi);
                squads.add(sq);
            }

            mi.setSquads(squads);
            missionsList.add(mi);
        }

        System.out.println("-- Loading --");
        Galaxy galaxy = new Galaxy(width, height, planetsArrayList, playersArrayList, borderMargin);

        game.load(galaxy, missionsList);
        Game.toggleFreeze();
    }

    private static void addAttribute(String name, String value, Element node) {
        Attr attr = doc.createAttribute(name);
        attr.setValue(value);
        node.setAttributeNode(attr);
    }

    private static String getAttribute(Node m, String name) {
        return m.getAttributes().getNamedItem(name).getTextContent();
    }

    private static void addPlayers(Element players) {
        for (Player p : Galaxy.getPlayers()) {
            Element player = doc.createElement("Player");
            players.appendChild(player);

            if (p.isAI()) {
                AI a = (AI) p;
                addAttribute("AI", a.AIclass(), player);
            } else {
                addAttribute("AI", "0", player);
            }

            addAttribute("mainPlayer", p.isMainPlayer() ? "1" : "0", player);
            addAttribute("color", p.getColor().toString(), player);
            addAttribute("id", Integer.toString(p.getId()), player);
            addAttribute("active", p.isActive() ? "1" : "0", player);
            addAttribute("shipType", p.getShipType(), player);
            addAttribute("effectivesPercent", Double.toString(p.getEffectivesPercent()), player);
        }
    }

    private static void addPlanets(Element planets) {
        for (Planet p : Galaxy.getPlanets()) {
            Element planet = doc.createElement("Planet");
            planets.appendChild(planet);

            addAttribute("type", p.assetReference(), planet);
            addAttribute("id", Integer.toString(p.getId()), planet);
            addAttribute("ownerId", Integer.toString(p.getOwner().getId()), planet);
            addAttribute("x", Double.toString(p.getPosX()), planet);
            addAttribute("y", Double.toString(p.getPosY()), planet);
            addAttribute("size", Double.toString(p.getSize()), planet);
            addAttribute("shipCapacity", Integer.toString(p.getShipCapacity()), planet);
            addAttribute("shipsPerTick", Double.toString(p.getShipsPerTick()), planet);
            addAttribute("productionProgression", Double.toString(p.getProductionProgression()), planet);
            addAttribute("shipType", p.getShipType(), planet);
            addAttribute("shipsCount", Integer.toString(p.getNbShip()), planet);
        }
    }

    private static void addMissions(Element missions) {
        for (Mission m : Game.missions) {
            Element mission = doc.createElement("Mission" + m.getId());
            missions.appendChild(mission);

            addAttribute("addQueue", Integer.toString(m.getAddQueue()), mission);
            addAttribute("owner", Galaxy.getPlayers().contains(m.getOwner()) ? Integer.toString(m.getOwner().getId()) : "None", mission);
            addAttribute("origin", Integer.toString(m.getOriginPlanet().getId()), mission);
            addAttribute("destination", Integer.toString(m.getDestinationPlanet().getId()), mission);
            addAttribute("mission", m.getMission(), mission);
            addAttribute("squadSize", Integer.toString(m.getSquadSize()), mission);

            Element squads = doc.createElement("Squads");
            mission.appendChild(squads);
            addSquads(squads, m);
        }
    }

    private static void addSquads(Element squads, Mission m) {
        for (Squad s : m.getSquads()) {
            Element squad = doc.createElement("Squad" + m.getId());
            squads.appendChild(squad);

            Element ships = doc.createElement("Ships");
            squad.appendChild(ships);
            addShips(ships, s.getShips());
        }
    }

    private static void addShips(Element ships, ArrayList<Ship> shipsList) {
        for (Ship s : shipsList) {
            Element ship = doc.createElement("Ship");
            ships.appendChild(ship);

            addAttribute("currentSpeed", Double.toString(s.getCurrentSpeed()), ship);
            addAttribute("type", "planets.entities.ship." + Character.toUpperCase(s.assetReference().charAt(0)) + s.assetReference().substring(1), ship);
            addAttribute("x", Double.toString(s.getPosX()), ship);
            addAttribute("y", Double.toString(s.getPosY()), ship);
        }
    }

    public static String getSaveName(File f) {
        String name = f.getName();
        return name.substring(0, name.length() - save_ext.length());
    }

    public static ArrayList<File> getSaveFiles() {
        File saveFolder = new File(SaveManager.save_folder);
        ArrayList<File> files = new ArrayList<>();
        for (File f : saveFolder.listFiles()) {
            String name = f.getName();
            if (name.substring(name.length() - (save_ext.length())).equals(save_ext)) {
                System.out.println("Added button " + SaveManager.getSaveName(f));
                files.add(f);
            }
        }

        return files;
    }
}
