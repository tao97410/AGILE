package h4131.calculus;

import h4131.model.Intersection;

public class InterCompare implements Comparable<InterCompare> {

    public double distance;
    public Intersection intersection;

    public InterCompare(double aDistance, Intersection anIntersection) {
        this.distance = aDistance;
        this.intersection = anIntersection;
    }

    @Override
    public int compareTo(InterCompare o) {
        return ((Double) distance).compareTo(o.distance);
    }

}
