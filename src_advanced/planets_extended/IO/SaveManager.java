package planets_extended.IO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import planets_extended.Planets;
import planets_extended.ResourcesManager;
import planets_extended.Sprite;
import planets_extended.entities.AIs.AI;
import planets_extended.entities.AIs.BaseAI;
import planets_extended.entities.Galaxy;
import planets_extended.entities.Mission;

import planets_extended.entities.Player;
import planets_extended.entities.Squad;
import planets_extended.entities.planet.Planet;
import planets_extended.entities.ship.Ship;
import planets_extended.utils.IteratableNodeList;
import planets_extended.windows.Game;
import planets_extended.ui.SelectPercentage;

/**
 * Manage savegames : Save and Load as XML.
 *
 * @author Adri
 */
public class SaveManager {

    /**
     * Save game directory, should always end with a /.
     */
    private final static String SAVE_FOLDER = "./saves/";

    /**
     * The savegame's file extension.
     */
    private final static String SAVE_EXT = ".planets_extended";

    /**
     * Legacy savegame file extension.
     */
    private final static String SAVE_EXT_LEGACY = ".planets";

    /**
     * The current working Document object to generate XML.
     */
    private static Document doc;

    /**
     * Saves the current game state into an XML File.
     *
     * @param game The game state to save
     * @param savename The name of the savefile
     */
    public static void save(Game game, String savename) {
        try {
            if (!checkSaveFolder()) {
                return;
            }

            // Create a Document Factory in order to properly generate an XML file
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            doc = docBuilder.newDocument();

            // Create the main container along with some basic information
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);
            addAttribute("width", Double.toString(game.WIDTH), rootElement);
            addAttribute("height", Double.toString(game.HEIGHT), rootElement);

            // Add the Galaxy in Game and its main attributes. Not a container.
            Element galaxy = doc.createElement("Galaxy");
            rootElement.appendChild(galaxy);
            addAttribute("borderMargin", Double.toString(Galaxy.borderMargin), galaxy);

            // Add the players in Game
            Element players = doc.createElement("Players");
            rootElement.appendChild(players);
            addPlayers(players);

            // Add the planets_extended in Game
            Element planets_extended = doc.createElement("Planets");
            rootElement.appendChild(planets_extended);
            addPlanets(planets_extended);

            // Add the missions in Game
            Element missions = doc.createElement("Missions");
            rootElement.appendChild(missions);
            addMissions(missions);

            // Generate the XML and create/overwrite the existing file.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(SaveManager.SAVE_FOLDER + savename + SAVE_EXT));

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException pce) {
        }
    }

    /**
     * Reads the savefile from its name and replaces the current Game by the
     * newly generated one.
     *
     * @param save_name The name of the save file.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void load(String save_name) throws ParserConfigurationException, SAXException, IOException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Game.setFreeze(true);
        if (!checkSaveFolder()) {
            return;
        }
        // Fetch the desired savefile
        File file = new File(SaveManager.SAVE_FOLDER + save_name + SAVE_EXT);
        if(!file.exists()) {
            file = new File(SaveManager.SAVE_FOLDER + save_name + SAVE_EXT_LEGACY);
        }

        // Create a Document Factory to parse the XML formatted file.
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        // This is the Game container
        Node gameNode = document.getElementsByTagName("game").item(0);

        double width = Double.parseDouble(getAttribute(gameNode, "width"));
        double height = Double.parseDouble(getAttribute(gameNode, "height"));

        // Create new game
        Planets.startGame(1, 1);
        Game game = Planets.game;

        // Get all usefull containers
        Node galaxyNode = gameNode.getChildNodes().item(0);
        Node players = gameNode.getChildNodes().item(1);
        Node planets_extended = gameNode.getChildNodes().item(2);
        Node missions = gameNode.getChildNodes().item(3);

        double borderMargin = Double.parseDouble(getAttribute(galaxyNode, "borderMargin"));

        // Prepare the new Galaxy's entities collections
        ArrayList<Player> playersArrayList = new ArrayList<>();
        ArrayList<Planet> planets_extendedArrayList = new ArrayList<>();

        // Parse all players from the associated container
        Map<String, Player> playersList = new HashMap<>();
        for (Node p : new IteratableNodeList(players.getChildNodes())) {
            Player pl;

            // If this player is an AI, use its AI type instead
            if (!"0".equals(p.getAttributes().getNamedItem("AI").getTextContent())) {
                pl = new BaseAI(Color.web(getAttribute(p, "color")));
            } else {
                // Otherwise, create an usual player.
                boolean active = getAttribute(p, "active").equals("1");
                boolean mainplayer = getAttribute(p, "mainPlayer").equals("1");
                pl = new Player(
                        getAttribute(p, "shipType"),
                        mainplayer,
                        Double.parseDouble(getAttribute(p, "effectivesPercent")),
                        active ? Color.web(getAttribute(p, "color")) : Color.GREY,
                        active);

                if(mainplayer) {
                    Game.mainPlayer = pl;
                }
            }
            // Set its it so that we can associate him with his belongings later on
            pl.setId(Integer.parseInt(getAttribute(p, "id")));

            // Add the reference to both an indexed array and the final reference.
            // We use a HashMap to be able to get by index while parsing, but in the end
            // only the collection will be needed by the game.
            playersList.put(Integer.toString(pl.getId()), pl);
            playersArrayList.add(pl);
        }

        // This bloc is very similar to the previous one, but for Planets
        Map<String, Planet> planets_extendedList = new HashMap<>();
        for (Node p : new IteratableNodeList(planets_extended.getChildNodes())) {
            String t = getAttribute(p, "type");

            String classRef = "planets_extended.entities.planet." + Character.toUpperCase(t.charAt(0)) + t.substring(1);

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

            planets_extendedList.put(Integer.toString(pl.getId()), pl);
            planets_extendedArrayList.add(pl);
        }

        // Again, but for missions and its contents
        ArrayList<Mission> missionsList = new ArrayList<>();
        for (Node m : new IteratableNodeList(missions.getChildNodes())) {

            Planet origin = planets_extendedList.get(getAttribute(m, "origin"));
            Planet destination = planets_extendedList.get(getAttribute(m, "destination"));

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

        // Call the galaxy constructor with the parsed elements
        Galaxy galaxy = new Galaxy(width, height, planets_extendedArrayList, playersArrayList, borderMargin);

        // Call game.load to start a prepared game
        game.load(galaxy, missionsList);
        Game.selectP = new SelectPercentage(width,height);
        
        // Unfreeze the game, ready to go.
        Game.toggleFreeze();
    }

    /**
     * This method is only useful for preventing code duplication.
     * 
     * @param name Name of the new attribute
     * @param value Value of the new attribute
     * @param node The Node receiving the new attribute
     */
    private static void addAttribute(String name, String value, Element node) {
        Attr attr = doc.createAttribute(name);
        attr.setValue(value);
        node.setAttributeNode(attr);
    }

    /**
     * This method is only useful for preventing code duplication.
     * 
     * @param m The source Node
     * @param name The searched attributes' name
     * @return 
     */
    private static String getAttribute(Node m, String name) {
        return m.getAttributes().getNamedItem(name).getTextContent();
    }

    /**
     * Add all players' nodes with their attributes to the given node
     * 
     * @param players The node to add players to
     */
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

    /**
     * Add all planets_extended' nodes with their attributes to the given node
     * 
     * @param planets_extended The node to add planets_extended to
     */
    private static void addPlanets(Element planets_extended) {
        for (Planet p : Galaxy.getPlanets()) {
            Element planet = doc.createElement("Planet");
            planets_extended.appendChild(planet);

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

    /**
     * Add all missions' nodes with their attributes to the given node
     * 
     * @param planets_extended The node to add missions to
     */
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


    /**
     * Add all squads' nodes with their attributes to the given node
     * 
     * @param planets_extended The node to add squads to
     */
    private static void addSquads(Element squads, Mission m) {
        for (Squad s : m.getSquads()) {
            Element squad = doc.createElement("Squad" + m.getId());
            squads.appendChild(squad);

            Element ships = doc.createElement("Ships");
            squad.appendChild(ships);
            addShips(ships, s.getShips());
        }
    }

    
    /**
     * Add all ships' nodes with their attributes to the given node
     * 
     * @param planets_extended The node to add ships to
     */
    private static void addShips(Element ships, ArrayList<Ship> shipsList) {
        for (Ship s : shipsList) {
            Element ship = doc.createElement("Ship");
            ships.appendChild(ship);

            addAttribute("currentSpeed", Double.toString(s.getCurrentSpeed()), ship);
            addAttribute("type", "planets_extended.entities.ship." + Character.toUpperCase(s.assetReference().charAt(0)) + s.assetReference().substring(1), ship);
            addAttribute("x", Double.toString(s.getPosX()), ship);
            addAttribute("y", Double.toString(s.getPosY()), ship);
        }
    }

    /**
     * Returns the name of a file whose extention matches SaveManager.SAVE_EXT
     * 
     * @param f The file to return the name from
     * @return The file name
     */
    public static String getSaveName(File f) {
        String name = f.getName();
        String ret = name.substring(0, name.length() - SAVE_EXT.length());
        if(ret.length() > 0) {
            return ret;
        } else {
            ret = name.substring(0, name.length() - SAVE_EXT_LEGACY.length());
            if(ret.length() > 0) {
                return ret;
            } else {
                return "Unrecognized file";
            }
        }
    }

    /**
     * Get all Files whose extention matches SaveManager.SAVE_EXT in the directory SaveManager.SAVE_FOLDER. It also supports legacy save files from the base version.
     * @return A list of savefiles
     */
    public static ArrayList<File> getSaveFiles() {
        ArrayList<File> files = new ArrayList<>();
        if (!checkSaveFolder()) {
            return files;
        }
        File saveFolder = new File(SaveManager.SAVE_FOLDER);
        for (File f : saveFolder.listFiles()) {
            String name = f.getName();
            if (name.substring(name.length() - (SAVE_EXT.length())).equals(SAVE_EXT) || name.substring(name.length() - (SAVE_EXT_LEGACY.length())).equals(SAVE_EXT_LEGACY)) {
                files.add(f);
            }
        }

        return files;
    }

    /**
     * Checks whether the save folder exists, if not, creates it.
     * @return whether the savefolder is ready or not.
     */
    public static boolean checkSaveFolder() {
        File dir = new File(SaveManager.SAVE_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.exists() && dir.isDirectory();
    }
}
