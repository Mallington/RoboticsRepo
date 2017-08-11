/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import System.Records.Marker;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malli
 */
public class Samaritan {
    // All implemented currently in the VideoGUI Class
    List<Marker> LATEST_MARKERS = new ArrayList<Marker>();
    
    public Samaritan(){
        System.out.println("Samaritian starting...");
    }
    
    public void updateMarkers( List<Marker> markers){
        System.out.println("New Set of Markers:");
        
    
        for(Marker m: markers) System.out.println("Marker"+m.CORNERS.get(0)+", "+m.CORNERS.get(1)+", "+m.CORNERS.get(2)+", "+m.CORNERS.get(3));
        System.out.println("END");
    }
    
    
    
    
    
}
