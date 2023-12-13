package h4131.calculus;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import h4131.model.DeliveryPoint;

public class TSP1 extends TemplateTSP {


	/**
	 * Initialise values for the TSP
	 */
	public TSP1(){
		bestSol=null;
		g=null;
		bestSolCost=-1;
		timeLimit=-1;
		startTime=-1;
		indexDeliveryError=-1;
	}

}
