package h4131.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import h4131.calculus.Arc;
import h4131.calculus.Graph;
import h4131.calculus.InterCompare;
import h4131.calculus.InterInfo;

public class Map {
    private HashMap<Long,Intersection> intersections;
    private HashMap<Long,Collection<Segment>> adjacency;
    private DeliveryPoint warehouse;

    public Map(){
        this.intersections = new HashMap<>();
        this.adjacency = new HashMap<>();
    }

    public Intersection getIntersectionById(long id){
        return this.intersections.get(id);
    }

    /**
     * Checks if an intersection exists
     * @param id the id of the Intersection
     * @return boolean whether or not the intersection exists
     */

    public boolean hasIntersection (long id) {
        return intersections.get(id) != null;
    }

    /**
     * Adds an intersection to the map
     * @param inter Intersection to add
     */
    public void addIntersection (Intersection inter) {
        intersections.put(inter.getId(),inter);
    } 


    /**
     * Adds a segment to the map
     * @param seg Segment to add
     */
    public void addSegment(Segment seg) {
        if (adjacency.containsKey(seg.getOrigin().getId())){
            // If the origin exists, add the segment to the existing list
            Collection<Segment> segmentList = adjacency.get(seg.getOrigin().getId());
            segmentList.add(seg);
        } else {
            // If the origin doesn't exist, create a new list with the segment and put it in the hashmap
            Collection<Segment> newSegmentList = new ArrayList<>();
            newSegmentList.add(seg);
            adjacency.put(seg.getOrigin().getId(), newSegmentList);
        }
        
    }

    /**
     * Finds a segment of the map
     * @param idOrigin Id of the origin intersection
     * @param idDestination Id of the destination intersection
     * @return The wanted segment
     */
    private Segment findSegment (long idOrigin,long idDestination){
        Segment res = null;
        Collection<Segment>  segs = new ArrayList<>();
        if( (segs = adjacency.get(idOrigin))!=null){
            for(Segment s : segs){
                if(s.getDestination().getId() == idDestination){
                    res = s;
                    continue;
                }
            }
        }
        return res;
    }

    /**
     * Gets the graph of the shortest paths between delivery points.
     * We consider only paths between delivery points which can be delivered consecutively
     * @param deliveryPointsCol Delivery points considered
     * @return The wanted graph
     */
    public Graph getGraphFromPoints(Collection<DeliveryPoint> deliveryPointsCol) throws NullPointerException {

        Graph graph = new Graph();
        LinkedList<DeliveryPoint> deliveryPoints = new LinkedList<DeliveryPoint>(deliveryPointsCol);
    
        deliveryPoints.addFirst(warehouse);

        for (DeliveryPoint currentPoint : deliveryPoints)
        {
            graph.nodes.add(currentPoint);
            Collection<Intersection> possibleDestinations = new LinkedList<>();
            getPossibleDestinations(currentPoint, deliveryPoints, possibleDestinations);
            
            HashMap<Intersection,InterInfo> currentMap = dijkstra(currentPoint.getPlace(), possibleDestinations);
            if(currentMap != null){
                for (Intersection currentDestination : possibleDestinations){
                
                Collection<Segment> currentPath = new LinkedList<Segment>();
                getPath(currentPoint.getPlace(), currentDestination, currentMap, currentPath);

                Arc newArc = new Arc(currentPoint, getDeliveryPointFromIntersection(deliveryPoints, currentDestination), currentMap.get(currentDestination).distance);
                newArc.path = currentPath;
                graph.arcs.add(newArc);
                
                }
            }else{
                throw new NullPointerException();
            }
            
        }
        
        System.out.println(graph.toString());

        return graph;

    }

