package h4131.calculus;



import java.util.Collection;
import java.util.LinkedList;

import h4131.model.DeliveryPoint;
import h4131.model.Segment;

public class Arc {
    
    public DeliveryPoint origin;
    public DeliveryPoint destination;
    public double distance;
    public Collection<Segment> path;

    public Arc(DeliveryPoint aOrigin, DeliveryPoint aDestination, double aDistance) {
        this.origin = aOrigin;
        this.destination = aDestination;
        this.distance = aDistance;
        this.path = new LinkedList<>();
    }

    @Override
    public boolean equals(Object o){
        boolean res = true;
        if(!(o instanceof Arc))
            res = false;
        else{
            Arc a = (Arc) o;
            if(!origin.equals(a.origin) || !destination.equals(a.destination) || distance != a.distance)
                res = false;
        
        var iterA = this.path.iterator();
        var iterB = a.path.iterator();

        while (iterA.hasNext() && iterB.hasNext()) {
            Segment segmentA = iterA.next();
            Segment segmentB = iterB.next();

            if (!segmentA.equals(segmentB)) res = false;
        }
        }
        return res;
    }

}
