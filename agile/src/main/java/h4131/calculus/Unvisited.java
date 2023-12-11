package h4131.calculus;
import h4131.model.DeliveryPoint;
import h4131.model.TimeWindow;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;




public class Unvisited {
    private Integer[][] candidates;
	private int[] initialiseNbCandidate;
    public Unvisited(Collection<Integer> unvisited, TemplateGraph g){
        this.candidates = new Integer[5][10];
		this.initialiseNbCandidate=new int[5];
		Arrays.fill(initialiseNbCandidate,0);
        System.out.println(g.getSizeNbTimeWindow());
		for (Integer s : unvisited){
			candidates[g.getWindow(s).ordinal()-1][initialiseNbCandidate[g.getWindow(s).ordinal()-1]++] = s;
		}
    }
    public Integer[][] getCandidates(){
        return candidates;
    }
    public int[] initiliseNbCandidate(){
        return initialiseNbCandidate;
    }
    
}

