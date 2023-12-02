package h4131.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import h4131.calculus.Graph;
import h4131.calculus.InterCompare;
import h4131.calculus.InterInfo;

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

    public boolean hasIntersection (long id) {
        return intersections.get(id) != null;
    }

    public void addIntersection (Intersection inter) {
        intersections.put(inter.getId(),inter);
    } 

    public void addSegment(Segment seg) {
        if (adjacency.containsKey(seg.getOrigin().getId())){
            // If the origin exists, add the segment to the existing list
            List<Segment> segmentList = adjacency.get(seg.getOrigin().getId());
            segmentList.add(seg);
        } else {
            // If the origin doesn't exist, create a new list with the segment and put it in the hashmap
            List<Segment> newSegmentList = new ArrayList<>();
            newSegmentList.add(seg);
            adjacency.put(seg.getOrigin().getId(), newSegmentList);
        }
        
    }

    public Graph getGraphFromPoints(/*LinkedList<DeliveryPoint> deliveryPoints*/) {

        //TEST
        List<Intersection> allinter =  new ArrayList<Intersection>(intersections.values());
        List<Intersection> dest = allinter.subList(0, 3);
        HashMap<Intersection,InterInfo> result = Dijkstra(warehouse, dest);
        for(Entry<Intersection,InterInfo> entry : result.entrySet()){
            if(dest.contains(entry.getKey())){
                System.out.println(entry.getKey().getId() + " [distance : " + entry.getValue().distance + ", pred : " + entry.getValue().pred + "]\n");
            }
        }
        
        return null;

    }

    private HashMap<Intersection,InterInfo> Dijkstra(Intersection i0,List<Intersection> destinations){
        PriorityQueue<InterCompare> q = new PriorityQueue<>();
        HashMap<Intersection,InterInfo> m = new HashMap<>();
        int fin = destinations.size();
        m.put(i0, new InterInfo(0, null,true));
        q.add(new InterCompare(0, i0));
        while (!q.isEmpty()) {
            Intersection i = q.poll().intersection;
            if(!m.get(i).isGrey)continue;
            if(fin==0) return m;
            if(adjacency.get(i.getId()) != null){
                for(Segment seg : adjacency.get(i.getId())){
                    Intersection iDest = getIntersectionById(seg.getDestination().getId());
                    double newCost = m.get(i).distance + seg.getLength();
                    if(m.get(iDest)==null || newCost < m.get(iDest).distance){
                        m.put(iDest, new InterInfo(newCost, i, true));
                        q.add(new InterCompare(newCost, iDest));
                    }
                }
            }
            m.get(i).isGrey=false;
            if(destinations.contains(i)) fin--;
        }
        System.out.println("pas de solution\n");
        return null;
    } 

    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("warehouse:  ").append(warehouse.getId()).append("\n");
        int intersectionCount = 0;
        for (Entry<Long, Intersection> entry : intersections.entrySet()) {
            if (intersectionCount >= 10) {
                break;
            }

            Long key = entry.getKey();
            Intersection intersection = entry.getValue();
            double latitude = intersection.getLatitude();
            double longitude = intersection.getLongitude();

            result.append(key).append(": ").append("Latitude: ").append(latitude)
                    .append(", Longitude: ").append(longitude).append("\n");

            intersectionCount++;
        }

        // Iterate through the first ten adjacency entries
        int adjacencyCount = 0;
        for (Entry<Long, List<Segment>> entry : adjacency.entrySet()) {
            if (adjacencyCount >= 10) {
                break;
            }

            Long key = entry.getKey();
            List<Segment> segmentList = entry.getValue();

            result.append(key).append(": ");

            // Append the names of segments separated by a comma
            for (Segment segment : segmentList) {
                result.append(segment.getName()).append(", ");
            }

            // Remove the trailing comma and space
            if (!segmentList.isEmpty()) {
                result.setLength(result.length() - 2);
            }

            result.append("\n");

            adjacencyCount++;
        }

        return result.toString();
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
