package planets.utils;

/**
 *  Some useful functions for the Game
 * 
 * @author Adri
 */
public class GameUtils {

    /**
     * Gets a random number using the Random library given a range.
     * 
     * @param min the minimum number
     * @param max the maximum number
     * @return a random integer
     */
    public static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) ((Math.random() * ((max - min) + 1)) + min);
        return x;
    }

    /**
     * Checks if a line is crossing a circle, usefull to check if a ship can go straight for example.
     * 
     * @param x1 x coordinate of the line's first point
     * @param y1 y coordinate of the line's first point
     * @param x2 x coordinate of the line's second point
     * @param y2 y coordinate of the line's second point
     * @param cx x coordinate of the circle's center
     * @param cy y coordinate of the circle's center
     * @param radius circle's radius
     * @return true if the line is crossing the circle
     */
    public static boolean lineCrossingCircle(double x1, double y1, double x2, double y2, double cx, double cy, double radius) {
        double a = x1 - x2;
        double b = y1 = y2;
        double x = Math.sqrt(a * a + b * b);
        return (Math.abs((cx - x1) * (y2 - y1) - (cy - y1) * (x2 - x1)) / x <= radius);
    }
}
