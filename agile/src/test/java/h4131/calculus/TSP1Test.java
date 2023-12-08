package h4131.calculus;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.TimeWindow;

public class TSP1Test {

     @Mock
    TSP1 templateTSPMock;

    @BeforeEach
    void setUp(){
        templateTSPMock = new TSP1(); 
    }

    @Test
    void testBound() {

    }

    @Test
    void testIterator() {

    }

    @Test
    void testGetSolutionGraph() {
        double actual;
        double expected=-1;

        templateTSPMock.setG(new CompleteGraph());
        actual=templateTSPMock.getSolutionCost();
            
        assertEquals(expected, actual);
       
    }

    @Test
    void testGetSolution() {
        double actual;
        double expected=12.0;
        templateTSPMock.setG(new CompleteGraph());
        templateTSPMock.setBestSolCost(12.0);
        actual=templateTSPMock.getSolutionCost();
        assertEquals(expected, actual);
       
    }



    @Test
    void testSearchSolutionNegativeTimeLimit(){
        double actual;
        double expected=templateTSPMock.getSolutionCost();
        templateTSPMock.searchSolution(-1, null);
        actual=templateTSPMock.getSolutionCost();
        assertEquals(expected, actual);
    }
    
    
    @Test
    void testSearchSolutionGeneric(){
        CompleteGraph g=new CompleteGraph();

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
        g.setNbPlageHoraire(nbPlageHoraire);

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
        templateTSPMock.searchSolution(2000, g);

        System.out.println(templateTSPMock.getSolutionCost());
        double actual;
        double expected=3202.5041610000007*2;
        actual=templateTSPMock.getSolutionCost();
        assertEquals(expected, actual);
        assertNull(g.getDeliveryErreur());

    }

    @Test
    void testSearchSolutionWithError(){

        CompleteGraph g=new CompleteGraph();

        DeliveryPoint point1=new DeliveryPoint(new Intersection(10,45.74979, 4.87572), TimeWindow.WAREHOUSE);
        DeliveryPoint point2=new DeliveryPoint(new Intersection(11,45.76873, 4.8624663), TimeWindow.EIGHT_NINE);
        DeliveryPoint point3=new DeliveryPoint(new Intersection(12,45.749226, 4.843437), TimeWindow.EIGHT_NINE);
        DeliveryPoint point4=new DeliveryPoint(new Intersection(13,45.74748, 4.8490343), TimeWindow.EIGHT_NINE);
        DeliveryPoint point5=new DeliveryPoint(new Intersection(14,45.748867, 4.8440557), TimeWindow.EIGHT_NINE);

        g.nodes.add(point1);
        g.nodes.add(point2);
        g.nodes.add(point3);
        g.nodes.add(point4);
        g.nodes.add(point5);
        g.setNbNodes(5);

        g.arcs.add(new Arc(point1,point2,3202.5041610000007));
        g.arcs.add(new Arc(point1,point3,3155.4564463999995));
        g.arcs.add(new Arc(point1,point4,2900.8164603999994));
        g.arcs.add(new Arc(point1,point5,3217.6863763999995));
        g.arcs.add(new Arc(point2,point3,3515.1940860000004));
        g.arcs.add(new Arc(point2,point4,3315.0175360000007));
        g.arcs.add(new Arc(point2,point5,3577.4240160000004));
        g.arcs.add(new Arc(point3,point2,3353.8586940000014));
        g.arcs.add(new Arc(point3,point4,522.957998));
        g.arcs.add(new Arc(point3,point5,62.22993));
        g.arcs.add(new Arc(point4,point2,3339.0530633999997));
        g.arcs.add(new Arc(point4,point3,897.2631860000001));
        g.arcs.add(new Arc(point4,point5,959.4931160000001));
        g.arcs.add(new Arc(point5,point2,3291.628764000001));
        g.arcs.add(new Arc(point5,point3,369.75099));
        g.arcs.add(new Arc(point5,point4,460.72806799999995));
        g.arcs.add(new Arc(point5,point1,3202.5041610000007));

        g.setTimeBegining(TimeWindow.EIGHT_NINE);

        int[] nbPlageHoraire=new int[2];
        nbPlageHoraire[0]=4;
        g.setNbPlageHoraire(nbPlageHoraire);

        TimeWindow[] listeWindow=new TimeWindow[5];
        listeWindow[0]=TimeWindow.WAREHOUSE;
        listeWindow[1]=TimeWindow.EIGHT_NINE;
        listeWindow[2]=TimeWindow.EIGHT_NINE;
        listeWindow[3]=TimeWindow.EIGHT_NINE;
        listeWindow[4]=TimeWindow.EIGHT_NINE;
        g.setListeWindow(listeWindow);

       
        double[][]cost= new double[5][5];
        int nodePos=0;
        int nodePos2=0;
        int arcExiste=0;
        for(DeliveryPoint node: g.nodes){
            for(DeliveryPoint node2 :g.nodes){
                if(node==node2){
                    cost[nodePos][nodePos2]=-1;
                }else{
                    for(Arc arc : g.arcs){
                        if(arc.origin==node && arc.destination==node2){
                            cost[nodePos][nodePos2]=arc.distance;
                            arcExiste=1;
                        }
                    }
                    if(arcExiste==0)
                        cost[nodePos][nodePos2]=Double.MAX_VALUE;
                }
                arcExiste=0;
                nodePos2++;
            }
            nodePos2=0;
            nodePos++;
        }
        g.setCost(cost);

        double[][] timeWindow=new double[5][5];
        timeWindow[0][0]=7.0;
        timeWindow[0][1]=8.0;
        timeWindow[1][0]=8.0;
        timeWindow[1][1]=9.0;
        timeWindow[2][0]=8.0;
        timeWindow[2][1]=9.0;
        timeWindow[3][0]=8.0;
        timeWindow[3][1]=9.0;
        timeWindow[4][0]=8.0;
        timeWindow[4][1]=9.0;
        g.setTimeWindow(timeWindow);

        templateTSPMock.searchSolution(2000, g);

        double actual;
        double expected=4;
        actual=templateTSPMock.getIndexDeliveryErreur();
        assertEquals(expected, actual);

    }


}
