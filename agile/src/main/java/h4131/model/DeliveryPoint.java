package h4131.model;


public class DeliveryPoint {
    private Intersection place;
    private TimeWindow time;

    public DeliveryPoint(Intersection aPlace, TimeWindow aTime){
        this.place = aPlace;
        this.time = aTime;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Place: (" + place.getLatitude() + ", " + place.getLongitude() + ") "
            + "\nTime window: " + time + "\n");
        return result.toString();
    }

    /**
     * @return Intersection return the place
     */
    public Intersection getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(Intersection place) {
        this.place = place;
    }

    /**
     * @return TIME_WINDOW return the time
     */
    public TimeWindow getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(TimeWindow time) {
        this.time = time;
    }


    @Override
    public boolean equals(Object o){
        
        if(!(o instanceof DeliveryPoint))
            return false;
        else{
            DeliveryPoint d  = (DeliveryPoint) o;
            return (place.equals(d.getPlace()) && time == d.getTime());
        }
        
    }

}
