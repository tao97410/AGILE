package h4131.calculus;

import java.util.ArrayList;
import java.util.Collection;



import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.Segment;

public class Graph {

    public Collection<DeliveryPoint> nodes;
    public Collection<Arc> arcs;

    public Graph() {
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();
    }

    // public String toString() {
    //     StringBuilder result = new StringBuilder();
    //     for (Arc a : arcs) {
    //         result.append("* From (" 
    //             + a.origin.getPlace().getLatitude() 
    //             + ", " 
    //             + a.origin.getPlace().getLongitude() 
    //             + ") to (" 
    //             + a.destination.getPlace().getLatitude() 
    //             + ", " 
    //             + a.destination.getPlace().getLongitude() 
    //             + "): " + a.distance + "\n");
    //     }
    //     return result.toString();
    // }

    @Override
    public boolean equals(Object o){
        boolean res = true;
        
        if(!(o instanceof Graph))
            return false;
        else{
            Graph g  = (Graph) o;
            for(DeliveryPoint d : nodes){
                if (!g.nodes.contains(d)) res=false;
            }
            for(Arc a : arcs){
                if (!g.arcs.contains(a)) res=false;
            }
        }

        return res;
        
    }

    

}
