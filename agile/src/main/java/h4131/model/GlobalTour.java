package h4131.model;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collection;

public class GlobalTour {

    private Collection<Tour> tours;

    public GlobalTour(){
        this.tours=new ArrayList<>();
    }
    
    public GlobalTour(Collection<Tour> someTours){
        this.tours = someTours;

    }

    /**
     * @return List<Tour> return the tours
     */
    public Collection<Tour> getTours() {
        return tours;
    }

    /**
     * @param tours the tours to set
     */
    public void setTours(Collection<Tour> tours) {
        this.tours = tours;
    }
    
    public void addTour (Tour tour){
        tours.add(tour);
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        for(Tour tour : tours){
            result.append(tour).append("\n");
        }
        return result.toString();
    }
}