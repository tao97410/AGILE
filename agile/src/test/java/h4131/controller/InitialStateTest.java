package h4131.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.Map;
import h4131.model.TimeWindow;
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
}
