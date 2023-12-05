package h4131.model;
import java.util.ArrayList;
import java.util.Collection;

public class Tour {
    private Collection<Segment> course;
    private Collection<DeliveryPoint> deliveryPoints;
    private long courierId;

    public Tour(long aCourierId) {
        this.courierId = aCourierId;
        this.course = new ArrayList<>();
        this.deliveryPoints = new ArrayList<>();
    }

    /**
     * @return List<Segment> return the course
     */
    public Collection<DeliveryPoint> getDeliveryPoints() {
        return deliveryPoints;
    }

    /**
     * @param course the course to set
     */
    public void setDeliveryPoints(Collection<DeliveryPoint> deliveryPoints) {
        this.deliveryPoints = deliveryPoints;
    }

    /**
     * @return List<Segment> return the course
     */
    public Collection<Segment> getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Collection<Segment> course) {
        this.course = course;
    }

    /**
     * @return Courier return the courier
     */
    public long getCourierId() {
        return courierId;
    }

    /**
      * @param courier the courier to set
      */
    public void setCourier(long courierId) {
         this.courierId = courierId;
    }

    public void addSegment(Segment segment){
        this.course.add(segment);
    }

    public void addDeliveryPoint(DeliveryPoint point){
        this.deliveryPoints.add(point);
    }

}
