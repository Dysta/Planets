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
import planets.ResourcesManager;
import planets.Sprite;
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
            StreamResult result = new StreamResult(new File(SaveManager.save_folder + savename + ".xml"));

            transformer.transform(source, result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public static void load(Game game, String save_name) throws ParserConfigurationException, SAXException, IOException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Game.toggleFreeze();
            File file = new File(SaveManager.save_folder + save_name + ".xml");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            double width = Double.parseDouble(document.getElementsByTagName("game").item(0).getAttributes().getNamedItem("width").getTextContent());
            double height = Double.parseDouble(document.getElementsByTagName("game").item(0).getAttributes().getNamedItem("height").getTextContent());

            double borderMargin = Double.parseDouble(document.getElementsByTagName("game").item(0).getChildNodes().item(0).getAttributes().getNamedItem("borderMargin").getTextContent());

            Node players = document.getElementsByTagName("game").item(0).getChildNodes().item(1);
            Node planets = document.getElementsByTagName("game").item(0).getChildNodes().item(2);
            Node missions = document.getElementsByTagName("game").item(0).getChildNodes().item(3);
            
            ArrayList<Player> playersArrayList = new ArrayList<>();            
            ArrayList<Planet> planetsArrayList = new ArrayList<>();

            Map<String, Player> playersList = new HashMap<>();
            for (Node p : new IteratableNodeList(players.getChildNodes())) {
                Player pl = new Player(
                        p.getAttributes().getNamedItem("shipType").getTextContent(), 
                        p.getAttributes().getNamedItem("mainPlayer").getTextContent().equals("1"),
                        Double.parseDouble(p.getAttributes().getNamedItem("effectivesPercent").getTextContent()),
                        Color.web(p.getAttributes().getNamedItem("color").getTextContent()), 
                        p.getAttributes().getNamedItem("active").getTextContent().equals("1"));
                
                pl.setId(Integer.parseInt(p.getAttributes().getNamedItem("id").getTextContent()));
                playersList.put(Integer.toString(pl.getId()), pl);
                playersArrayList.add(pl);
            }

            Map<String, Planet> planetsList = new HashMap<>();
            for (Node p : new IteratableNodeList(planets.getChildNodes())) {
                String t = p.getAttributes().getNamedItem("type").getTextContent();
                String classRef = "planets.entities.planet." + Character.toUpperCase(t.charAt(0)) + t.substring(1);
                
                Planet pl = (Planet) Class.forName(classRef)
                        .getConstructor(Sprite.class, Player.class, double.class, double.class, double.class)
                        .newInstance(ResourcesManager.assets.get("basePlanet"), 
                                playersList.get(p.getAttributes().getNamedItem("ownerId").getTextContent()),
                                Double.parseDouble(p.getAttributes().getNamedItem("x").getTextContent()),
                                Double.parseDouble(p.getAttributes().getNamedItem("y").getTextContent()),
                                Double.parseDouble(p.getAttributes().getNamedItem("size").getTextContent())
                        );
                
                pl.loadPlanet(p.getAttributes().getNamedItem("shipType").getTextContent(), 
                        Double.parseDouble(p.getAttributes().getNamedItem("shipsPerTick").getTextContent()),
                        Integer.parseInt(p.getAttributes().getNamedItem("shipsCount").getTextContent()),
                        Integer.parseInt(p.getAttributes().getNamedItem("shipCapacity").getTextContent()),
                        Double.parseDouble(p.getAttributes().getNamedItem("productionProgression").getTextContent()));
                
                pl.setId(Integer.parseInt(p.getAttributes().getNamedItem("id").getTextContent()));
                System.out.println("Added "+pl);
                planetsList.put(Integer.toString(pl.getId()), pl);
                planetsArrayList.add(pl);
            }
            
            ArrayList<Mission> missionsList = new ArrayList<>();
            for (Node m : new IteratableNodeList(missions.getChildNodes())) {
                System.out.println(planetsList.get(m.getAttributes().getNamedItem("origin").getTextContent()));
                Mission mi = new Mission(
                        planetsList.get(m.getAttributes().getNamedItem("origin").getTextContent()),
                        planetsList.get(m.getAttributes().getNamedItem("destination").getTextContent()),
                        Integer.parseInt(m.getAttributes().getNamedItem("addQueue").getTextContent()),
                        Integer.parseInt(m.getAttributes().getNamedItem("squadSize").getTextContent()),
                        m.getAttributes().getNamedItem("mission").getTextContent()
                );
                
                missionsList.add(mi);
            }
            

            System.out.println("-- Loading --");
            System.out.println("Width: " + width);
            System.out.println("Height: " + height);
            System.out.println("borderMargin: " + borderMargin);
            Galaxy galaxy = new Galaxy(width, height, planetsArrayList, playersArrayList, borderMargin);
            game.load(galaxy, missionsList);
            Game.toggleFreeze();
    }

    private static void addAttribute(String name, String value, Element node) {
        Attr attr = doc.createAttribute(name);
        attr.setValue(value);
        node.setAttributeNode(attr);
    }

    private static void addPlayers(Element players) {
        for (Player p : Galaxy.getPlayers()) {
            Element player = doc.createElement("Player");
            players.appendChild(player);

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
            addAttribute("type", s.assetReference(), ship);
            addAttribute("x", Double.toString(s.getPosX()), ship);
            addAttribute("y", Double.toString(s.getPosY()), ship);
        }
    }
}
