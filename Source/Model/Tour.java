package model;
import java.util.ArrayList;
import java.util.List;

public class Tour {
    private List<Segment> course;
    private List<DeliveryPoint> deliveryPoints;
    private Courier courier;

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

    public void addSegment(Segment segment){
        this.course.add(segment);
    }

    public void addDeliveryPoint(DeliveryPoint point){
        this.deliveryPoints.add(point);
    }

}
