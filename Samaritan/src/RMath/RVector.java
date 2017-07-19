/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMath;

import org.opencv.core.Point;

/**
 *
 * @author malli
 */
public class RVector {
    double xDisplace;
    double yDisplace;
    public RVector(Point first, Point Second){
        this.xDisplace = Second.x- first.x;
        this.yDisplace = Second.y- first.y;
    }
    public void multiplyVector(double mag){
        xDisplace*=mag;
        yDisplace*=mag;
        
    }
    public Point applyVector(Point start, double times){
        Point end = new Point(start.x, start.y);
        end.x +=xDisplace*times;
        end.y +=yDisplace*times;
        return end;
    }
}
