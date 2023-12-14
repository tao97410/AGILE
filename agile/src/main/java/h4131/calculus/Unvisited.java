package h4131.calculus;

import java.util.Arrays;
import java.util.Collection;

public class Unvisited {
    private Integer[][] candidates;
	private int[] initialiseNbCandidate;

    public Unvisited(Collection<Integer> unvisited, TemplateGraph g){
        this.candidates = new Integer[5][15];
		this.initialiseNbCandidate=new int[5];
		Arrays.fill(initialiseNbCandidate,0);
		for (Integer s : unvisited){
			candidates[g.getWindow(s).ordinal()-1][initialiseNbCandidate[g.getWindow(s).ordinal()-1]++] = s;
		}
    }


    public Integer[][] getCandidates(){
        return candidates;
    }
    public int[] getInitialiseNbCandidate(){
        return initialiseNbCandidate;
    }
    
}

