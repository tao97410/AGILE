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

public class SeqIterTest {

    @Mock
    SeqIter iteratorMock;
    @Mock
    Unvisited uMock;
    @Mock
    Graph g;

    @BeforeEach
    void setUp(){
        int nbSommets=5;
        g=new Graph();
        DeliveryPoint point1=new DeliveryPoint(new Intersection(10,45.74979, 4.87572), TimeWindow.WAREHOUSE);
        DeliveryPoint point2=new DeliveryPoint(new Intersection(11,45.76873, 4.8624663), TimeWindow.NINE_TEN);
        DeliveryPoint point3=new DeliveryPoint(new Intersection(12,45.86873, 4.9624663), TimeWindow.NINE_TEN);
        DeliveryPoint point4=new DeliveryPoint(new Intersection(13,46.86873, 4.9624663), TimeWindow.TEN_ELEVEN);
        DeliveryPoint point5=new DeliveryPoint(new Intersection(15,46.86873, 4.5624663), TimeWindow.EIGHT_NINE);

        g.nodes.add(point1);
        g.nodes.add(point2);
        g.nodes.add(point3);
        g.nodes.add(point4);
        g.nodes.add(point5);

        g.setNbNodes(nbSommets);


        TimeWindow[] listeWindow=new TimeWindow[nbSommets];
        listeWindow[0]=TimeWindow.WAREHOUSE;
        listeWindow[1]=TimeWindow.NINE_TEN;
        listeWindow[2]=TimeWindow.NINE_TEN;
        listeWindow[3]=TimeWindow.TEN_ELEVEN;
        listeWindow[4]=TimeWindow.EIGHT_NINE;


        g.setListeWindow(listeWindow);
        double[][]cost= new double[nbSommets][nbSommets];
        cost[0][1]=3202.5041610000007;
        cost[1][0]=3202.5041610000007;
        cost[2][1]=3232.5041610000007;


        double[][] timeWindow=new double[nbSommets][2];
        timeWindow[0][0]=7.0;
        timeWindow[0][1]=8.0;
        timeWindow[1][0]=9.0;
        timeWindow[1][1]=10.0;
        timeWindow[2][0]=9.0;
        timeWindow[2][1]=10.0;
        timeWindow[3][0]=10.0;
        timeWindow[3][1]=11.0;
        timeWindow[4][0]=8.0;
        timeWindow[4][1]=9.0;

        
        g.setTimeWindow(timeWindow);

        g.setCost(cost);

        g.setSizeNbTimeWindow(1);
        Collection<Integer> unvisited= new ArrayList<>();
        unvisited.add(1);
        unvisited.add(2);
        unvisited.add(3);
        uMock= new Unvisited(unvisited, g);
       
    }

    @Test
    void testHasNextTrueInTheSameTimeWindow() {
        iteratorMock=new SeqIter(uMock, g, 1);
        boolean expected=true;
        boolean actual=iteratorMock.hasNext();
        assertEquals(expected,actual);

    }

    @Test
    void testHasNextFalseInTheSameTimeWindow() {
        iteratorMock=new SeqIter(uMock, g, 1);
        boolean expected=false;
        iteratorMock.next();
        iteratorMock.next();
        boolean actual=iteratorMock.hasNext();
        assertEquals(expected,actual);

    }


    @Test
    void testHasNextTrueChangingWindow() {
        iteratorMock=new SeqIter(uMock, g, 0);
        boolean expected=true;
        boolean actual=iteratorMock.hasNext();
        assertEquals(expected,actual);
    }

    @Test
    void testHasNextFalseChangingWindow() {
        iteratorMock=new SeqIter(uMock, g, 3);
        boolean expected=false;
        iteratorMock.next();
        boolean actual=iteratorMock.hasNext();
        assertEquals(expected,actual);
    }



    @Test
    void testRemove() {
        iteratorMock=new SeqIter(uMock, g, 3);
        iteratorMock.next();
        Integer DeliveryPoint=3;

        Integer actual=DeliveryPoint;
        Integer expected=iteratorMock.remove();
        assertEquals(expected,DeliveryPoint);
   
    }

    @Test
    void testAddFollowing() {
        iteratorMock=new SeqIter(uMock, g, 3);
        Integer DeliveryPoint=4;
        iteratorMock.addFollowing(DeliveryPoint);
        Integer expected=DeliveryPoint;
		int window=g.getWindow(DeliveryPoint).ordinal()-1;
        Integer actual=iteratorMock.getCandidates()[window][iteratorMock.getNbCandidates()[window]];
        assertEquals(expected, actual);

    }
   
}
