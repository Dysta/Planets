/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planets.utils;

import java.util.ArrayList;

/**
 *
 * @author Adri
 */
public class Polygon {

    private final ArrayList<Point> points;

    public Polygon(ArrayList<Point> ps) {
        this.points = ps;
    }

    // adapted from the original by https://stackoverflow.com/users/134176/dean-povey
    public boolean contains(Point p) {
        int i, j;

        boolean contained = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if (((points.get(j).x - points.get(i).x) >= p.x * (p.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x) && (p.y <= points.get(i).y) != (points.get(j).y > p.y)) {
                contained = !contained;
            }
        }
        return contained;
    }
}
