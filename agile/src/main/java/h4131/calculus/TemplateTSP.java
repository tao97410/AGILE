package h4131.calculus;

import java.util.ArrayList;
import java.util.Collection;



public abstract class TemplateTSP implements TSP {
	protected Integer[] bestSol;
	protected TemplateGraph g;
	protected double bestSolCost;
	protected int timeLimit;
	protected long startTime;
	protected int indexDeliveryError;
	private int maxnbVisitedNodes=0;
	private boolean timeExceeded=false;
	


	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param timeLimit stops the research if the timeLimit is exceeded
	 * @param g the completeGraph for which we want to search a solution
	 */	
	public void searchSolution(int timeLimit, Graph g){
		if (timeLimit <= 0){
			return;
		}
		
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;
		bestSol = new Integer[g.getNbVertices()];
		Collection<Integer> unvisitedCollection = new ArrayList<Integer>(g.getNbVertices()-1);
		for (int i=1; i<g.getNbVertices(); i++) unvisitedCollection.add(i);
		Unvisited unvisited = new Unvisited(unvisitedCollection, g);
		Collection<Integer> visited = new ArrayList<Integer>(g.getNbVertices());
		visited.add(0); // The first visited vertex is 0
		bestSolCost = Double.MAX_VALUE;
		branchAndBound(0, unvisited, visited, 0,g.getTimeBegining().ordinal()+7.0,0);
	}
	

	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 * @param time the current run time of the method
	 * @param nbVisitedNodes the number of visited nodes 
	 */	
	private void branchAndBound(int currentVertex, Unvisited unvisited, 
		Collection<Integer> visited, double currentCost,double time,int nbVisitedNodes){
		if (System.currentTimeMillis() - startTime > timeLimit) {
			this.timeExceeded=true;
			return;
		}
		SeqIter it=new SeqIter(unvisited, g,currentVertex);
	    if (!it.hasNext()){ 
	    	if(g.getCost(currentVertex,0)==Double.MAX_VALUE && nbVisitedNodes>=this.maxnbVisitedNodes){
				if(currentCost<bestSolCost){
					bestSolCost=currentCost;
					visited.toArray(bestSol);
					maxnbVisitedNodes=nbVisitedNodes;
				}

			}
			else if(g.getCost(currentVertex,0)!=Double.MAX_VALUE){
				if(currentCost+g.getCost(currentVertex, 0)<bestSolCost){
					bestSolCost=currentCost+g.getCost(currentVertex, 0);
					visited.toArray(bestSol);
					maxnbVisitedNodes=nbVisitedNodes+1;
				}
			}
	    } else if (currentCost<bestSolCost){
	        while (it.hasNext()){
	        	Integer nextVertex = it.next();
				if(g.getCost(currentVertex,nextVertex)!= Double.MAX_VALUE && time+g.timeTravel(currentVertex,nextVertex)+5.0/60.0>g.getWindow(1,nextVertex)){
					if(nbVisitedNodes>=this.maxnbVisitedNodes){
						if(nbVisitedNodes==this.maxnbVisitedNodes){
							if(currentCost+g.getCost(currentVertex,nextVertex)<bestSolCost){
								visited.toArray(bestSol);
								bestSolCost = currentCost+g.getCost(currentVertex,nextVertex);
								indexDeliveryError=nextVertex;
								this.maxnbVisitedNodes=nbVisitedNodes;
							}
						}
						else{
							this.maxnbVisitedNodes=nbVisitedNodes;
							visited.toArray(bestSol);
							bestSolCost = currentCost+g.getCost(currentVertex,nextVertex);
							indexDeliveryError=nextVertex;
						}
					}
				}
				else{
					double newTime;
					if(time+g.timeTravel(currentVertex,nextVertex)+5.0/60.0<g.getWindow(0,nextVertex)){
						newTime=g.getWindow(0,nextVertex)+5.0/60.0;
					}
					else{
						newTime=time+g.timeTravel(currentVertex,nextVertex)+5.0/60.0;
					}
					visited.add(nextVertex);
					Integer develeryPointDelete=it.remove();
					branchAndBound(nextVertex, unvisited, visited, 
							currentCost+g.getCost(currentVertex, nextVertex),newTime,nbVisitedNodes++);
					visited.remove(nextVertex);
					it.addFollowing(develeryPointDelete);
				}
	        }	    
	    }
	}

	// ******************GET METHODS********************
	
	public int getIndexDeliveryError(){
		return indexDeliveryError;
	}


	public Integer[] getSolution(){
		return bestSol;
	}
	
	public double getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}

	// ******************GET METHODS********************


	// ******************SET METHODS********************
		
	public void setG(TemplateGraph g) {
		this.g = g;
	}

	public void setBestSolCost(double bestSolCost) {
		this.bestSolCost = bestSolCost;
	}
	public boolean getTimeExceeded(){
		return this.timeExceeded;
	}


	// ******************SET METHODS********************
	
	

}
