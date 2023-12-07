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

          for(DeliveryPoint d : nodes){
            result.append("Node : (")
            .append(d.getPlace().getLatitude())
            .append(" , ")
            .append(d.getPlace().getLongitude())
            .append(") at ")
            .append(d.getTime())
            .append("\n");
        }

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
