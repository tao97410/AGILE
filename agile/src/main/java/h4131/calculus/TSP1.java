package h4131.calculus;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {


	public TSP1(){
		bestSol=null;
		g=null;
		bestSolCost=-1;
		timeLimit=-1;
		startTime=-1;
		indexDeliveryErreur=-1;
	}

	@Override
	protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
		return 0;
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, TemplateGraph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

	


}
