package h4131.model;
import java.util.Collection;
import java.util.ArrayList;

public class GlobalTour {
    private Collection<Tour> tours;
    private String nameOfMapAssociated;

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
    
    /**
     * Adds a tour to a global tour
     * @param tour Tour to add
     */
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

    public String getMap(){
        return nameOfMapAssociated;
    }

    public void setMap(String name){
        this.nameOfMapAssociated = name;
    }
}