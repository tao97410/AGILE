package h4131.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import h4131.calculus.Arc;
import h4131.calculus.Graph;



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
    
    @Nested
    class MapGraphTest{

        @Mock
        private Collection<DeliveryPoint> mockedDeliveryPoints;

        
        private Graph result;

        @BeforeEach
        public void init(){
            mockedMap = new Map();
            mockedDeliveryPoints = new LinkedList<DeliveryPoint>();
            result = new Graph();


            Intersection mockedinterWarehouse = new Intersection( 1, 4,5);
            Intersection mockedinterFirst = new Intersection(2, 8,7);
            Intersection mockedinterCheckpoint = new Intersection(25, 10,7);
            Intersection mockedinterSecond = new Intersection(3, 5,9);

            mockedMap.addIntersection(mockedinterWarehouse);
            mockedMap.addIntersection(mockedinterFirst);
            mockedMap.addIntersection(mockedinterSecond);
            mockedMap.addIntersection(mockedinterCheckpoint);



            DeliveryPoint mockWarehouse = new DeliveryPoint(mockedinterWarehouse, TimeWindow.WAREHOUSE);
            DeliveryPoint mockFirstDeliveryPoint = new DeliveryPoint(mockedinterFirst, TimeWindow.EIGHT_NINE);
            DeliveryPoint mockSecondDeliveryPoint = new DeliveryPoint(mockedinterSecond, TimeWindow.TEN_ELEVEN); //non contiguous time window

            mockedMap.setWarehouse(mockWarehouse);

            Segment mockedSegment1 = new Segment(mockedinterWarehouse, mockedinterFirst, 5, "alban");
            Segment mockedSegment2 = new Segment(mockedinterFirst, mockedinterSecond, 8, "clement");
            Segment mockedSegment3 = new Segment(mockedinterFirst, mockedinterCheckpoint, 2, "florian");
            Segment mockedSegment4 = new Segment(mockedinterCheckpoint, mockedinterSecond, 3, "antoine");//shorter path by taking the "checkpoint"
            Segment mockedSegment5 = new Segment(mockedinterSecond, mockedinterWarehouse, 5, "tao");

            mockedMap.addSegment(mockedSegment1);
            mockedMap.addSegment(mockedSegment2);
            mockedMap.addSegment(mockedSegment3);
            mockedMap.addSegment(mockedSegment4);
            mockedMap.addSegment(mockedSegment5);

            
            
            mockedDeliveryPoints.add(mockFirstDeliveryPoint);
            mockedDeliveryPoints.add(mockSecondDeliveryPoint);

            result.nodes.add(mockWarehouse);
            result.nodes.add(mockFirstDeliveryPoint);
            result.nodes.add(mockSecondDeliveryPoint);

            Arc mockArc1 = new Arc(mockWarehouse, mockFirstDeliveryPoint, 5);
            mockArc1.path.add(mockedSegment1);

            Arc mockArc2 = new Arc(mockFirstDeliveryPoint, mockSecondDeliveryPoint, 5); 
            mockArc2.path.add(mockedSegment3);
            mockArc2.path.add(mockedSegment4);

            Arc mockArc3 = new Arc(mockSecondDeliveryPoint, mockWarehouse, 5);
            mockArc3.path.add(mockedSegment5);


            result.arcs.add(mockArc2);
            result.arcs.add(mockArc3);
            result.arcs.add(mockArc1);
            
            
            
        }

        @Test
        void TestGetGraphFromPointsGlobal() {
            
            assertEquals(result,mockedMap.getGraphFromPoints(mockedDeliveryPoints));

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


    



   
}

