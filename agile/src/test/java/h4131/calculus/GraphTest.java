package h4131.calculus;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import h4131.model.TimeWindow;

public class GraphTest {

    
    Graph mockedGraph;


    @Test
    void testEquals() {
        
        mockedGraph = new Graph();
        Graph result = new Graph();


        Intersection mockedinterWarehouse = new Intersection( 1, 4,5);
        Intersection mockedinterFirst = new Intersection(2, 8,7);
        Intersection mockedinterCheckpoint = new Intersection(25, 10,7);
        Intersection mockedinterSecond = new Intersection(3, 5,9);

        



        DeliveryPoint mockWarehouse = new DeliveryPoint(mockedinterWarehouse, TimeWindow.WAREHOUSE);
        DeliveryPoint mockFirstDeliveryPoint = new DeliveryPoint(mockedinterFirst, TimeWindow.EIGHT_NINE);
        DeliveryPoint mockSecondDeliveryPoint = new DeliveryPoint(mockedinterSecond, TimeWindow.TEN_ELEVEN); //non contiguous time window


        Segment mockedSegment1 = new Segment(mockedinterWarehouse, mockedinterFirst, 5, "alban");
        Segment mockedSegment2 = new Segment(mockedinterFirst, mockedinterSecond, 8, "clement");
        Segment mockedSegment3 = new Segment(mockedinterFirst, mockedinterCheckpoint, 2, "florian");
        Segment mockedSegment4 = new Segment(mockedinterCheckpoint, mockedinterSecond, 3, "antoine");//shorter path by taking the "checkpoint"
        Segment mockedSegment5 = new Segment(mockedinterSecond, mockedinterWarehouse, 5, "tao");

   

        
        

        mockedGraph.nodes.add(mockWarehouse);
        mockedGraph.nodes.add(mockFirstDeliveryPoint);
        mockedGraph.nodes.add(mockSecondDeliveryPoint);
       
       
        result.nodes.add(mockFirstDeliveryPoint);
        result.nodes.add(mockWarehouse);
        result.nodes.add(mockSecondDeliveryPoint);

        Arc mockArc1 = new Arc(mockWarehouse, mockFirstDeliveryPoint, 5);
        mockArc1.path.add(mockedSegment1);

        Arc mockArc2 = new Arc(mockFirstDeliveryPoint, mockSecondDeliveryPoint, 5); 
        mockArc2.path.add(mockedSegment3);
        mockArc2.path.add(mockedSegment4);

        Arc mockArc3 = new Arc(mockSecondDeliveryPoint, mockWarehouse, 5);
        mockArc3.path.add(mockedSegment5);


        mockedGraph.arcs.add(mockArc2);
        mockedGraph.arcs.add(mockArc3);
        mockedGraph.arcs.add(mockArc1);

        result.arcs.add(mockArc2);
        result.arcs.add(mockArc1);
        result.arcs.add(mockArc3);
        


        
        assertTrue(mockedGraph.equals(result));
    }
}
