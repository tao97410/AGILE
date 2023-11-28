package h4131.model;
public class Segment {
    private Intersection origin;
    private Intersection destination;
    private double length;
    private String name;

    public Segment(Intersection anOrigin, Intersection aDestination, double aLength, String aName){
        this.destination = aDestination;
        this.origin = anOrigin;
        this.length = aLength;
        this.name = aName;
    }

    /**
     * @return Intersection return the origin
     */
    public Intersection getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    /**
     * @return Intersection return the destination
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    /**
     * @return double return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
