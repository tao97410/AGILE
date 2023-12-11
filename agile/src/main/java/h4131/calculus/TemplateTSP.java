package h4131.calculus;
import h4131.model.TimeWindow;
import javafx.scene.effect.Light.Spot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;



public abstract class TemplateTSP implements TSP {
	protected Integer[] bestSol;
	protected TemplateGraph g;
	protected double bestSolCost;
	protected int timeLimit;
	protected long startTime;
	protected int indexDeliveryErreur;
	private int maxNbSommetVisited=0;
	


	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param timeLimit stops the research if the timeLimit is exceeded
	 * @param g the completeGraph for which we want to search a solution
	 */	
	public void searchSolution(int timeLimit, Graph g){
		if (timeLimit <= 0) return;
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
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract int bound(Integer currentVertex, Unvisited unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */

	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(int currentVertex, Unvisited unvisited, 
		Collection<Integer> visited, double currentCost,double time,int nbSommetVisited){
		System.out.println("Grande boucle");
		if (System.currentTimeMillis() - startTime > timeLimit) return;
		SeqIter it=new SeqIter(unvisited, g,currentVertex);
	    if (!it.hasNext()){ 
			
	    	if(g.getCost(currentVertex,0)==Double.MAX_VALUE && nbSommetVisited>=this.maxNbSommetVisited){
				if(currentCost<bestSolCost){
					bestSolCost=currentCost;
					visited.toArray(bestSol);
				}

			}
			else if(g.getCost(currentVertex,0)!=Double.MAX_VALUE){
				if(currentCost+g.getCost(currentVertex, 0)<bestSolCost){
					bestSolCost=currentCost+g.getCost(currentVertex, 0);
					visited.toArray(bestSol);
				}
			}

			
	    		
	    	
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){

	        
			
	        while (it.hasNext()){
				
	        	Integer nextVertex = it.next();

				if(g.getCost(currentVertex,nextVertex)!= Double.MAX_VALUE && time+g.timeTravel(currentVertex,nextVertex)+5.0/60.0>g.getWindow(1,nextVertex)){
					
					if(nbSommetVisited>=this.maxNbSommetVisited){
						if(nbSommetVisited==this.maxNbSommetVisited){
							if(currentCost+g.getCost(currentVertex,nextVertex)<bestSolCost){
								visited.toArray(bestSol);
								bestSolCost = currentCost+g.getCost(currentVertex,nextVertex);
								indexDeliveryErreur=nextVertex;
							}
						}
						else{
							this.maxNbSommetVisited=nbSommetVisited;
							visited.toArray(bestSol);
							bestSolCost = currentCost+g.getCost(currentVertex,nextVertex);
							indexDeliveryErreur=nextVertex;
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
					Integer develeryPointDelete=it.remove(nextVertex);
					branchAndBound(nextVertex, unvisited, visited, 
							currentCost+g.getCost(currentVertex, nextVertex),newTime,nbSommetVisited++);
					visited.remove(nextVertex);
					it.addFollowing(develeryPointDelete);
				}
	        }	    
	    }
	}

	// ******************GET METHODS********************
	
	public int getIndexDeliveryErreur(){
		return indexDeliveryErreur;
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


	// ******************SET METHODS********************
	
	

}
