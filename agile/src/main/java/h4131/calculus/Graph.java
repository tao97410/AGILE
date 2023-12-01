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

}
