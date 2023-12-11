package h4131.model;
import java.util.ArrayList;
import java.util.Collection;



public class Tour {
    private Collection<Segment> course;
    private Collection<DeliveryPoint> deliveryPoints;
    private int courierId;

    public Tour(int aCourierId) {
        this.courierId = aCourierId;
        this.course = new ArrayList<>();
        this.deliveryPoints = new ArrayList<>();
    }

    public Tour(){
        this.course = new ArrayList<>();
        this.deliveryPoints = new ArrayList<>();
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
    public int getCourierId() {
        return courierId;
    }

    /**
      * @param courier the courier to set
      */
    public void setCourier(int courierId) {
         this.courierId = courierId;
    }

    public void addSegment(Segment segment){
        this.course.add(segment);
    }

    public void addDeliveryPoint(DeliveryPoint point){
        this.deliveryPoints.add(point);
    }
    public void setDeliveryPoints(Collection<DeliveryPoint> deliveryPoints){
        this.deliveryPoints=deliveryPoints;
    }

    public Collection<DeliveryPoint> getDeliveryPoints(){
        return this.deliveryPoints;
    }

}
