package h4131.calculus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.xml.sax.SAXException;

import h4131.model.DeliveryPoint;
import h4131.model.GlobalTour;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.TimeWindow;
import h4131.model.Tour;
import h4131.xml.ExceptionXML;
import h4131.xml.XMLdeserializer;


public class GraphTest {

    @Mock
    Graph graph;

    @BeforeEach    
    void beforeMethod(){
        graph = new Graph();
        Collection<DeliveryPoint> nodes= new ArrayList<>();
        DeliveryPoint point1 = new DeliveryPoint(new Intersection(0, 0, 0), TimeWindow.EIGHT_NINE);
        nodes.add(point1);
        System.out.println(point1.toString());
        DeliveryPoint point2 = new DeliveryPoint(new Intersection(0, 0, 0), TimeWindow.EIGHT_NINE);
        nodes.add(point2);
        DeliveryPoint point3 = new DeliveryPoint(new Intersection(0, 0, 0), TimeWindow.NINE_TEN);
        nodes.add(point3);
        graph.setNodes(nodes);

    }

    @Test
    void testComputeBestTour() {
        Graph g=new Graph();

        DeliveryPoint point1=new DeliveryPoint(new Intersection(10,45.74979, 4.87572), TimeWindow.WAREHOUSE);
        DeliveryPoint point2=new DeliveryPoint(new Intersection(11,45.76873, 4.8624663), TimeWindow.EIGHT_NINE);
        g.nodes.add(point1);
        g.nodes.add(point2);
        g.setNbNodes(2);

        g.arcs.add(new Arc(point1,point2,3202.5041610000007));
        g.arcs.add(new Arc(point2,point1,3202.5041610000007));

        g.setTimeBegining(TimeWindow.EIGHT_NINE);

        int[] nbPlageHoraire=new int[2];
        nbPlageHoraire[0]=1;
        g.setnbDeliveryPointByTimeSlot(nbPlageHoraire);

        TimeWindow[] listeWindow=new TimeWindow[2];
        listeWindow[0]=TimeWindow.WAREHOUSE;
        listeWindow[1]=TimeWindow.EIGHT_NINE;
        g.setListeWindow(listeWindow);

       
        double[][]cost= new double[2][2];
        cost[0][1]=3202.5041610000007;
        cost[1][0]=3202.5041610000007;

        double[][] timeWindow=new double[2][2];
        timeWindow[0][0]=7.0;
        timeWindow[0][1]=8.0;
        timeWindow[1][0]=8.0;
        timeWindow[1][1]=9.0;
        g.setTimeWindow(timeWindow);

        g.setCost(cost);

        GlobalTour globalTour= new GlobalTour();
        GlobalTour globalTourMock=spy(globalTour);
        globalTourMock.setTours(new ArrayList<>());

        g.computeBestTour(globalTourMock);

        verify(globalTourMock).addTour(any(Tour.class));

        assertNull(g.getDeliveryErreur());
        List<Tour> list=globalTourMock.getTours();
        assertEquals(globalTourMock.getTours().get(0).getDeliveryPoints().get(0),point1);
        assertEquals(globalTourMock.getTours().get(0).getDeliveryPoints().get(1),point2);
        

    }

    @Test
    void testIsArcOutOfBounds1() {
        boolean expected= false;
        boolean actual;
        actual=graph.isArc(-1,-1);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcOutOfBounds2() {
        boolean expected= true;
        boolean actual;
        actual=graph.isArc(2,0);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcOutOfBounds3() {
        boolean expected= false;
        boolean actual;
        actual=graph.isArc(3,4);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcBetweenDifferentNodes() {
        boolean expected= true;
        boolean actual;
        actual=graph.isArc(2,1);
        assertEquals(expected, actual);
    }

    @Test
    void testIsArcBetweenSameNodes() {
        boolean expected= false;
        boolean actual;
        actual=graph.isArc(-1,-1);
        assertEquals(expected, actual);
    }

    @Test
    void testTimeTravel() {
        graph = new Graph();
        double [][] cout= new double[5][5];
        double [][] expected= new double[5][5];
        for(int i=0 ; i<cout.length ; i++){
            for(int j=0 ; j<cout.length ; j++){
                 cout[i][j]=(double)16000.0*i;
                 expected[i][j]=cout[i][j]/15000.0;
            }
        }
        graph.setCost(cout);
        for(int i=0 ; i<cout.length ; i++){
            for(int j=0 ; j<cout.length ; j++){
                 assertEquals(expected[i][j], graph.timeTravel(i, j),0);
            }
        }

    }


    @Test
    void testFindDeliveryErreurNegative() {
        graph = new Graph();
        assertNull(graph.findDeliveryErreur(-1));
       

    }

    @Test
    void testFindDeliveryErreur() {
        graph = new Graph();
        Collection<DeliveryPoint> node=new ArrayList<>();
        DeliveryPoint point1=new DeliveryPoint(new Intersection(10,45.74979, 4.87572), TimeWindow.WAREHOUSE);
        DeliveryPoint point2=new DeliveryPoint(new Intersection(11,45.76873, 4.8624663), TimeWindow.EIGHT_NINE);
        DeliveryPoint point3=new DeliveryPoint(new Intersection(11,0, 2), TimeWindow.EIGHT_NINE);
        DeliveryPoint point4=new DeliveryPoint(new Intersection(11,1, 2), TimeWindow.EIGHT_NINE);
        
        node.add(point1);
        node.add(point2);
        node.add(point3);
        node.add(point4);
       
        graph.setNodes(node);
        
        DeliveryPoint expected=point3;
        DeliveryPoint actual=graph.findDeliveryErreur(2);


        assertEquals(expected, actual);

    }

}

