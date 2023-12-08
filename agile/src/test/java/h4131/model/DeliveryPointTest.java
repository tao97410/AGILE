package h4131.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class DeliveryPointTest {

    DeliveryPoint mockedDP;
    @Test
    void testEquals() {
        mockedDP = new DeliveryPoint(new Intersection(1, 2, 3), TimeWindow.EIGHT_NINE);
        DeliveryPoint result = new DeliveryPoint(new Intersection(1, 2, 3), TimeWindow.EIGHT_NINE);
        assertTrue(mockedDP.equals(result));
    }
}
