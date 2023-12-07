package h4131.calculus;

import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
	public abstract int getNbVertices();

	/**
	 * @param i 
	 * @param j 
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
	public abstract double getCost(int i, int j);

	/**
	 * @param i 
	 * @param j 
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
	public abstract boolean isArc(int i, int j);

	public abstract double timeTravel(int i, int j);

	public abstract double getWindow(int firstOrLast,Integer i);

	public abstract int getNbPlageHoraire(int plageHoraire);

	public abstract TimeWindow getWindow(Integer deliveryPoint);

	public abstract TimeWindow getTimeBegining();
}
