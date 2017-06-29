/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Samaritan;

import Periscope.Periscope;

/**
 * @author 160165
 */
public class Samaritan {
    Periscope p;

    Samaritan() {
        StartUpFunctions startUpFunctions = new StartUpFunctions();
        startUpFunctions.initialise(p);
        this.p = startUpFunctions.getPeriscope();
        p.main();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Samaritan();
    }

    class StartUpFunctions {
        Periscope p;

        void initialise(Periscope p) {
            p = new Periscope();
            this.p = p;
            System.out.println("This is starting up");
        }

        public Periscope getPeriscope() {
            return p;
        }
    }
}
