/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMath;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.opencv.core.Point;

/**
 *
 * @author malli
 */
public class RGrid {
    private int width;
    private int height;
   
    private List<Point> coor = new ArrayList<Point>();
    
    public RGrid (int width, int height){
        this.width = width;
        this.height = height; 
       
        for(int i =0;(this.width*this.height)>i; i++)coor.add(new Point());  
    }
    
    public void setPoint(int x, int y, Point p){
        coor.set( x+ (y*(width)), p);   
    }
    
    public Point getPoint(int x, int y){
        return coor.get( x+ (y*(width)));
        
    }
    public static void main(String args[]){
        RGrid g = new RGrid(10,10);
        g.setPoint(9, 9, new Point());
       g.getStream();
    }
    public List<Point> getList(){
       return new ArrayList<Point>(coor);
    }
    public ListIterator<Point> getStream(){
        return coor.listIterator();
    }
    
     public static RGrid findGridCoor(List<Point> corners, int height, int width) {
        RGrid grid = new RGrid(height, width);

        RVector top = new RVector(corners.get(2), corners.get(0));
        RVector bottom = new RVector(corners.get(1), corners.get(3));

        for (int x = 0; x < width; x++) {
            double px = ((double) (1 + (x * 2))) / ((double) width * 2);

            Point a = top.applyVector(corners.get(2), px);
            Point b = bottom.applyVector(corners.get(1), px);
            for (int y = 0; y < height; y++) {
                double py = ((double) (1 + (y * 2))) / ((double) height * 2);
                RVector heightV = new RVector(a, b);
                Point coor = heightV.applyVector(a, py);
                grid.setPoint(x, y, coor);
            }
        }

        return grid;
    }
    
    
    
    
}
