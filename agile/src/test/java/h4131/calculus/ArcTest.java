package h4131.calculus;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import h4131.model.DeliveryPoint;
import h4131.model.Intersection;
import h4131.model.Segment;
import h4131.model.TimeWindow;

public class ArcTest {
    Arc mockedArc;
    @Test
    void testEquals() {
        mockedArc = new Arc(new DeliveryPoint(new Intersection(1, 2, 3), TimeWindow.TEN_ELEVEN),new DeliveryPoint(new Intersection(4, 5, 6), TimeWindow.TEN_ELEVEN), 7);
        Arc result = new Arc(new DeliveryPoint(new Intersection(1, 2, 3), TimeWindow.TEN_ELEVEN),new DeliveryPoint(new Intersection(4, 5, 6), TimeWindow.TEN_ELEVEN), 7);

        Segment seg1 = new Segment(new Intersection(1, 2, 3), new Intersection(4, 5, 6), 7, "rue");

        mockedArc.path.add(seg1);
        result.path.add(seg1);

        assertTrue(mockedArc.equals(result));
    }
}
