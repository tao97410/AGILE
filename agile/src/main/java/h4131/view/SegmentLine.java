package h4131.view;

import h4131.model.Segment;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class SegmentLine extends Line{

    private Segment segment;
    private Paint previousColor;

    public SegmentLine(double startX, double startY, double endX, double endY, Segment aSegment){
        super(startX, startY, endX, endY);
        this.segment = aSegment;
    }

    public void setSegment(Segment aSegment){
        this.segment = aSegment;
    }

    public Segment getSegment(){
        return this.segment;
    }

    public void setPreviousColor(Paint aPreviousColor){
        this.previousColor = aPreviousColor;
    }

    public Paint getPreviousColor(){
        return this.previousColor;
    }
}
