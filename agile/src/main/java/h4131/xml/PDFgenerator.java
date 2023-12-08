package h4131.xml;

import java.util.LinkedList;
import java.util.List;

import h4131.calculus.Arc;
import h4131.calculus.Graph;
import h4131.model.Intersection;
import h4131.model.Segment;
import h4131.model.Tour;

public class PDFgenerator {

    public enum Instruction {
        CONTINUE,
        RIGHT,
        LEFT,
        GO_BACK
    }

    

    /**
     * Generating the courrier instructions to execute a tour
     * @param tour Tour considered
     * @param graph Graph considered
     * @return String containing all the instructions
     */
    public static String getInstructionsForTour(Tour tour, Graph graph) {
        
        StringBuilder result = new StringBuilder("~ Instructions for the courrier " + tour.getCourierId() + " ~\n\n");
        result.append("Start from the warehouse\n");

        List<Arc> arcsList = new LinkedList<>(graph.arcs);
        int deliveryCount = 0;
        
        for (Arc a : arcsList) {
            
            result.append("\n>>> Delivery n°" + deliveryCount + " to " + a.destination.getPlace().toString() + ":\n");
            int segCount = 0;
            Segment previousSeg = null;
            
            for (Segment s : a.path) {

                if (segCount == 0) {
                
                    result.append("Take " + s.getName() + "\n");
                    segCount ++;
                    previousSeg = s;
                    continue;
                
                } else {
                
                    Instruction currentInstruction = getInstruction(previousSeg, s);
                    switch (currentInstruction) {
                        case CONTINUE:
                            result.append("Continue on " + s.getName() + "\n");
                            break;
                        case RIGHT:
                            result.append("Turn right to " + s.getName() + "\n");
                            break;
                        case LEFT:
                            result.append("Turn left to " + s.getName() + "\n");
                            break;
                        case GO_BACK:
                            result.append("Go back on " + s.getName() + "\n");
                            break;
                        default:
                            break;
                    }
                    segCount ++;
                
                }
            }

            result.append("Deliver the package to " + a.destination.getPlace().toString() + "\n");
            deliveryCount ++;
        }
        
        result.append("Go back to the warehouse, tour completed!\n");

        return result.toString();
    
    }

    /**
     * Gets the instruction about the change of direction from one segment to another
     * @param seg1 First segment
     * @param seg2 Second segment
     * @return The correct instruction
     */
    private static Instruction getInstruction(Segment seg1, Segment seg2) {
        
        if (seg1.getName().equals(seg2.getName())) {
        
            return Instruction.CONTINUE;
        
        } else if (seg1.getOrigin().equals(seg2.getDestination())) {
            
            return Instruction.GO_BACK;
        
        } else {
            
            Intersection pointA = seg1.getOrigin();
            Intersection pointB = seg1.getDestination();
            Intersection pointC = seg2.getDestination();

            double slope = (pointB.getLatitude() - pointA.getLatitude()) / (pointB.getLongitude() - pointA.getLongitude());
            double intercept = pointA.getLatitude() - slope * pointA.getLongitude();

            double below = pointC.getLatitude() - slope * pointC.getLongitude() - intercept;
            double right = pointB.getLongitude() - pointA.getLongitude();

            if ((right > 0 && below > 0) || (right < 0 && below < 0)) {

                return Instruction.RIGHT;

            } else {

                return Instruction.LEFT;

            }
        }
    }

}
