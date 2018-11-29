/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.utils;

/**
 *
 * @author Adri
 */
public class GameUtils {

    public static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) ((Math.random() * ((max - min) + 1)) + min);
        return x;
    }

    public static boolean lineCrossingCircle(double x1, double y1, double x2, double y2, double cx, double cy, double radius) {
        double a = x1 - x2;
        double b = y1 = y2;
        double x = Math.sqrt(a * a + b * b);
        return (Math.abs((cx - x1) * (y2 - y1) - (cy - y1) * (x2 - x1)) / x <= radius);
    }
}
