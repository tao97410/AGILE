package h4131.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import h4131.calculus.Arc;
import h4131.calculus.CompleteGraph;

public class Tour {
    private List<Segment> course;
    private List<DeliveryPoint> deliveryPoints;
    private Courier courier;
    public Tour(){
        this.course = new ArrayList<>();
        this.deliveryPoints = new ArrayList<>();
    }
    public Tour(Courier aCourier) {
        this.courier = aCourier;
        this.course = new ArrayList<>();
        this.deliveryPoints = new ArrayList<>();
    }

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
    public void setDeliveryPoints(List<DeliveryPoint> deliveryPoints){
        this.deliveryPoints=deliveryPoints;
    }

}
