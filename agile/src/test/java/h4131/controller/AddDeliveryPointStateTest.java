package h4131.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import h4131.model.Map;
import h4131.view.IntersectionCircle;
import h4131.view.WindowBuilder;

@ExtendWith(MockitoExtension.class)
public class AddDeliveryPointStateTest {

    @Mock
    AddDeliveryPointState mockedAddDeliveryPointState;

    @Test
    public void openMenuIntersectionTest(){
        mockedAddDeliveryPointState = new AddDeliveryPointState();

        Map map = new Map(null);

        Controller mockedController = mock(Controller.class);
        when(mockedController.getMap()).thenReturn(map);
        when(mockedController.getNumberOfCourier()).thenReturn(5);

        WindowBuilder windowBuilder = mock(WindowBuilder.class);
        IntersectionCircle idIntersection = new IntersectionCircle(0, 0, 0, null, null);

        mockedAddDeliveryPointState.openMenuIntersection(mockedController, windowBuilder, idIntersection);

        verify(windowBuilder).openMenuIntersection(map, 5, idIntersection);
    }


    
}
