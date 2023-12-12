package h4131.calculus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected TemplateGraph g;
	private double bestSolCost;
	private int timeLimit;
	private long startTime;
	private int indexDeliveryErreur=-1;
	
	
	public void searchSolution(int timeLimit, Graph g){
		if (timeLimit <= 0) return;
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;
		bestSol = new Integer[g.getNbVertices()];
		Collection<Integer> unvisited = new ArrayList<Integer>(g.getNbVertices()-1);
		for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
		Collection<Integer> visited = new ArrayList<Integer>(g.getNbVertices());
		visited.add(0); // The first visited vertex is 0
		bestSolCost = Double.MAX_VALUE;
		branchAndBound(0, unvisited, visited, 0,g.getTimeBegining().ordinal()+7.0,0,g.getTimeBegining().ordinal(),0);
	}
	
	public Integer[] getSolution(){
		return bestSol;
	}
	
	public double getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, TemplateGraph g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(int currentVertex, Collection<Integer> unvisited, 
			Collection<Integer> visited, double currentCost,double time,int nbSommetVisited,int plageHoraire,int nbPlageHoraire){
		int maxNbSommetVisited=0;
		int newNbPlageHoraire=nbPlageHoraire+1;
		if (System.currentTimeMillis() - startTime > timeLimit) return;
	    if (unvisited.size() == 0){ 
	    	if (g.isArc(currentVertex,0)){ 
	    		visited.toArray(bestSol);
	    		bestSolCost = currentCost+g.getCost(currentVertex,0);
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
	        Iterator<Integer> it = iterator(currentVertex, unvisited, g);
			

			
	        while (it.hasNext()){
				
	        	Integer nextVertex = it.next();
				
				
				
				if(g.getNbPlageHoraire(plageHoraire-1)!=nbPlageHoraire && g.getWindow(nextVertex).ordinal()!=plageHoraire){
					
					continue;
				}
				
				else if(g.getNbPlageHoraire(plageHoraire-1)==nbPlageHoraire){
					newNbPlageHoraire=1;
				}
				
				if(g.getCost(currentVertex,nextVertex)!= Double.MAX_VALUE && time+g.timeTravel(currentVertex,nextVertex)+5.0/60.0>g.getWindow(1,nextVertex)){
					
					if(nbSommetVisited>=maxNbSommetVisited){
						if(nbSommetVisited==maxNbSommetVisited){
							if(currentCost+g.getCost(currentVertex,nextVertex)<bestSolCost){
								visited.toArray(bestSol);
								bestSolCost = currentCost+g.getCost(currentVertex,nextVertex);
								indexDeliveryErreur=nextVertex;
							}
						}
						else{
							

							visited.toArray(bestSol);
							bestSolCost = currentCost+g.getCost(currentVertex,nextVertex);
							indexDeliveryErreur=nextVertex;
							
						}
					}
					//verifier le bestSol
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
					unvisited.remove(nextVertex);
					branchAndBound(nextVertex, unvisited, visited, 
							currentCost+g.getCost(currentVertex, nextVertex),newTime,nbSommetVisited++,g.getWindow(nextVertex).ordinal(),newNbPlageHoraire);
					visited.remove(nextVertex);
					unvisited.add(nextVertex);
				}
	        }	    
	    }
	}
	public int getIndexDeliveryErreur(){
		return indexDeliveryErreur;
	}
	

}
