package h4131.calculus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import h4131.model.DeliveryPoint;

public class CompleteGraph implements Graph{
	private static final double MAX_COST = 10000;
	private static final double MIN_COST = 100;
    public Collection<DeliveryPoint> nodes;
    public Collection<Arc> arcs;
    double [][] cost;
    int nbNodes;

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
    public Collection<DeliveryPoint> tabToCollection(Integer [] tab){
        Collection<DeliveryPoint> bestTour= new LinkedList<>();
        int indexDelivery;
        for(Integer indexTab:tab){
            indexDelivery=0;
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
        while(d.hasNext()){
            DeliveryPoint suivant=(DeliveryPoint)d.next();
            for(Arc a :arcs){
                if(a.origin==d && a.destination==suivant){
                    arcsResult.add(a);
                }
            }
        }
        return arcsResult;
    }
    


    public void computeBestTour(){
        TSP1 tsp=new TSP1();
        this.initialiseCompleteGraph();
        System.out.println("Graphs with "+this.nbNodes+" vertices:");
        long startTime = System.currentTimeMillis();
        tsp.searchSolution(20000, this);
        System.out.println("Solution of cost "+tsp.getSolutionCost()+" found in "
                +(System.currentTimeMillis() - startTime)+"ms : ");
        this.nodes=tabToCollection(tsp.getSolution());
        this.arcs=findArc(this.nodes);

    }

    //Initialise the cost board and the number of nodes
    private void initialiseCompleteGraph(){
        nbNodes=nodes.size();
        createCost();

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

	@Override
	public double getCost(int i, int j) {
		if (i<0 || i>=nbNodes || j<0 || j>=nbNodes)
			return -1;
		return cost[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
        if (i<0 || i>=nodes.size() || j<0 || j>=nodes.size())
			return false;
		return i != j;
	}



}
