package h4131.calculus;

import h4131.model.Intersection;

public class InterInfo {

    public double distance;
    public Intersection pred;
    public boolean isGrey;

    public InterInfo(double adist,Intersection apred, boolean aIsGrey) {
        this.distance = adist;
        this.pred = apred;
        this.isGrey = aIsGrey;
    }



}
