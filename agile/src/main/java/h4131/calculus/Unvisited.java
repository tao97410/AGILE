package h4131.calculus;

import java.util.Arrays;
import java.util.Collection;

public class Unvisited {
    private Integer[][] candidates;
	private int[] initialiseNbCandidate;
    private int[] nbCandidateTotal;

    public Unvisited(Collection<Integer> unvisited, TemplateGraph g){
        this.candidates = new Integer[5][15];
		this.initialiseNbCandidate=new int[5];
		Arrays.fill(initialiseNbCandidate,0);
        this.nbCandidateTotal=new int[5];
		for (Integer s : unvisited){
			candidates[g.getWindow(s).ordinal()-1][initialiseNbCandidate[g.getWindow(s).ordinal()-1]++] = s;
		}
        for(int index=0;index<5;index++){
            this.nbCandidateTotal[index]=initialiseNbCandidate[index];
        }
    }


    public Integer[][] getCandidates(){
        return candidates;
    }
    public int[] getInitialiseNbCandidate(){
        return initialiseNbCandidate;
    }
    public int getNbCandidateTotal(int index){
        return nbCandidateTotal[index];
    }
    public void reduceNbCandidateTotal(int index){
        if(nbCandidateTotal[index]!=0){
            nbCandidateTotal[index]--;
        }
        
    }
    public void addNbCandidateTotal(int index){
        nbCandidateTotal[index]++;
        
    }
    
}

