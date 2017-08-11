/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System.Records;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Point;

/**
 *
 * @author malli
 */
public class Marker {
    public List<Point> CORNERS = new ArrayList<Point>();
    public Dimension ORIGIN_SIZE;
    public Marker(List<Point> corners, Dimension originImageSize){
        CORNERS = corners;
        ORIGIN_SIZE = originImageSize;
    }
}
