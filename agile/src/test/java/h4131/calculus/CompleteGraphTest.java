package h4131.calculus;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.TimeWindow;


public class CompleteGraphTest {

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
        // Map loadedMap = new Map();
        // try {
        //     XMLdeserializer.loadMap(loadedMap);
        // } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        // Graph g = loadedMap.getGraphFromPoints(null);
        // GlobalTour globalTour=new GlobalTour();
        // g.computeBestTour(globalTour);

        // Tour actualTour=globalTour.getTours().get(0);
        

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
}
