package h4131.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.crypto.spec.DESKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;

@RunWith(PowerMockRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModifyDeliveryPointStateTest {    
    
    @Mock
    WindowBuilder mockedWindowBuilder;

    @Mock
    Controller mockedController;

    @Mock
    ModifyDeliveryPointState mockedModifyDeliveryPointState;

    @BeforeEach
    void init(){
        mockedModifyDeliveryPointState = new ModifyDeliveryPointState();

        mockedController = new Controller();

        mockedWindowBuilder = mock(WindowBuilder.class);
        
    }

    @Test
    void testChangeInfosDeliveryPointWhenCourierIsZero() {
        mockedModifyDeliveryPointState.setCourier(0); 
        DeliveryPoint deliveryPoint = mock(DeliveryPoint.class);       
        mockedModifyDeliveryPointState.setCurrentDeliveryPoint(deliveryPoint);
        int newCourier = 1;
        TimeWindow aTime = TimeWindow.EIGHT_NINE;

        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class); 
        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);

        mockedModifyDeliveryPointState.changeInfosDeliveryPoint(spyController, mockedWindowBuilder, aTime, newCourier);

        verify(currentDeliveryPoint).removeNonAssignedDeliveryPoint(any(DeliveryPoint.class));
        verify(deliveryPoint).setTime(aTime);
        verify(currentDeliveryPoint).addAffectedDeliveryPoint(newCourier, deliveryPoint);
        verify(mockedWindowBuilder).disableBackground(false);
        verify(spyController).setCurrentState(any(InitialState.class));
    }

    @Test
    void testChangeInfosDeliveryPointWhenCourierIsGreaterThanZero() {
        mockedModifyDeliveryPointState.setCourier(2); 
        DeliveryPoint deliveryPoint = mock(DeliveryPoint.class);       
        mockedModifyDeliveryPointState.setCurrentDeliveryPoint(deliveryPoint);
        int newCourier = 1;
        TimeWindow aTime = TimeWindow.EIGHT_NINE;

        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class); 
        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);

        mockedModifyDeliveryPointState.changeInfosDeliveryPoint(spyController, mockedWindowBuilder, aTime, newCourier);

        verify(currentDeliveryPoint).removeAssignedDeliveryPoint(deliveryPoint, 2);
        verify(deliveryPoint).setTime(aTime);
        verify(currentDeliveryPoint).addAffectedDeliveryPoint(newCourier, deliveryPoint);
        verify(mockedWindowBuilder).disableBackground(false);
        verify(spyController).setCurrentState(any(InitialState.class));
    }

    @Test
    void testDeleteDeliveryPointWhenCourierIsZero() {
        mockedModifyDeliveryPointState.setCourier(0);      
        mockedModifyDeliveryPointState.setCurrentDeliveryPoint(new DeliveryPoint(null,null));  
        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class);
        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);

        mockedModifyDeliveryPointState.deleteDeliveryPoint(spyController, mockedWindowBuilder);

        verify(currentDeliveryPoint).removeNonAssignedDeliveryPoint(any(DeliveryPoint.class));
        verify(mockedWindowBuilder).disableBackground(false);
        verify(spyController).setCurrentState(any(InitialState.class));
    }

    @Test
    void testDeleteDeliveryPointWhenCourierIsGreaterThanZero() {
        mockedModifyDeliveryPointState.setCourier(2);      
        mockedModifyDeliveryPointState.setCurrentDeliveryPoint(new DeliveryPoint(null,null));  
        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class);
        Controller spyController = spy(mockedController);
        when(spyController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);

        mockedModifyDeliveryPointState.deleteDeliveryPoint(spyController, mockedWindowBuilder);

        verify(currentDeliveryPoint).removeAssignedDeliveryPoint(any(DeliveryPoint.class), eq(2));
        verify(mockedWindowBuilder).disableBackground(false);
        verify(spyController).setCurrentState(any(InitialState.class));
    }

}
