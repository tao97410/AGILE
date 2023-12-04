package h4131.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import h4131.calculus.Arc;
import h4131.calculus.Graph;

public class Tour {
    private List<Segment> course;
    private List<DeliveryPoint> deliveryPoints;
    private Courier courier;

    public Tour(Courier aCourier) {
        this.courier = aCourier;
        this.course = new ArrayList<>();
        this.deliveryPoints = new ArrayList<>();
    }

    
    public int TSPDynamicProgramming(Graph initialGraph,LinkedList<DeliveryPoint> d, Map map){
         initialGraph=map.getGraphFromPoints(d);
         Collection<DeliveryPoint> nodes= initialGraph.nodes;
         Collection<Arc> arcs=initialGraph.arcs;
         


        return 0;

    }


    // //Creates the cost function for the TSP
    //  private int[][] createCost(Collection<DeliveryPoint> nodes, Collection<Arc> arcs){
    //     Map<pair<DeliveryPoint,DeliveryPoint>,double> cost= new Map();
    //     for(int i=0 ; i<nodes.size() ;i++){
    //         for(int j=0 ; j<nodes.size() ; j++){
    //             if(i==j){
    //                 cost[i][j]=0;
    //             }else{
    //                 if
    //                 cost[i][j]=arc[0].distance;
    //             }
    //         }
    //     }

    //     return cost;

    // }
    /**
     * @return List<Segment> return the course
     */
    public List<Segment> getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(List<Segment> course) {
        this.course = course;
    }

    /**
     * @return Courier return the courier
     */
    public Courier getCourier() {
        return courier;
    }

    /**
      * @param courier the courier to set
      */
    public void setCourier(Courier courier) {
         this.courier = courier;
    }

    public void addSegment(Segment segment){
        this.course.add(segment);
    }

    public void addDeliveryPoint(DeliveryPoint point){
        this.deliveryPoints.add(point);
    }

}
