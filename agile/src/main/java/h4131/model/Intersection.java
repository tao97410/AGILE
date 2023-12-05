package h4131.model;
public class Intersection {
    private long id;
    private double latitude;
    private double longitude;

    public Intersection(long anId, double aLatitude, double aLongitude){
        this.id = anId;
        this.latitude = aLatitude;
        this.longitude = aLongitude;    
    }


    /**
     * @return long return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return double return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return double return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o){
        
        if(!(o instanceof Intersection))
            return false;
        else{
            Intersection i  = (Intersection) o;
            return (id == i.getId() && latitude == i.getLatitude()&& longitude == i.getLongitude());
        }
        
    }

}