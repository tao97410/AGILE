package h4131.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class TourTest {

    @Mock
    private Tour mockedTour;

    @Test
    void testAddDeliveryPoint() {
        mockedTour = new Tour(1);
        DeliveryPoint pointInstance = new DeliveryPoint(new Intersection(1, 1.0, 1.0), TimeWindow.EIGHT_NINE);
        mockedTour.addDeliveryPoint(pointInstance);
        assertTrue(mockedTour.getDeliveryPoints().contains(pointInstance));
    }

    @Test
    void testAddSegment() {
        mockedTour = new Tour(1);
        Segment segmentInstance = new Segment(new Intersection(1, 1.0, 1.0), new Intersection(1, 2.0, 2.0), 10.0, "Rue de l'INSA");
        mockedTour.addSegment(segmentInstance);
        assertTrue(mockedTour.getCourse().contains(segmentInstance));
    }
}
