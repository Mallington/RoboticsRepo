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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
public class MotorController {
    public MotorController() throws IOException{
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
      }
    }
    public void halt(){
        
    }
    public void start(){
        
    }
    public void setMotor(int motNum, int motorPower){
        
    }
    
    public static void main(String args[]) throws IOException{
        MotorController m = new MotorController();
    }
    
}
