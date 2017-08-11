/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Periscope;

import AI.Samaritan;
import RMath.*;
import System.Records.Marker;

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
import javafx.scene.Camera;
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
import org.opencv.videoio.Videoio;

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
    private Slider aper;
    
    ScheduledExecutorService timer;
    
    
    Samaritan samaritanAI;
    
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
                    //    int toSet = Videoio.CAP_PROP_EXPOSURE;
       //  System.out.println("Before, BExp: "+capture.get(toSet));
                       // ajustWebcam();
                        frame = new Mat();
                        capture.read(frame);
                        frame = drawFilter(frame, faceDetector);

                        MatOfByte buffer = new MatOfByte();
                        Imgcodecs.imencode(".png", frame, buffer);
                        Image img = new Image(new ByteArrayInputStream(buffer.toArray()));
                       // System.out.println(threshold.getValue());

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
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 50, TimeUnit.MILLISECONDS);
       // timer.sc

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

          // try{  System.loadLibrary("C:\\Users\\malli\\OneDrive\\Documents\\RoboticsRepo\\Samaritan\\OpenCV\\opencv\\build\\bin\\opencv_ffmpeg320_64.dll"); } catch(Exception e){System.out.println(e.getCause().getMessage());}
        capture = new VideoCapture(0);
        samaritanAI = new Samaritan();
        //int toSet = Videoio.CAP_PROP_EXPOSURE;
        // System.out.println("Before, BExp: "+capture.get(toSet));
      
       
       // capture.open("Marker.mp4");
      // if(!capture.open("C:\\Users\\malli\\OneDrive\\Documents\\RoboticsRepo\\Samaritan\\src\\Periscope\\Marker.mp4"))System.out.println("File Open failed");
      
    }

    public Image renderFrame(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        Image img = new Image(new ByteArrayInputStream(buffer.toArray()));
        return img;
    }

    
    public void ajustWebcam(){
      
        
        int toSet = Videoio.CAP_PROP_EXPOSURE;
        capture.set(toSet,aper.getValue());
        System.out.println("Exp: "+capture.get(toSet));
        
    }
    
    public Mat drawFilter(Mat frame, CascadeClassifier faceDetector) {
        Mat orig = frame;
        if (GR.isSelected()) {
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
        }
        if (FD.isSelected()) {
            frame = RProcess.cropToFace(frame, faceDetector);
        }
        if (CED.isSelected()) {
            frame = RProcess.drawCanny(frame, this.threshold.getValue());
            List<Marker> markers = RProcess. getMarkers(frame,orig);
            samaritanAI.updateMarkers(markers);
        }
        return orig;
    }

   

    public void setWindowConfig() {
        cannyTools.setVisible(CED.isSelected());
        
    }

}
