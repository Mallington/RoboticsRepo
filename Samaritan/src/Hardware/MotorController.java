/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hardware;

/**
 *
 * @author mathew
 */
import com.fazecast.jSerialComm.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
public class MotorController {
    public MotorController() throws IOException, InterruptedException{
        /*
        SerialPort[] serialArray = SerialPort.getCommPorts();
        
       for(SerialPort p: serialArray) System.out.println(p.getDescriptivePortName()+" == "+p.getSystemPortName());
       serialArray[5].openPort();
       serialArray[5].setBaudRate(9600);
       
      InputStream st = serialArray[5].getInputStream();
      BufferedInputStream r = new BufferedInputStream(st);
      while(true){
      while(st.available()>0){
          System.out.print((char)r.read());
      }
      }*/
        
        SerialPort[] serialArray = SerialPort.getCommPorts();
        int i =0;
         for(SerialPort p: serialArray) System.out.println(p.getDescriptivePortName()+" == "+p.getSystemPortName()+" port: "+(i++));
         int port = 5;
         serialArray[port].openPort();
         OutputStream out = serialArray[port].getOutputStream();
         
         
         
         
         BufferedOutputStream outs = new BufferedOutputStream(out);
        Thread.sleep(5000);
         outs.write(49);
         outs.write(55);
         outs.flush();
         outs.close();
         BufferedInputStream sin = new BufferedInputStream(serialArray[port].getInputStream());
         
         while(true){
             while(sin.available()>0){
                 System.out.print((char)sin.read());
             }
         }
         
         
         
    }
    public void halt(){
        
    }
    public void start(){
        
    }
    public void setMotor(int motNum, int motorPower){
        List<Integer> params = new ArrayList<Integer>();
        params.add(motNum);
        
    }
    
    
    public static void main(String args[]) throws IOException, InterruptedException{
        MotorController m = new MotorController();
    }
    
}
