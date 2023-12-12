package h4131.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import h4131.calculus.Arc;
import h4131.calculus.Graph;
import h4131.calculus.InterCompare;
import h4131.calculus.InterInfo;

public class Map {
    private HashMap<Long,Intersection> intersections;
    private HashMap<Long,List<Segment>> adjacency;
    private DeliveryPoint warehouse;

    public Map(DeliveryPoint aWarehouse){
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

    public Graph getGraphFromPoints(LinkedList<DeliveryPoint> d) {


        // TEST
        ArrayList<Intersection> allIntersections = new ArrayList<>(intersections.values());
        LinkedList<DeliveryPoint> deliveryPoints = new LinkedList<>();
        System.out.println("Warehouse - " + warehouse.toString());
        DeliveryPoint point3 = new DeliveryPoint(allIntersections.get(23), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point3);
        System.out.println(point3.toString());
        DeliveryPoint point1 = new DeliveryPoint(allIntersections.get(1), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point1);
        System.out.println(point1.toString());
        DeliveryPoint point2 = new DeliveryPoint(allIntersections.get(51), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point2);
        System.out.println(point2.toString());
        DeliveryPoint point15 = new DeliveryPoint(allIntersections.get(52), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point15);
        System.out.println(point15.toString());
        DeliveryPoint point16 = new DeliveryPoint(allIntersections.get(53), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point16);
        System.out.println(point16.toString());
        DeliveryPoint point17 = new DeliveryPoint(allIntersections.get(54), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point17);
        System.out.println(point17.toString());
        DeliveryPoint point18 = new DeliveryPoint(allIntersections.get(58), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point18);
        System.out.println(point18.toString());
        DeliveryPoint point19 = new DeliveryPoint(allIntersections.get(56), TimeWindow.EIGHT_NINE);
        deliveryPoints.add(point19);
        System.out.println(point19.toString());

       
        DeliveryPoint point74 = new DeliveryPoint(allIntersections.get(91), TimeWindow.NINE_TEN);
        deliveryPoints.add(point74);
        System.out.println(point74.toString());
        DeliveryPoint point75 = new DeliveryPoint(allIntersections.get(92), TimeWindow.NINE_TEN);
        deliveryPoints.add(point75);
        System.out.println(point75.toString());
        DeliveryPoint point76= new DeliveryPoint(allIntersections.get(93), TimeWindow.NINE_TEN);
        deliveryPoints.add(point76);
        System.out.println(point76.toString());
        DeliveryPoint point77 = new DeliveryPoint(allIntersections.get(94), TimeWindow.NINE_TEN);
        deliveryPoints.add(point77);
        System.out.println(point77.toString());
        DeliveryPoint point78 = new DeliveryPoint(allIntersections.get(95), TimeWindow.NINE_TEN);
        deliveryPoints.add(point78);
        System.out.println(point78.toString());
        DeliveryPoint point79 = new DeliveryPoint(allIntersections.get(96), TimeWindow.NINE_TEN);
        deliveryPoints.add(point79);
        System.out.println(point79.toString());

        DeliveryPoint point7 = new DeliveryPoint(allIntersections.get(44), TimeWindow.ELEVEN_TWELVE);
        deliveryPoints.add(point7);
        System.out.println(point7.toString());
        DeliveryPoint point5 = new DeliveryPoint(allIntersections.get(77), TimeWindow.TEN_ELEVEN);
        deliveryPoints.add(point5);
        System.out.println(point5.toString());
        DeliveryPoint point6 = new DeliveryPoint(allIntersections.get(88), TimeWindow.TEN_ELEVEN);
        deliveryPoints.add(point6);
        System.out.println(point6.toString());
         DeliveryPoint point8 = new DeliveryPoint(allIntersections.get(66), TimeWindow.ELEVEN_TWELVE);
        deliveryPoints.add(point8);
        System.out.println(point8.toString());
        

        Graph graph = new Graph();
        deliveryPoints.addFirst(warehouse);

        for (DeliveryPoint currentPoint : deliveryPoints)
        {
            graph.nodes.add(currentPoint);
            List<Intersection> possibleDestinations = new LinkedList<>();

            // Searching the minimum TimeWindow, STRICTLY GREATER THAN the current TimeWindow
            // If there isn't any point with a strictly greater TimeWindow, its value will be WAREHOUSE
            // It's logic, because it means that we will come back to the warehouse after having delivered all points of this window
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

            // The possible destinations are the ones with the same TimeWindow than the current point
            // or with the TimeWindow equal to the minWindow calculated before
            // Indeed, we can't "jump over" a TimeWindow
            // For example, a 11-12 delivery can't be delivered after a 9-10 delivery if a 10-11 delivery exists
            for (DeliveryPoint otherPoint : deliveryPoints) {
                if (otherPoint != currentPoint && (otherPoint.getTime().compareTo(currentPoint.getTime()) == 0 || otherPoint.getTime().compareTo(minWindow) == 0)) {
                    possibleDestinations.add(otherPoint.getPlace());
                }
            }
            HashMap<Intersection,InterInfo> currentMap = dijkstra(currentPoint.getPlace(), possibleDestinations);
            for (Intersection currentDestination : possibleDestinations){
                
                LinkedList<Segment> currentPath = new LinkedList<Segment>();
                getPath(currentPoint.getPlace(), currentDestination, currentMap, currentPath);

                Arc newArc = new Arc(currentPoint, getDeliveryPointFromIntersection(deliveryPoints, currentDestination), currentMap.get(currentDestination).distance);
                newArc.path = currentPath;
                graph.arcs.add(newArc);
                
            }
        }
        
        System.out.println(graph.toString());

        return graph;

    }

    private void getPath(Intersection origin, Intersection destination, HashMap<Intersection,InterInfo> map, LinkedList<Segment> path){
        
        if (origin.getId() != destination.getId()){
            getPath(origin, map.get(destination).pred, map, path);
            path.add(findSegment(map.get(destination).pred.getId(), destination.getId()));
        } 
    }

    private DeliveryPoint getDeliveryPointFromIntersection(List<DeliveryPoint> deliveries, Intersection intersection) {
        for (DeliveryPoint d : deliveries) {
            if (d.getPlace().getId() == intersection.getId()) {
                return d;
            }
        }
        return null;
    }

    private HashMap<Intersection,InterInfo> dijkstra(Intersection origin, List<Intersection> destinations){
        
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
