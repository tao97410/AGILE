package h4131.model;
import java.util.List;

public class GlobalTour {
    private List<Tour> tours;

    public GlobalTour(List<Tour> someTours){
        this.tours = someTours;
    }

    /**
     * @return List<Tour> return the tours
     */
    public List<Tour> getTours() {
        return tours;
    }

    /**
     * @param tours the tours to set
     */
    public void setTours(List<Tour> tours) {
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