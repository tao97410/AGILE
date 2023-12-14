package h4131.calculus;


import java.util.List;

import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;

public interface TemplateGraph {

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

 
	/**
     * Compute the time in seconds to go from i to j
     * @param i beginning of an arc
     * @param j end of an arc
     * @return the time in seconds to go from i to j
     */
	public abstract double timeTravel(int i, int j);

	public abstract double getWindow(int firstOrLast,Integer i);


	public abstract int getnbDeliveryPointByTimeSlot(int plageHoraire);

	public abstract TimeWindow getWindow(Integer deliveryPoint);

	public abstract TimeWindow getTimeBegining();

	public abstract DeliveryPoint getDeliveryErreur();


	public abstract int getSizeNbTimeWindow();

	public abstract DeliveryPoint getTabDeliveryPoint(int index);

	public abstract DeliveryPoint getWareHouse();
}
