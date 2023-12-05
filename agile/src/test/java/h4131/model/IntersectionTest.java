package h4131.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class IntersectionTest {

    
    Intersection mockedIntersection;

    @Test
    void testEquals() {
        mockedIntersection = new Intersection(12, 5, 6);
        Intersection result = new Intersection(12, 5, 6);
        assertTrue(mockedIntersection.equals(result));
    }
}
