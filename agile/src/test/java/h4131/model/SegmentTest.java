package h4131.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class SegmentTest {

    Segment mockedSegment;
    @Test
    void testEquals() {
        mockedSegment = new Segment(new Intersection(1, 2, 3), new Intersection(4, 5, 6),   10, "coucou");
        Segment result = new Segment(new Intersection(1, 2, 3), new Intersection(4, 5, 6),   10, "coucou");
        assertTrue(mockedSegment.equals(result));

    }
}
