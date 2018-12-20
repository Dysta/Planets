package planets.utils;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *  Some useful functions for debugging
 * 
 * @author Adri
 */
public class DebugUtils {

    /**
     * Gets all the nodes of a Parent recursively.
     * 
     * @param root the Parent to get the nodes from
     * @return the nodes
     */
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    /**
     * Adds each Node once and its children recursively
     * 
     * @param parent the parent node 
     * @param nodes the collection to add the nodes to
     */
    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllDescendents((Parent) node, nodes);
            }
        }
    }
    
    /**
     * Computes the tickrate using a collection of timestamps
     * 
     * @param ticks a timestamp collection
     * @return the tickrate
     */
    public static double tickRate(ArrayList<Long> ticks) {
        return 1000 / ((ticks.get(ticks.size()-1) - ticks.get(0)) / ticks.size());
    }
}
