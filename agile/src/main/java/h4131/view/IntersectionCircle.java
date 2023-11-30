package h4131.view;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class IntersectionCircle extends Circle {
    
    private Long intersectionId;

    public IntersectionCircle(double x, double y, double radius, Paint color, Long id){
        super(x, y, radius, color);
        setIntersectionId(id);
    }

    public void setIntersectionId(Long id){
        this.intersectionId = id;
    }

    public Long getIntersectionId(){
        return intersectionId;
    }
}
