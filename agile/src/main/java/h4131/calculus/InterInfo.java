package h4131.calculus;

import h4131.model.Intersection;

public class InterInfo {

    public double distance;
    public Intersection pred;
    public boolean isGrey;

    public InterInfo(double aDist, Intersection aPred, boolean aIsGrey) {
        this.distance = aDist;
        this.pred = aPred;
        this.isGrey = aIsGrey;
    }



}
