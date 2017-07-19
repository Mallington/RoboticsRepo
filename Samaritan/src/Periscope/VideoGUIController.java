/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Periscope;

import RMath.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.rectangle;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 * FXML Controller class
 *
 * @author Mathew
 */
public class VideoGUIController implements Initializable {

    @FXML
    private Button button;
    @FXML
    private ImageView currentFrame;
    @FXML
    private CheckBox GR;
    @FXML
    private CheckBox CED;
    @FXML
    private CheckBox FD;
     @FXML
    private AnchorPane cannyTools;
      @FXML
    private Slider threshold;
      @FXML
     private Slider min;
        @FXML
    private Slider max;
    ScheduledExecutorService timer;
    /**
     * Initializes the controller class.
     */

    VideoCapture capture;
    Mat frame;
    @FXML
    protected void startCamera(ActionEvent event) {
        CascadeClassifier faceDetector = new CascadeClassifier(new File("src\\Periscope\\lbpcascade_frontalface.xml").getAbsolutePath());
        Runnable frameGrabber = new Runnable() {
            public void run() {
                try {
                    setWindowConfig();
                    if (capture.isOpened()) {
                        frame = new Mat();
                        capture.read(frame);
                        frame =drawFilter(frame,faceDetector);

                        
                        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        Image img = new Image(new ByteArrayInputStream(buffer.toArray()));
                        System.out.println(threshold.getValue());

                        Platform.runLater(new Runnable() {
                            public void run() {
                                currentFrame.setImage(img);
                               
                            }
                        });

                        // frame.submat(faceDetections.toArray()[0]);
                    }
                } catch (Exception e) {
                    System.out.println("DROPPED FRAME -----------------");
                }
            }
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0);
    }

    
    public Image renderFrame(Mat frame){
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        Image img = new Image(new ByteArrayInputStream(buffer.toArray()));
        return img;
    }
    
    
   
    
    public Mat drawFilter(Mat frame,CascadeClassifier faceDetector ) {
        Mat orig = frame;
        if (GR.isSelected()) Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
        if(FD.isSelected()) frame =RProcess.cropToFace( frame, faceDetector);
        if(CED.isSelected()){
            frame = RProcess.drawCanny(frame,this.threshold.getValue());
            orig= detectShape(frame,orig);
        }
return orig;
    }
    
   
    
    public  Mat detectShape(Mat edges,Mat orig){
        Mat out = new Mat();
        
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(edges, contours, out, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
       
      
         Imgproc.cvtColor(edges, edges, Imgproc.COLOR_GRAY2BGR);
        for(int i=0; i<contours.size(); i++){
              MatOfPoint cont = contours.get(i);
            MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            matOfPoint2f.fromList(cont.toList());
              Imgproc.approxPolyDP(matOfPoint2f, approxCurve, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
              Rect rect = Imgproc.boundingRect(cont);  
              long total = approxCurve.total();
            
              
            
              
              
               if(total==4){
                   
                   
                  
                   
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
                     for(Point p: approxCurve.toList()){
                          Imgproc.line(orig, la, p, new Scalar(255,125,60),2);
                          la =p;
                     }     
                            
                            List<Point> outerPoints = RProcess.findOuter(new MatOfPoint(approxCurve.toArray()));
                            int k =0;
                            for(Point p : outerPoints)  {
                                Imgproc.circle(orig,p, 5, new Scalar(255,255,0));
                                Imgproc.putText(orig, ""+k, p, Core.FONT_HERSHEY_PLAIN,5, new Scalar(0, 0, 255));
                                k++;
                            }
                            
                            orig = drawCircleGrid(orig,outerPoints, 10,10);
                            
                          
                            /*
                    Imgproc.putText(orig, "Sq Obj Det", new Point(rect.x, rect.y), Core.FONT_HERSHEY_PLAIN,1, new Scalar(0, 0, 255));
                    rectangle(orig, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));*/
                   
               }
               else{
                    rectangle(edges, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
               }
          
       
       
        }
      
      
       
      
      
        
     return orig;   
    }
   
   
    public Mat drawCircleGrid(Mat frame,List<Point> corners, int height, int width){
    RGrid grid = RGrid.findGridCoor(corners,height ,width);
  
    
    for(Point c: grid.getList() ) Imgproc.circle(frame,c, 7, new Scalar(200, 0, 125));
     if( RProcess.isBlack(frame, grid.getPoint(2, 2)))Imgproc.circle(frame,grid.getPoint(2, 2), 7, new Scalar(0, 0, 0));
        return frame;
    }
    
    public void setWindowConfig(){
        cannyTools.setVisible(CED.isSelected());
        
    }
    
    

}
