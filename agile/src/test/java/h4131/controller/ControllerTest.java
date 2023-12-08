package h4131.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;
import javafx.stage.Stage;

public class ControllerTest {

    @Mock
    State mockedState;

    @InjectMocks
    Controller mockedController = new Controller();

    @BeforeEach
    void init(){
        mockedState = mock(State.class);
        mockedController.setCurrentState(mockedState); 
    }


    @Test
    void testAddDeliveryPoint() {
        
        int courier = 0;
        TimeWindow tw = TimeWindow.EIGHT_NINE;

        mockedController.addDeliveryPoint(tw, courier);
        
        verify(mockedState).addDeliveryPoint(mockedController, null,tw, courier);

    }

    @Test
    void testCancelDeliveryPoint() {

        mockedController.cancelDeliveryPoint();

        verify(mockedState).cancelDeliveryPoint(mockedController, null);

    }

    @Test
    void testChangeInfosDeliveryPoint() {
        int courier = 0;
        TimeWindow tw = TimeWindow.EIGHT_NINE;

        mockedController.changeInfosDeliveryPoint(tw, courier);
        
        verify(mockedState).changeInfosDeliveryPoint(mockedController, null, tw, courier);

    }

    @Test
    void testChangeNumberOfCourier() {
        int aNumber = 0;

        mockedController.changeNumberOfCourier(aNumber);

        verify(mockedState).setNumberOfCourier(mockedController, null, aNumber);

    }

    @Test
    void testDeleteDeliveryPoint() {

        mockedController.deleteDeliveryPoint();

        verify(mockedState).deleteDeliveryPoint(mockedController, null);

    }

    @Test
    void testLeftClick() {

        Long id = (long) 0;

        mockedController.leftClick(id);

        verify(mockedState).leftClick(mockedController, null, id);

    }

    @Test
    void testLoadGlobalTour() {

        mockedController.loadGlobalTour();

        verify(mockedState).loadGlobalTour(mockedController, null);

    }

    @Test
    void testLoadMap() {

        String fileName = "FileName";

        mockedController.loadMap(fileName);

        verify(mockedState).loadMap(mockedController, null, fileName);

    }

    @Test
    void testModifyDeliveryPoint() {

        DeliveryPoint deliveryPoint = new DeliveryPoint(null, null);
        int courier = 0;

        mockedController.modifyDeliveryPoint(deliveryPoint, courier);

        verify(mockedState).modifyDeliveryPoint(mockedController, null, deliveryPoint, courier);
    }

}
