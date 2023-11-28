import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args) {
        Courier c1 = new Courier(0, "Pierre");
       
        Intersection i1 = new Intersection(0, 45, 10);
        Intersection i2 = new Intersection(1, 45, 11);

        Intersection i3 = new Intersection(3, 35, 10);
        Intersection i4 = new Intersection(4, 35, 11);

        Segment s1 = new Segment(i1, i2, 10, "Rue test1");
        Segment s2 = new Segment(i3, i4, 15, "Rue test2");

        Tour t1 = new Tour(c1);
        t1.addSegment(s1);
        t1.addSegment(s2);

        DeliveryPoint d1 = new DeliveryPoint(i4, TimeWindow.EIGHT_NINE);   

        t1.addDeliveryPoint(d1);

        List<Tour> tours = new ArrayList<Tour>();
        tours.add(t1);

        GlobalTour g1 = new GlobalTour(tours);

        System.out.println(g1.getTours().get(0).getCourse().get(0).getLength());


    }
}
