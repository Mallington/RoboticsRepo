/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Periscope;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
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
    private ImageView primaryFace;
    @FXML
    private CheckBox GR;
    
    
    ScheduledExecutorService timer ;
    /**
     * Initializes the controller class.
     */
    
     VideoCapture capture ;
    
    @FXML
    protected void startCamera(ActionEvent event){
             CascadeClassifier faceDetector = new CascadeClassifier(new File("src\\Periscope\\lbpcascade_frontalface.xml").getAbsolutePath());
        Runnable frameGrabber = new Runnable(){
            public void run(){
                try{
            if(capture.isOpened()){
            Mat frame = new Mat();
            capture.read(frame);
            
            
             MatOfRect faceDetections = new MatOfRect();
  
    faceDetector.detectMultiScale(frame, faceDetections);

    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

    // Draw a bounding box around each face.
      Rect toCrop = null;
    for (Rect rect : faceDetections.toArray()) {
     
       rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 255, 0));
       toCrop = new Rect(rect.x,rect.y,rect.width,rect.height);
    }

            
            
            
            //Imgproc.cvtColor(frame, frame, Imgproc.COLORMAP_RAINBOW);
            try{
                frame = new Mat(frame, toCrop);
            }
            catch(Exception e){
                
            }
          
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", frame, buffer);
            Image img = new Image(new ByteArrayInputStream(buffer.toArray()));
 
            Platform.runLater(new Runnable(){public void run(){
            currentFrame.setImage(img);
               primaryFace.setImage(img);
            }});
            
            
    
            
           // frame.submat(faceDetections.toArray()[0]);
           

           
            
            
            
        }
                } catch(Exception e){System.out.println("DROPPED FRAME -----------------");}
            }
        };
        this.timer =  Executors.newSingleThreadScheduledExecutor();  
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
        
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       capture = new VideoCapture(0);
    }    
    
    public void drawFilter(Mat frame){
        if(GR.isPressed()) Imgproc.cvtColor(frame, frame, Imgcodecs.IMREAD_GRAYSCALE);
        
        
        
    }
    
    
    
}
