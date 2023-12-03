package h4131.calculus;

import java.util.ArrayList;
import java.util.Collection;

import h4131.model.DeliveryPoint;

public class Graph {

    public Collection<DeliveryPoint> nodes;
    public Collection<Arc> arcs;

    public Graph() {
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Arc a : arcs) {
            result.append("* From (" 
                + a.origin.getPlace().getLatitude() 
                + ", " 
                + a.origin.getPlace().getLongitude() 
                + ") to (" 
                + a.destination.getPlace().getLatitude() 
                + ", " 
                + a.destination.getPlace().getLongitude() 
                + "): " + a.distance + "\n");
        }
        return result.toString();
    }

}
