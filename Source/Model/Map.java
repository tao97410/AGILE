import java.util.HashMap;
import java.util.List;

public class Map {
    private HashMap<Long,Intersection> intersections;
    private HashMap<Long,List<Segment>> adjacency;
    private Intersection warehouse;

    public Map(Intersection aWarehouse){
        this.warehouse = aWarehouse;
        this.intersections = new HashMap<>();
        this.adjacency = new HashMap<>();
    }

    public Intersection getIntersectionById(long id){
        return this.intersections.get(id);
    }

    public List<Segment> getDestinationsById(long id){
        return this.adjacency.get(id);
    }

    /**
     * @return HashMap<long,Intersection> return the intersections
     */
    public HashMap<Long,Intersection> getIntersections() {
        return intersections;
    }

    /**
     * @param intersections the intersections to set
     */
    public void setIntersections(HashMap<Long,Intersection> intersections) {
        this.intersections = intersections;
    }

    /**
     * @return HashMap<long,List<Segment>> return the adjacency
     */
    public HashMap<Long,List<Segment>> getAdjacency() {
        return adjacency;
    }

    /**
     * @param adjacency the adjacency to set
     */
    public void setAdjacency(HashMap<Long,List<Segment>> adjacency) {
        this.adjacency = adjacency;
    }

    /**
     * @return Intersection return the warehouse
     */
    public Intersection getWarehouse() {
        return warehouse;
    }

    /**
     * @param warehouse the warehouse to set
     */
    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

}
