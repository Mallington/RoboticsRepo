/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMath;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
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
import System.Records.Marker;
import java.awt.Dimension;
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
          
            Point startPos = new Point(bounds.x+((double)bounds.width/2.0),bounds.y+((double)bounds.height/2.0));
        for(int i =0; i<4; i++) arr.add(startPos);
          
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
     
     public static double[] getAvgColour(Mat toPick, Point d, int lineSize){
         double[] avg =  toPick.get((int)d.x,(int) d.y);
        
         for(int x = (int)d.x-lineSize; x< ((int)d.x+(lineSize*2)); x++) for(int y = (int)d.y-lineSize; y< ((int)d.y+(lineSize*2)); y++){
             double[] cols = toPick.get((int)d.x,(int) d.y);
             for(int i=0; i<3; i++) avg[i]= (avg[i]+ cols[i])/2;
         }
             
         
         
         
         return avg;
     }
     
     
      public static List<Marker> getMarkers(Mat edges, Mat orig) {
        Mat out = new Mat();
        List<Marker> markers = new ArrayList<Marker>();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(edges, contours, out, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.cvtColor(edges, edges, Imgproc.COLOR_GRAY2BGR);
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint cont = contours.get(i);
            MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            matOfPoint2f.fromList(cont.toList());
      
            Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            Rect rect = Imgproc.boundingRect(cont);
            long total = approxCurve.total();

            if (total == 4) {

                // Imgproc.putT
                /*
                   int z =0;
                   Point l= matOfPoint2f.toList().get(0);
                            for(Point p: matOfPoint2f.toList()){
                                
                             //   System.out.println(p.x+", "+p.y);
                                Imgproc.line(orig, l, p, new Scalar(255,0,0),2);
                               
                                        l=p;
                                z++;
               }*/
                Point la = approxCurve.toList().get(0);
                for (Point p : approxCurve.toList()) {
                    Imgproc.line(orig, la, p, new Scalar(255, 125, 60), 2);
                    la = p;
                }

                List<Point> outerPoints = RProcess.findOuter(new MatOfPoint(approxCurve.toArray()));
                markers.add(new Marker(outerPoints, new Dimension(orig.width(),orig.height())));
                int k = 0;
                for (Point p : outerPoints) {
                    Imgproc.circle(orig, p, 5, new Scalar(255, 255, 0));
                    Imgproc.putText(orig, "" + k, p, Core.FONT_HERSHEY_PLAIN, 5, new Scalar(0, 0, 255));
                    k++;
                }

                //orig = drawCircleGrid(orig, outerPoints, 10, 10);

                /*
                    Imgproc.putText(orig, "Sq Obj Det", new Point(rect.x, rect.y), Core.FONT_HERSHEY_PLAIN,1, new Scalar(0, 0, 255));
                    rectangle(orig, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));*/
            } else {
                rectangle(edges, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            }

        }

        return markers;
    }

    public static Mat drawCircleGrid(Mat frame, List<Point> corners, int height, int width) {
        RGrid grid = RGrid.findGridCoor(corners, height, width);

        for (Point c : grid.getList()) {
            //Imgproc.circle(frame, c, 7, new Scalar(200, 0, 125));
             if (RProcess.isBlack(frame, c)) {
                 
            Imgproc.circle(frame, c, 7, new Scalar(0, 0, 0));
        }
        else{
             Imgproc.circle(frame, c, 7, new Scalar(255, 255, 255));
        }
        }
        /*
        if (RProcess.isBlack(frame, grid.getPoint(2, 2))) {
            Imgproc.circle(frame, grid.getPoint(2, 2), 7, new Scalar(0, 0, 0));
        }
        else{
             Imgproc.circle(frame, grid.getPoint(2, 2), 7, new Scalar(255, 255, 255));
        }*/
        
        return frame;
    }
     
     
     public static boolean isBlack(Mat toPick, Point p){
         
         double[] cols = getAvgColour( toPick, p, 5);
          //System.out.println("At "+p.x+", "+p.y+" :: "+(int)p.x+", "+(int) p.y);
         //System.out.println(cols[0]+","+cols[1]+","+cols[2]);
         if(((cols[0]+cols[1]+cols[2])/3) >127 )return true;
         else return false;
           
   
       
         
         
         
        
         
     }
     
}
