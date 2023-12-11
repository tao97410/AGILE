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
	protected int bound(Integer currentVertex, Unvisited unvisited) {
		return 0;
	}

	

	


}
