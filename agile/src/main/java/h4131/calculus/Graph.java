package h4131.calculus;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.login.CredentialException;

import h4131.model.Courier;
import h4131.model.DeliveryPoint;
import h4131.model.Segment;
import h4131.model.TimeWindow;
import h4131.model.Tour;
import h4131.model.GlobalTour;


public class Graph implements TemplateGraph{

    private static final double MAX_COST = 10000;
	private static final double MIN_COST = 100;
    public Collection<DeliveryPoint> nodes;
    public Collection<Arc> arcs;
    private double [][] cost;
    private double [][] timeWindow;
    private int [] nbPlageHoraire;
    private TimeWindow [] listeWindow;
    private int nbNodes;
    private TimeWindow timeBegining;
    private DeliveryPoint wareHouse;
    private DeliveryPoint deliveryErreur;
    private int sizeNbTimeWindow;

    public Graph() {
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();
        
    }
    


    
    /**
     * At the end of TSP, we have a tab of indexes that correspond each to a deliveryPoint 
     * This method transform the tab into a collection of the associated deliveryPoint  
     * @param tab A tab of integer that represents the indexes of the deliveryPoints
     * @return A collection of DeliveryPoint
     */	
    private Collection<DeliveryPoint> tabToCollection(Integer [] tab){
        Collection<DeliveryPoint> bestTour= new LinkedList<>();
        int indexDelivery;
        for(Integer indexTab:tab){
            indexDelivery=0;
            if(indexTab==null){
                break;
            }
            for(DeliveryPoint d:nodes){
                if(indexDelivery==indexTab){
                    bestTour.add(d);
                    break;
                }
                indexDelivery++;

            }
        }

        return bestTour;
    }

    /** At the end of TSP, we have a collection of DeliveryPoint by which the courier passes 
     * This method gets the right arcs for each pair of successive points   
     * @param delivery a collection of deliveryPoint by which the courier has to pass in the right order
     * @return A collection of Arc
     */
    private Collection<Arc> findArc(Collection<DeliveryPoint> delivery){
        Collection<Arc> arcsResult=new LinkedList<Arc>();
        Iterator<DeliveryPoint> d= delivery.iterator();
        DeliveryPoint actual=d.next();
        DeliveryPoint suivant=actual;
        while(d.hasNext()){
            suivant=(DeliveryPoint)d.next();
            for(Arc a :arcs){
                if(actual==a.origin && a.destination==suivant){
                    arcsResult.add(a);
                }
                
                    
                
            }
            actual=suivant;
        }
        for(Arc a : arcs){
            if(a.origin==suivant && a.destination==wareHouse){
                arcsResult.add(a);
            }
        }

        return arcsResult;
    }

    /**
     * At the end of TSP, we have a tab of indexes that correspond each to a deliveryPoint 
     * This method transform the tab into a collection of the associated deliveryPoint  
     * @param indexDeliveryErreur The index of the deliveryPoint that can't be delivered 
     * @return The concerned DeliveryPoint
     */	
    public DeliveryPoint findDeliveryErreur(int indexDeliveryErreur){
        if(indexDeliveryErreur==-1){
            return null;
        }
        return nodes.toArray(new DeliveryPoint[nbNodes])[indexDeliveryErreur];
    }

     /**
     * Creates from all the arcs in the graph a list of all the segments corresponding to the arcs 
     * @return A list of all the segments in the graph
     */	
    private List<Segment> buildCourse(){
        List<Segment> course=new LinkedList<Segment>();
        for(Arc a:this.arcs){
            for(Segment s:a.path){
                course.add(s);
            }

        }
        return course;
    }

