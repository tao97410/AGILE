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


public class CompleteGraph implements Graph{
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

    public CompleteGraph() {
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();
        
    }
    
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

    @Override
	public int getNbVertices() {
		return nbNodes;
	}



    //After having computed the best tour, we have a tab of the indexes of the nodes 
    // Then we have to associate each index to the corresponding DeliveryPoint 
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
    private List<Segment> buildCourse(){
        List<Segment> course=new LinkedList<Segment>();
        for(Arc a:this.arcs){
            for(Segment s:a.path){
                course.add(s);

            }

        }
        return course;
    }
    private List<DeliveryPoint> buildListeDelivery(){
        List<DeliveryPoint> listeDelivery=new ArrayList<DeliveryPoint>();
        for(DeliveryPoint d:this.nodes){
            listeDelivery.add(d);
        }
        return listeDelivery;
    }
    


    public void computeBestTour(GlobalTour globalTour){
        TSP1 tsp=new TSP1();
        this.initialiseCompleteGraph();
        System.out.println("Graphs with "+this.nbNodes+" vertices:");
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(20000, this);
        System.out.println("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(System.currentTimeMillis() - startTime)+"ms : ");
        this.nodes=tabToCollection(tsp.getSolution());
        this.arcs=findArc(this.nodes);
        Tour tour=new Tour();
        tour.setCourse(this.buildCourse());
        tour.setDeliveryPoints(this.buildListeDelivery());
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
    private void createWindowBegining(){
        TimeWindow min=TimeWindow.ELEVEN_TWELVE;
        for(DeliveryPoint d:nodes){
            if(d!=wareHouse && d.getTime().compareTo(min)<0){
                min=d.getTime();
            }

        }
        timeBegining=min;

    }

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
        DeliveryPoint previous=nodes.toArray(new DeliveryPoint[1])[0];
        int plageHoraire=-1;
        while(d.hasNext()){
            actual=(DeliveryPoint)d.next();
            if(actual.getTime()==previous.getTime()){
                nbPlageHoraire[plageHoraire]+=1;

            }
            else{
                plageHoraire++;
                nbPlageHoraire[plageHoraire]+=1;
            }
            previous=actual;

            
        }
    
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
	public boolean isArc(int i, int j) {
        if (i<0 || i>=nodes.size() || j<0 || j>=nodes.size())
			return false;
		return i != j;
	}
    @Override
    public double timeTravel(int i, int j){
        return cost[i][j]/15000.0;
    }

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
    
    



}
