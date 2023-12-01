package h4131.calculus;

import h4131.model.Intersection;

public class InterCompare implements Comparable<InterCompare> {

    public int distance;
    public Intersection intersection;

    public InterCompare(int aDistance, Intersection aIntersection) {
        this.distance = aDistance;
        this.intersection = aIntersection;
    }

    @Override
    public int compareTo(InterCompare o) {
        return ((Integer) distance).compareTo(o.distance);
    }

}
