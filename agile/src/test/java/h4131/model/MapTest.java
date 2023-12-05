package h4131.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class MapTest {

    @Mock
    private Map mockedMap;


    @Test
    void mockedMapAddIntersection() {
        mockedMap= new Map();
        new Intersection(10,20,30);
        mockedMap.addIntersection(new Intersection(10,20,30));
        assertNotNull(mockedMap.getIntersections().get((long)10));
    }

    @Test
    void mockedMapAddSegment() { 
        mockedMap= new Map();
        Segment s= new Segment(new Intersection(10,20,30), new Intersection(20,30,40), 10, "Test");
        mockedMap.addSegment(s);
        assertNotNull(mockedMap.getAdjacency().get(s.getOrigin().getId()));
    }

    @Test
    void mockedMapAddSegmentWithOriginExisting() { 
        mockedMap= new Map();
        Segment s= new Segment(new Intersection(10,20,30), new Intersection(20,30,40), 10, "Test");
        mockedMap.addSegment(s);
        Segment sameOrigin= new Segment(new Intersection(10,20,30), new Intersection(40,50,60), 20, "sameOrigin");
        assertNotNull(mockedMap.getAdjacency().get(sameOrigin.getOrigin().getId()));
    }

    
    @Test
    void mockedMapHasIntersection() {
        mockedMap= new Map();
        mockedMap.addIntersection(new Intersection(10,20,30));
        assertEquals(mockedMap.hasIntersection(10),true);
    }
    

    @Test
    void mockedMapGetAdjacency() {

    }

    @Test
    void mockedMapGetDestinationsById() {

    }

    @Test
    void mockedMapGetGraphFromPoints() {

    }

    @Test
    void mockedMapGetIntersectionById() {

    }

    @Test
    void mockedMapGetIntersections() {

    }

    @Test
    void mockedMapGetWarehouse() {

    }


    @Test
    void mockedMapSetAdjacency() {

    }

    @Test
    void mockedMapSetIntersections() {

    }

    @Test
    void mockedMapSetWarehouse() {

    }

    @Test
    void mockedMapToString() {

    }
}
