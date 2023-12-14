package h4131.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.powermock.modules.junit4.PowerMockRunner;

import h4131.calculus.Arc;
import h4131.calculus.Graph;
import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.GlobalTour;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.Segment;
import h4131.model.TimeWindow;
import h4131.model.Tour;
import h4131.view.WindowBuilder;

@RunWith(PowerMockRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InitialStateTest {

    @Mock
    WindowBuilder mockedWindowBuilder;

    @Mock
    Controller mockedController;

    @Mock
    InitialState mockedInitialState;

    @BeforeEach
    void init(){        
        mockedInitialState = new InitialState();

        mockedController = new Controller();

        mockedWindowBuilder = mock(WindowBuilder.class);
        
    }

    @Test
    void testLeftClick() {
        Map map = mock(Map.class);
        Controller spyController = spy(mockedController);        
        when(spyController.getMap()).thenReturn(map);
        when(spyController.getNumberOfCourier()).thenReturn(1);

        Intersection intersection = new Intersection(0, 0, 0);
        when(map.getIntersectionById(anyLong())).thenReturn(intersection);

        mockedInitialState.leftClick(spyController, mockedWindowBuilder, (long) 0);

        verify(spyController).setCurrentState(any(AddDeliveryPointState.class));
        verify(mockedWindowBuilder).openMenuIntersection(1, intersection);

    }

    @Test
    void testLoadGlobalTour() {

    }

    @Test
    void testLoadMap() {
        // Map map = mock(Map.class); 

        // String fileName = "FileName";

        // MockedStatic<XMLdeserializer> theMock = Mockito.mockStatic(XMLdeserializer.class);

        // mockedInitialState.loadMap(mockedController, mockedWindowBuilder, fileName);

        // theMock.verify(() -> XMLdeserializer.loadMap(fileName, map), times(1));
        // verify(mockedController).setMap(map);
        // verify(mockedWindowBuilder).drawMap(map);

    }

    @Test
    void testModifyDeliveryPoint() {
        int courier = 0;
        DeliveryPoint deliveryPoint = new DeliveryPoint(null, null);
        Controller spyController = spy(mockedController); 
        
        mockedInitialState.modifyDeliveryPoint(spyController, mockedWindowBuilder, deliveryPoint, courier);

        verify(mockedWindowBuilder).openMenuModifyDeliveryPoint(courier, deliveryPoint, courier);
        verify(spyController).setCurrentState(any(ModifyDeliveryPointState.class));

    }

    @Test
    void testSetNumberOfCourierWhenNumberOfCourierIsInferior() {

        int numberOfCourier = 0;

        Intersection intersection = new Intersection(0, 0, 0);

        DeliveryPoint deliveryPoint = new DeliveryPoint(intersection, TimeWindow.EIGHT_NINE);

        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class);
        LinkedList<LinkedList<DeliveryPoint>> affected = new LinkedList<LinkedList<DeliveryPoint>>();
        LinkedList<DeliveryPoint> nonAffected = new LinkedList<DeliveryPoint>();

        affected.add(new LinkedList<>());
        affected.get(0).add(deliveryPoint);

        nonAffected.add(deliveryPoint);

        Controller spyController = spy(mockedController); 

        when(spyController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);
        when(currentDeliveryPoint.getAffectedDeliveryPoints()).thenReturn(affected);
        when(currentDeliveryPoint.getNonAffectedDeliveryPoints()).thenReturn(nonAffected);

        mockedInitialState.setNumberOfCourier(spyController, mockedWindowBuilder, numberOfCourier);

        verify(currentDeliveryPoint).addAllNonAffectedDeliveryPoints(affected.get(0));
        verify(currentDeliveryPoint).removeLastCourier();

    }

    @Test
    void testSetNumberOfCourierWhenNumberOfCourierIsSuperior(){
        int numberOfCourier = 2;

        Intersection intersection = new Intersection(0, 0, 0);

        DeliveryPoint deliveryPoint = new DeliveryPoint(intersection, TimeWindow.EIGHT_NINE);

        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class);
        LinkedList<LinkedList<DeliveryPoint>> affected = new LinkedList<LinkedList<DeliveryPoint>>();
        LinkedList<DeliveryPoint> nonAffected = new LinkedList<DeliveryPoint>();

        affected.add(new LinkedList<>());
        affected.get(0).add(deliveryPoint);

        nonAffected.add(deliveryPoint);

        Controller spyController = spy(mockedController); 

        when(spyController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);
        when(currentDeliveryPoint.getAffectedDeliveryPoints()).thenReturn(affected);
        when(currentDeliveryPoint.getNonAffectedDeliveryPoints()).thenReturn(nonAffected);

        mockedInitialState.setNumberOfCourier(spyController, mockedWindowBuilder, numberOfCourier);

        verify(currentDeliveryPoint).addNewCourier();
    }

    @Test
    void testComputeGlobalTour(){
        Graph graph = new Graph();
        Graph mockedGraph = spy(graph);
        
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


        CurrentDeliveryPoint currentDeliveryPoint = new CurrentDeliveryPoint(1);
        CurrentDeliveryPoint spyCurrentDeliveryPoint = spy(currentDeliveryPoint);

        DeliveryPoint deliveryPoint = new DeliveryPoint(null, null);
        LinkedList<DeliveryPoint> list = new LinkedList<DeliveryPoint>();
        LinkedList<DeliveryPoint> list1 = new LinkedList<DeliveryPoint>();
        list.add(deliveryPoint);
        LinkedList<LinkedList<DeliveryPoint>> list2 = new LinkedList<LinkedList<DeliveryPoint>>();
        list2.add(list);
        list2.add(list1);
        spyCurrentDeliveryPoint.setAffectedDeliveryPoints(list2);

        

        Map mockedMap = mock(Map.class);

        GlobalTour globalTour = mock(GlobalTour.class);

        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(spyCurrentDeliveryPoint);
        when(spyController.getMap()).thenReturn(mockedMap);
        when(spyController.getGlobalTour()).thenReturn(globalTour);

        when(mockedMap.getGraphFromPoints(list)).thenReturn(mockedGraph);

        mockedInitialState.computeGlobalTour(spyController, mockedWindowBuilder);

        verify(globalTour, times(2)).addTour(any(Tour.class));
        //verify(mockedGraph).computeBestTour(any(GlobalTour.class));
        verify(mockedWindowBuilder).drawGlobalTour(globalTour);

    }

    @Test
    void testComputeGlobalTourWhenErrorinGraph(){
        Graph graph = new Graph();
        Graph mockedGraph = spy(graph);
        
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

        Arc mockArc1 = new Arc(mockWarehouse, mockFirstDeliveryPoint, 99999999);
        mockArc1.path.add(mockedSegment1);

        Arc mockArc2 = new Arc(mockFirstDeliveryPoint, mockSecondDeliveryPoint, 99999999); 
        mockArc2.path.add(mockedSegment3);
        mockArc2.path.add(mockedSegment4);

        Arc mockArc3 = new Arc(mockSecondDeliveryPoint, mockWarehouse, 99999999);
        mockArc3.path.add(mockedSegment5);


        mockedGraph.arcs.add(mockArc2);
        mockedGraph.arcs.add(mockArc3);
        mockedGraph.arcs.add(mockArc1);


        CurrentDeliveryPoint currentDeliveryPoint = new CurrentDeliveryPoint(1);
        CurrentDeliveryPoint spyCurrentDeliveryPoint = spy(currentDeliveryPoint);

        DeliveryPoint deliveryPoint = new DeliveryPoint(null, null);
        LinkedList<DeliveryPoint> list = new LinkedList<DeliveryPoint>();
        LinkedList<DeliveryPoint> list1 = new LinkedList<DeliveryPoint>();
        list.add(deliveryPoint);
        LinkedList<LinkedList<DeliveryPoint>> list2 = new LinkedList<LinkedList<DeliveryPoint>>();
        list2.add(list);
        list2.add(list1);
        spyCurrentDeliveryPoint.setAffectedDeliveryPoints(list2);        

        Map mockedMap = mock(Map.class);

        GlobalTour globalTour = mock(GlobalTour.class);

        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(spyCurrentDeliveryPoint);
        when(spyController.getMap()).thenReturn(mockedMap);
        when(spyController.getGlobalTour()).thenReturn(globalTour);

        when(mockedMap.getGraphFromPoints(list)).thenReturn(mockedGraph);

        mockedInitialState.computeGlobalTour(spyController, mockedWindowBuilder);

        verify(mockedWindowBuilder).alert("error on this time window : EIGHT_NINEon tour n°1");
        verify(mockedWindowBuilder).drawGlobalTour(globalTour);
    }

    @Test
    void testComputeGlobalTourWhenNoPathFound(){
        CurrentDeliveryPoint currentDeliveryPoint = new CurrentDeliveryPoint(1);
        CurrentDeliveryPoint spyCurrentDeliveryPoint = spy(currentDeliveryPoint);

        DeliveryPoint deliveryPoint = new DeliveryPoint(null, null);
        LinkedList<DeliveryPoint> list = new LinkedList<DeliveryPoint>();
        LinkedList<DeliveryPoint> list1 = new LinkedList<DeliveryPoint>();
        list.add(deliveryPoint);
        LinkedList<LinkedList<DeliveryPoint>> list2 = new LinkedList<LinkedList<DeliveryPoint>>();
        list2.add(list);
        list2.add(list1);
        spyCurrentDeliveryPoint.setAffectedDeliveryPoints(list2);        

        Map mockedMap = mock(Map.class);

        GlobalTour globalTour = mock(GlobalTour.class);

        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(spyCurrentDeliveryPoint);
        when(spyController.getMap()).thenReturn(mockedMap);
        when(spyController.getGlobalTour()).thenReturn(globalTour);

        when(mockedMap.getGraphFromPoints(list)).thenThrow(new NullPointerException());

        mockedInitialState.computeGlobalTour(spyController, mockedWindowBuilder);

        verify(mockedWindowBuilder).alert("No path found. Check that every intersections are accessibles in both ways.");

    }
    
}
