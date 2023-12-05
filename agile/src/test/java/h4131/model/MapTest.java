package h4131.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class MapTest {

    @Mock
    private Map mockedMap;


    @Test
    void TestAddIntersection() {
        mockedMap= new Map();
        Intersection intersection = new Intersection(10,20,30);
        mockedMap.addIntersection(intersection);
        assertNotNull(mockedMap.getIntersections().get((long)10));
    }

    @Test
    void TestAddSegment() { 
        mockedMap= new Map();
        Segment s= new Segment(new Intersection(10,20,30), new Intersection(20,30,40), 10, "Test");
        mockedMap.addSegment(s);
        assertNotNull(mockedMap.getAdjacency().get(s.getOrigin().getId()));
    }

    @Test
    void TestAddSegmentWithOriginExisting() { 
        mockedMap= new Map();
        Segment s= new Segment(new Intersection(10,20,30), new Intersection(20,30,40), 10, "Test");
        mockedMap.addSegment(s);
        Segment sameOrigin= new Segment(new Intersection(10,20,30), new Intersection(40,50,60), 20, "sameOrigin");
        assertNotNull(mockedMap.getAdjacency().get(sameOrigin.getOrigin().getId()));
    }

    
    @Test
    void TestHasIntersection() {
        mockedMap= new Map();
        mockedMap.addIntersection(new Intersection(10,20,30));
        assertEquals(mockedMap.hasIntersection(10),true);
    }
    


    @Test
    void TestGetGraphFromPointsWithoutPrivate() {
        mockedMap = new Map();
        DeliveryPoint mockWarehouse = new DeliveryPoint(new Intersection(1, 4,5), TimeWindow.WAREHOUSE);
        DeliveryPoint mockFirsDeliveryPoint = new DeliveryPoint(new Intersection(2, 8,7), TimeWindow.WAREHOUSE);
        DeliveryPoint mockSecondDeliveryPoint = new DeliveryPoint(new Intersection(3, 5,9), TimeWindow.WAREHOUSE);

        Collection<DeliveryPoint> mockedDeliveryPoints = new LinkedList<>();

    }

    @Test
    void TestGetGraphFromPointsWithGetPath() {

    }

    @Test
    void TestGetGraphFromPointsWithFindSegment() {

    }

    @Test
    void TestGetGraphFromPointsWithDijkstra() {

    }



   
}

