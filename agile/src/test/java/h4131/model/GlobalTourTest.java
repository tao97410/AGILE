package h4131.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class GlobalTourTest {
    
    @Mock
    private GlobalTour mockedGlobalTour;
    
    @Test
    void testAddIntersection() {
        mockedGlobalTour = new GlobalTour();
        Tour tourInstance = new Tour(1);
        mockedGlobalTour.addTour(tourInstance);
        assertTrue(mockedGlobalTour.getTours().contains(tourInstance));
    }
}
