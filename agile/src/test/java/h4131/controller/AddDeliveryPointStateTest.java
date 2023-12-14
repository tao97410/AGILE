package h4131.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import h4131.model.CurrentDeliveryPoint;
import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.TimeWindow;
import h4131.view.WindowBuilder;

@ExtendWith(MockitoExtension.class)
public class AddDeliveryPointStateTest {

    @Mock
    AddDeliveryPointState mockedAddDeliveryPointState;

    @Test
    void testAddDeliveryPoint() {
        Intersection inter = new Intersection(0, 0, 0);
        mockedAddDeliveryPointState = new AddDeliveryPointState();
        mockedAddDeliveryPointState.setCurrentIntersection(inter);

        CurrentDeliveryPoint currentDeliveryPoint = mock(CurrentDeliveryPoint.class);

        Controller mockedController = mock(Controller.class);
        mockedController.setCurrentState(new InitialState());
        when(mockedController.getCurrentDeliveryPoint()).thenReturn(currentDeliveryPoint);

        WindowBuilder windowBuilder = mock(WindowBuilder.class);
        int courier = 0;
        TimeWindow tw = TimeWindow.EIGHT_NINE;

        mockedAddDeliveryPointState.addDeliveryPoint(mockedController, windowBuilder, tw, courier);

        verify(windowBuilder).disableBackground(false);
        verify(mockedController).setCurrentState(any(InitialState.class));
        verify(currentDeliveryPoint).addAffectedDeliveryPoint(0,new DeliveryPoint(inter, tw));

    }

    @Test
    void testCancelDeliveryPoint() {
        Intersection inter = new Intersection(0, 0, 0);
        mockedAddDeliveryPointState = new AddDeliveryPointState();
        mockedAddDeliveryPointState.setCurrentIntersection(inter);

        Controller mockedController = mock(Controller.class);
        mockedController.setCurrentState(new InitialState());

        WindowBuilder windowBuilder = mock(WindowBuilder.class);

        mockedAddDeliveryPointState.cancelDeliveryPoint(mockedController, windowBuilder);

        verify(windowBuilder).disableBackground(false);
        verify(windowBuilder).unSetIntersection((long) 0);
        verify(mockedController).setCurrentState(any(InitialState.class));

    }
}
