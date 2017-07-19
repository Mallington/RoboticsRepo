/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMath;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.rectangle;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author malli
 */
public class RProcess {
    
    
      public static Mat drawCanny(Mat frame,double threshold){
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));
        
        Imgproc.Canny(detectedEdges, detectedEdges, threshold, threshold * 3, 3, false);
        return detectedEdges;
    }
    
    
      public static Mat cropToFace(Mat frame, CascadeClassifier faceDetector) {
        MatOfRect faceDetections = new MatOfRect();

        faceDetector.detectMultiScale(frame, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        Rect toCrop = null;
        for (Rect rect : faceDetections.toArray()) {

            rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 255, 0));
            toCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
        }

        try {
            frame = new Mat(frame, toCrop);
        } catch (Exception e) {

        }
        return frame;
    }
    
    
     public static List<Point> findOuter(MatOfPoint in){
        try{
            List<Point> toProcess = in.toList();
            Rect bounds = Imgproc.boundingRect(in);
        List<Point> arr = new ArrayList<Point>();
            System.out.println("1");
            Point startPos = new Point(bounds.x+((double)bounds.width/2.0),bounds.y+((double)bounds.height/2.0));
        for(int i =0; i<4; i++) arr.add(startPos);
            System.out.println("2");
        for( Point p: toProcess){
            boolean notTaken =true;
        if(p.y<arr.get(0).y &&notTaken) {
            arr.set(0, p); notTaken =false; 
        }
        if(p.y>arr.get(1).y&&notTaken) {
            arr.set(1, p); notTaken =false;
        }
        if(p.x<arr.get(2).x&&notTaken){
            arr.set(2, p); notTaken =false;
        }
         if(p.x>arr.get(3).x&&notTaken){
             arr.set(3, p); notTaken =false;
         }
            /*
             if(p.y<=bounds.y) arr.set(0, p);
        if(p.y>=bounds.height) arr.set(1, p);
        if(p.x<=bounds.x) arr.set(2, p);
         if(p.x>=bounds.width) arr.set(3, p);*/
            
    }
        if(arr.get(2)==startPos) {
            System.out.println("F");
            RVector vect = new RVector(arr.get(2), arr.get(3));
            arr.set(2,vect.applyVector(arr.get(2), -1));
        }
        
        
     return arr;
     } catch(Exception e) {System.out.println("Nope"); return null;}
        
    }
     
     public static boolean isBlack(Mat toPick, Point p){
         double[] cols = toPick.get((int)p.x,(int) p.y);
         System.out.println("At "+p.x+", "+p.y+" :: "+(int)p.x+", "+(int) p.y);
         System.out.println(cols[0]+","+cols[1]+","+cols[2]);
         
         return true;
     }
     
}
