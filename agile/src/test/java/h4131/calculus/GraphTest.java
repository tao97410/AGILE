package h4131.calculus;

import static org.junit.Assert.assertTrue;


import org.junit.jupiter.api.Test;

import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.Segment;
import h4131.model.TimeWindow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class GraphTest {

    @Mock
    Graph mockedGraph;

    @BeforeEach    
    void beforeMethod(){
        mockedGraph = new Graph();
        Collection<DeliveryPoint> nodes= new ArrayList<>();
        DeliveryPoint point1 = new DeliveryPoint(new Intersection(0, 0, 0), TimeWindow.EIGHT_NINE);
        nodes.add(point1);
        System.out.println(point1.toString());
        DeliveryPoint point2 = new DeliveryPoint(new Intersection(0, 0, 0), TimeWindow.EIGHT_NINE);
        nodes.add(point2);
        DeliveryPoint point3 = new DeliveryPoint(new Intersection(0, 0, 0), TimeWindow.NINE_TEN);
        nodes.add(point3);
        mockedGraph.setNodes(nodes);

    }

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


   

    @Test
    void testIsArcOutOfBounds1() {
        boolean expected= false;
        boolean actual;
        actual=mockedGraph.isArc(-1,-1);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcOutOfBounds2() {
        boolean expected= true;
        boolean actual;
        actual=mockedGraph.isArc(2,0);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcOutOfBounds3() {
        boolean expected= false;
        boolean actual;
        actual=mockedGraph.isArc(3,4);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcBetweenDifferentNodes() {
        boolean expected= true;
        boolean actual;
        actual=mockedGraph.isArc(2,1);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcBetweenSameNodes() {
        boolean expected= false;
        boolean actual;
        actual=mockedGraph.isArc(-1,-1);
        assertEquals(expected, actual);
    }

    @Test
    void testTimeTravel() {
        mockedGraph = new Graph();
        double [][] cout= new double[5][5];
        double [][] expected= new double[5][5];
        for(int i=0 ; i<cout.length ; i++){
            for(int j=0 ; j<cout.length ; j++){
                 cout[i][j]=(double)16000.0*i;
                 expected[i][j]=cout[i][j]/15000.0;
            }
        }
        mockedGraph.setCost(cout);
        for(int i=0 ; i<cout.length ; i++){
            for(int j=0 ; j<cout.length ; j++){
                 assertEquals(expected[i][j], mockedGraph.timeTravel(i, j),0);
            }
        }

    }


    @Test
    void testFindDeliveryErreurNegative() {
        mockedGraph = new Graph();
        assertNull(mockedGraph.findDeliveryErreur(-1));
       

    }

    @Test
    void testFindDeliveryErreur() {
        mockedGraph = new Graph();
        Collection<DeliveryPoint> node=new ArrayList<>();
        DeliveryPoint point1=new DeliveryPoint(new Intersection(10,45.74979, 4.87572), TimeWindow.WAREHOUSE);
        DeliveryPoint point2=new DeliveryPoint(new Intersection(11,45.76873, 4.8624663), TimeWindow.EIGHT_NINE);
        DeliveryPoint point3=new DeliveryPoint(new Intersection(11,0, 2), TimeWindow.EIGHT_NINE);
        DeliveryPoint point4=new DeliveryPoint(new Intersection(11,1, 2), TimeWindow.EIGHT_NINE);
        
        node.add(point1);
        node.add(point2);
        node.add(point3);
        node.add(point4);
       
        mockedGraph.setNodes(node);
        
        DeliveryPoint expected=point3;
        DeliveryPoint actual=mockedGraph.findDeliveryErreur(2);


        assertEquals(expected, actual);

    }

}