    /**
     * Gets all the possible destinations which can be served directly after a current point
     * @param currentPoint Current delivery point
     * @param deliveryPoints List of other delivery points
     * @param possibleDestinations List of possible destinations, to be filled in this method
     */
    private void getPossibleDestinations(DeliveryPoint currentPoint, Collection<DeliveryPoint> deliveryPoints,Collection<Intersection> possibleDestinations){
            TimeWindow minWindow = TimeWindow.WAREHOUSE;
            for (DeliveryPoint otherPoint : deliveryPoints) {
                if (minWindow.compareTo(TimeWindow.WAREHOUSE) == 0) {
                    if (otherPoint.getTime().compareTo(currentPoint.getTime()) > 0) {
                        minWindow = otherPoint.getTime();
                    }
                } else if (otherPoint.getTime().compareTo(minWindow) < 0 && otherPoint.getTime().compareTo(currentPoint.getTime()) > 0) {
                    minWindow = otherPoint.getTime();                
                }
            }

            for (DeliveryPoint otherPoint : deliveryPoints) {
                if (otherPoint != currentPoint && (otherPoint.getTime().compareTo(currentPoint.getTime()) == 0 || otherPoint.getTime().compareTo(minWindow) == 0)) {
                    possibleDestinations.add(otherPoint.getPlace());
                }
            }

    }

    /**
     * Gets the complete path from one point to another
     * @param origin Origin intersection
     * @param destination Destination intersetion
     * @param map Array resulting from the Dijkstra algorithm
     * @param path Complete path, to be filled in this method
     */
    private void getPath(Intersection origin, Intersection destination, HashMap<Intersection,InterInfo> map, Collection<Segment> path){
        
        if (origin.getId() != destination.getId()){
            getPath(origin, map.get(destination).pred, map, path);
            path.add(findSegment(map.get(destination).pred.getId(), destination.getId()));
        } 
    }

    /**
     * Gets the delivery point corresponding to a particular intersection
     * @param deliveries List of delivery points to search into
     * @param intersection Intersection to consider
     * @return Corresponding delivery point
     */
    private DeliveryPoint getDeliveryPointFromIntersection(Collection<DeliveryPoint> deliveries, Intersection intersection) {
        for (DeliveryPoint d : deliveries) {
            if (d.getPlace().getId() == intersection.getId()) {
                return d;
            }
        }
        return null;
    }

    /**
     * Processing Dijkstra algorithm
     * @param origin Origin for the algorithm
     * @param destinations Destinations to consider
     * @return Resulting array including all informations
     */
    private HashMap<Intersection,InterInfo> dijkstra(Intersection origin, Collection<Intersection> destinations){
        
        PriorityQueue<InterCompare> queue = new PriorityQueue<>();
        HashMap<Intersection,InterInfo> map = new HashMap<>();
        int destinationsLeft = destinations.size();
        map.put(origin, new InterInfo(0, null, true));
        queue.add(new InterCompare(0, origin));
        
        while (!queue.isEmpty()) {
            Intersection currentInter = queue.poll().intersection;
            if (!map.get(currentInter).isGrey)
                continue;
            if (destinationsLeft == 0) 
                return map;
            if (adjacency.get(currentInter.getId()) != null) {
                for (Segment seg : adjacency.get(currentInter.getId())){
                    Intersection currentDest = getIntersectionById(seg.getDestination().getId());
                    double newCost = map.get(currentInter).distance + seg.getLength();
                    if (map.get(currentDest) == null || newCost < map.get(currentDest).distance){
                        map.put(currentDest, new InterInfo(newCost, currentInter, true));
                        queue.add(new InterCompare(newCost, currentDest));
                    }
                }
            }
            map.get(currentInter).isGrey = false;
            if (destinations.contains(currentInter))
                destinationsLeft--;
        }
        
        System.out.println("pas de solution\n");
        return null;
    } 

    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("warehouse:  ").append(warehouse.getPlace().getId()).append("\n");
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
        for (Entry<Long, Collection<Segment>> entry : adjacency.entrySet()) {
            if (adjacencyCount >= 10) {
                break;
            }

            Long key = entry.getKey();
            Collection<Segment> segmentList = entry.getValue();

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
         
    /**
     * @param id the id of the intersection
     * @return Collection<Segment> return the intersection corresponding to the id
     */

    public Collection<Segment> getDestinationsById(long id){
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
    public HashMap<Long,Collection<Segment>> getAdjacency() {
        return adjacency;
    }

    /**
     * @param adjacency the adjacency to set
     */
    public void setAdjacency(HashMap<Long,Collection<Segment>> adjacency) {
        this.adjacency = adjacency;
    }

    /**
     * @return Intersection return the warehouse
     */
    public DeliveryPoint getWarehouse() {
        return warehouse;
    }

    /**
     * @param warehouse the warehouse to set
     */
    public void setWarehouse(DeliveryPoint warehouse) {
        this.warehouse = warehouse;
    }

}
