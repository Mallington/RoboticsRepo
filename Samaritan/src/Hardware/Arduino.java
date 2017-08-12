/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hardware;

import java.util.ArrayList;
import java.util.List;
import com.fazecast.jSerialComm.*;

/**
 *
 * @author mathew
 */
public class Arduino {

    public Arduino(int port) {

    }

    public static int getPortValue(String descriptor) {
        List<Port> bob = listPorts();
        for (Port alice : bob) {
            if (alice.descriptor.contains(descriptor)) {
                return alice.port;
            }
        }

        return 0;
    }

    public static List<Port> listPorts() {
        List<Port> portNames = new ArrayList<Port>();
        int i = 0;
        for (SerialPort port : SerialPort.getCommPorts()) {
            Port p = new Port();
            p.port = i++;
            p.descriptor = port.getSystemPortName();
            portNames.add(p);
        }
        return portNames;
    }

    public static void main(String args[]) {
        int i = 0;
        for (Port name : Arduino.listPorts()) {
            System.out.println(name);
        }

        Arduino a = new Arduino(Arduino.getPortValue("cu.usbmodem1421"));

    }

    public static class Port {

        int port;
        String descriptor;
        
        @Override
        public String toString(){
            return port + " : " + descriptor;
        }
    }
}
