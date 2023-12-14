package h4131.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import h4131.calculus.Arc;
import h4131.calculus.Graph;
import h4131.model.Intersection;
import h4131.model.Segment;

public class PDFgenerator {

    public enum Instruction {
        CONTINUE,
        RIGHT,
        LEFT,
        GO_BACK
    }

    /**
     * Generates the PDF file containing instructions
     * @param graph Graph considered
     * @param courierId Id of the courier
     * @param path Path of the PDF file
     */
    public static void getPdfInstructions(Graph graph, long courierId, String path) {
        String instructions = getInstructionsForTour(graph, courierId);
        generatePdf(path, instructions);
    }

    /**
     * Generating the courrier instructions to execute a tour
     * @param graph Graph considered
     * @param courierId Id of the courier
     * @return String containing all the instructions
     */
    private static String getInstructionsForTour(Graph graph, long courierId) {

        StringBuilder result = new StringBuilder();

        result.append("\n# INSTRUCTIONS FOR THE COURIER " + courierId + " #\n");

        List<Arc> arcsList = new LinkedList<>(graph.arcs);
        int deliveryCount = 0;
        int intersectionsCount = 0;
        int instructionCount = 1;
        double metersCount = 0.0;
        Segment previousSeg = null;
        
        for (Arc a : arcsList) {
            
            if (deliveryCount < arcsList.size() - 1) {
                result.append("\n   >>> Delivery n°" + (deliveryCount + 1) + " to " + a.destination.getPlace().toString() + " <<<\n");
            } else {
                result.append("\n   >>> Come back to the warehouse <<<\n");
            }

            int segCount = 0;
            
            for (Segment s : a.path) {

                if (segCount == 0) {
                    
                    metersCount = s.getLength();
                    intersectionsCount = 0;
                    
                    if (deliveryCount == 0) {
                        segCount ++;
                        previousSeg = s;
                        result.append("1) Start from the warehouse\n");
                        result.append("2) Take " + s.getName() + "\n");
                        instructionCount = 3;
                        continue;
                    }
                    

                }

                if (segCount > 0)
                    intersectionsCount ++;

                Instruction currentInstruction = getInstruction(previousSeg, s);
                switch (currentInstruction) {
                    case CONTINUE:
                        break;
                    case RIGHT:
                        if (segCount > 0) {
                            result.append(instructionCount + ") Continue on " + previousSeg.getName() + " for " + (int) metersCount + " meters\n");
                            result.append((instructionCount + 1) + ") At the ");
                            if (intersectionsCount == 1) {
                                result.append("1st");
                            } else if (intersectionsCount == 2) {
                                result.append("2nd");
                            } else if (intersectionsCount == 3) {
                                result.append("3rd");
                            } else {
                                result.append(intersectionsCount + "th");
                            }
                            result.append(" intersection, turn right on " + s.getName() + "\n");
                            metersCount = 0.0;
                            intersectionsCount = 0;
                            instructionCount += 2;
                        } else {
                            result.append(instructionCount + ") Turn right on " + s.getName() + "\n");
                            instructionCount ++;
                        }
                        break;
                    case LEFT:
                        if (segCount > 0) {
                            result.append(instructionCount + ") Continue on " + previousSeg.getName() + " for " + (int) metersCount + " meters\n");
                            result.append((instructionCount + 1) + ") At the ");
                            if (intersectionsCount == 1) {
                                result.append("1st");
                            } else if (intersectionsCount == 2) {
                                result.append("2nd");
                            } else if (intersectionsCount == 3) {
                                result.append("3rd");
                            } else {
                                result.append(intersectionsCount + "th");
                            }
                            result.append(" intersection, turn left on " + s.getName() + "\n");
                            metersCount = 0.0;
                            intersectionsCount = 0;
                            instructionCount += 2;
                        } else {
                            result.append(instructionCount + ") Turn left on " + s.getName() + "\n");
                            instructionCount ++;
                        }
                        break;
                    case GO_BACK:
                        result.append(instructionCount + ") Go back on " + s.getName() + "\n");
                        if (segCount > 0) 
                            metersCount = 0.0;
                        intersectionsCount = 0;
                        instructionCount ++;
                        break;
                    default:
                        break;
                }

                if (segCount > 0) {
                    metersCount += s.getLength();
                }

                previousSeg = s;
                segCount ++;
            }

            
            if (deliveryCount < arcsList.size() - 1) {
                intersectionsCount ++;
                result.append(instructionCount + ") Continue on " + previousSeg.getName() + " for " + (int) metersCount + " meters\n");
                result.append((instructionCount + 1) + ") Deliver the package at the ");
                if (intersectionsCount == 1) {
                    result.append("1st");
                } else if (intersectionsCount == 2) {
                    result.append("2nd");
                } else if (intersectionsCount == 3) {
                    result.append("3rd");
                } else {
                    result.append(intersectionsCount + "th");
                }
                result.append(" intersection " + a.destination.getPlace().toString() + "\n");
                metersCount = 0.0;
                intersectionsCount = 0;
                instructionCount = 1;
            }

            deliveryCount ++;
        
        }
        
        result.append(instructionCount + ") Continue on " + previousSeg.getName() + " for " + (int) metersCount + " meters\n");
        result.append((instructionCount + 1) + ") You're back to the warehouse, tour completed!\n");
        return result.toString();
    
    }

    /**
     * Gets the instruction about the change of direction from one segment to another
     * @param seg1 First segment
     * @param seg2 Second segment
     * @return The correct instruction
     */
    private static Instruction getInstruction(Segment seg1, Segment seg2) {
        
        if (seg1.getOrigin().equals(seg2.getDestination())) {
        
            return Instruction.GO_BACK;
        
        } else if (seg1.getName().equals(seg2.getName())) {
            
            return Instruction.CONTINUE;
        
        } else {
            
            Intersection pointA = seg1.getOrigin();
            Intersection pointB = seg1.getDestination();
            Intersection pointC = seg2.getDestination();

            double slope = (pointB.getLatitude() - pointA.getLatitude()) / (pointB.getLongitude() - pointA.getLongitude());
            double intercept = pointA.getLatitude() - slope * pointA.getLongitude();

            double above = pointC.getLatitude() - slope * pointC.getLongitude() - intercept;
            double right = pointB.getLongitude() - pointA.getLongitude();

            if (right * above < 0) {

                return Instruction.RIGHT;

            } else {

                return Instruction.LEFT;

            }
        }
    }

    /**
     * Generates a PDF file
     * @param path Path of the file
     * @param content Content of the file
     */
    private static void generatePdf(String path, String content) {
        Document document = new Document();
        try {
            com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(PDFgenerator.class.getResource("/h4131/main_logo.png"));
            img.scalePercent(10);
            img.setAlignment(Image.RIGHT);
            try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            
            document.open();
            try {
                document.add(img);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Font font = FontFactory.getFont(FontFactory.COURIER, 10, new BaseColor(33, 69, 134));
            Paragraph words = new Paragraph(content, font);
            words.setLeading((float) 12.0);

            try {
                document.add(words);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.close();
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        
    }

}