    /**
     * Compute the TSP for a GlobalTour
     * @param globalTour The globarTour from which we do the TSP
     */	
    public void computeBestTour(GlobalTour globalTour){
        TSP1 tsp=new TSP1();
        this.initialiseCompleteGraph();
        System.out.println("Graphs with "+this.nbNodes+" vertices:");
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(20000, this);
        deliveryErreur=findDeliveryErreur(tsp.getIndexDeliveryErreur());
        System.out.println("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(System.currentTimeMillis() - startTime)+"ms : ");
        this.nodes=tabToCollection(tsp.getSolution());
        this.arcs=findArc(this.nodes);
        Tour tour=new Tour();
        tour.setCourse(this.buildCourse());
        tour.setDeliveryPoints(new ArrayList<>(this.nodes));
        globalTour.addTour(tour);

    }

    //Initialise the cost board and the number of nodes
    private void initialiseCompleteGraph(){
        nbNodes=nodes.size();
        createCost();
        createWareHouse();
        createTimeWindow();
        createNbPlageHoraire();
        createWindow();
        createWindowBegining();
    }

    //Creates the cost function
    private void createCost(){
        int nodePos=0;
        int nodePos2=0;
        int arcExiste=0;
        cost= new double[nbNodes][nbNodes];
        for(DeliveryPoint node: nodes){
            for(DeliveryPoint node2 : nodes){
                if(node==node2){
                    cost[nodePos][nodePos2]=-1;
                }else{
                    for(Arc arc : arcs){
                        if(arc.origin==node && arc.destination==node2){
                            cost[nodePos][nodePos2]=arc.distance;
                            arcExiste=1;
                        }
                    }
                    if(arcExiste==0)
                        cost[nodePos][nodePos2]=Double.MAX_VALUE;
                }
                arcExiste=0;
                nodePos2++;
            }
            nodePos2=0;
            nodePos++;
        }
    }

    private void createWareHouse(){
        wareHouse=nodes.toArray(new DeliveryPoint[1])[0];
    }


    /**
     * Put the timeBeginning to the lowest TimeWindow amongs the nodes
     */	
    private void createWindowBegining(){
        TimeWindow min=TimeWindow.ELEVEN_TWELVE;
        for(DeliveryPoint d:nodes){
            if(d!=wareHouse && d.getTime().compareTo(min)<0){
                min=d.getTime();
            }

        }
        timeBegining=min;

    }

    /**
     * Convert the timeWindow 
     */	
    private void createTimeWindow(){
        int i=0;
        double resTimeWindow[][]=new double[nbNodes][2];
        for(DeliveryPoint node:nodes){
            resTimeWindow[i][0]=(double)(node.getTime().ordinal())+7.0;
            resTimeWindow[i][1]=(double)(node.getTime().ordinal())+8.0;
            i++;
        }
        this.timeWindow=resTimeWindow;
    }



    private void createNbPlageHoraire(){
        nbPlageHoraire=new int[nbNodes];
        Arrays.fill(nbPlageHoraire,0);
        Iterator<DeliveryPoint> d=nodes.iterator();
        d.next();
        DeliveryPoint actual;
        int plageHoraire=0;
        while(d.hasNext()){
           actual=d.next();
           nbPlageHoraire[actual.getTime().ordinal()-1]+=1;
        }
        for(int i:nbPlageHoraire){
            if(i!=0){
                plageHoraire+=1;
            }
        }
        this.sizeNbTimeWindow=plageHoraire;
        
    
    }
    private void createWindow(){
        listeWindow=new TimeWindow[nbNodes];
        int i=0;
        for(DeliveryPoint d : nodes){
            listeWindow[i]=d.getTime();
            i++;
        }
    }

	

	@Override
	public boolean isArc(int i, int j) {
        if (i<0 || i>=nodes.size() || j<0 || j>=nodes.size())
			return false;
		return i != j;
	}
    @Override
    public double timeTravel(int i, int j){
        return cost[i][j]/15000.0;
    }


    // ******************GET METHODS********************

    @Override
    public int getNbPlageHoraire(int plageHoraire){
        return nbPlageHoraire[plageHoraire];
    }
    @Override
    public TimeWindow getWindow(Integer deliveryPoint){
        return listeWindow[deliveryPoint];
    }
    @Override
    public TimeWindow getTimeBegining(){
        return timeBegining;
    }
    @Override
    public DeliveryPoint getDeliveryErreur(){
        return deliveryErreur;
    }

    @Override
	public double getCost(int i, int j) {
		if (i<0 || i>=nbNodes || j<0 || j>=nbNodes)
			return -1;
		return cost[i][j];
	}

    @Override
    public double getWindow(int firstOrLast,Integer i){
        return timeWindow[i][firstOrLast];
    }

    @Override
	public int getNbVertices() {
		return nbNodes;
	}
    @Override
    public int getSizeNbTimeWindow(){
        return sizeNbTimeWindow;
    }
    // ******************GET METHODS********************


    // ******************SET METHODS********************

    public void setCost(double[][] cost) {
        this.cost = cost;
    }

    public void setNodes(Collection<DeliveryPoint> nodes) {
        this.nodes = nodes;
    }

    public void setNbNodes(int nbNodes) {
        this.nbNodes = nbNodes;
    }

    public void setArcs(Collection<Arc> arcs) {
        this.arcs = arcs;
    }

    public void setTimeWindow(double[][] timeWindow) {
        this.timeWindow = timeWindow;
    }

    public void setNbPlageHoraire(int[] nbPlageHoraire) {
        this.nbPlageHoraire = nbPlageHoraire;
    }

    public void setListeWindow(TimeWindow[] listeWindow) {
        this.listeWindow = listeWindow;
    }

    public void setTimeBegining(TimeWindow timeBegining) {
        this.timeBegining = timeBegining;
    }

    public void setWareHouse(DeliveryPoint wareHouse) {
        this.wareHouse = wareHouse;
    }

    public void setDeliveryErreur(DeliveryPoint deliveryErreur) {
        this.deliveryErreur = deliveryErreur;
    }


    // ******************SET METHODS********************
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Arc a : arcs) {
            result.append("* From (" 
                + a.origin.getPlace().getLatitude() 
                + ", " 
                + a.origin.getPlace().getLongitude() 
                + ") to (" 
                + a.destination.getPlace().getLatitude() 
                + ", " 
                + a.destination.getPlace().getLongitude() 
                + "): " + a.distance + "\n");
        }
        return result.toString();
    }

    
    

    



}
