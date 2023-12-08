package h4131.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TourTest {

    @Mock
    Tour tourMocked;

    // @Test
    // void testAddDeliveryPoint() {

    // }

    @Test
    void testAddSegment() {
        tourMocked = new Tour(null);

        Intersection origine = new Intersection(1, 1, 1);
        Intersection dest = new Intersection(2, 2, 2);
        Segment segment = new Segment(origine, dest, 10.0, "a name");

        List<Segment> result = new ArrayList<>();
        result.add(segment);

        tourMocked.addSegment(segment);

        assertEquals(tourMocked.getCourse(),result);
    }
}
