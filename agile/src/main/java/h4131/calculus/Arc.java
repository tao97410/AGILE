package h4131.calculus;

import java.util.Collection;
import java.util.LinkedList;

import h4131.model.DeliveryPoint;
import h4131.model.Segment;

public class Arc {
    
    public DeliveryPoint origin;
    public DeliveryPoint destination;
    public int distance;
    public Collection<Segment> path;

    public Arc(DeliveryPoint aOrigin, DeliveryPoint aDestination, int aDistance) {
        this.origin = aOrigin;
        this.destination = aDestination;
        this.distance = aDistance;
        this.path = new LinkedList<>();
    }

}