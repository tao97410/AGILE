package h4131.calculus;

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

public class SeqIter {
	private Integer[][] candidates;
	private int[] nbCandidates;
	private int[] initialiseNbCandidate;
	private TemplateGraph g;
	private int currentWindow;
	private Unvisited unvisited;


	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	public SeqIter(Unvisited unvisited, TemplateGraph g,Integer currentVertex){
		this.unvisited=unvisited;
		this.candidates =unvisited.getCandidates();
		this.nbCandidates=Arrays.copyOf(unvisited.initiliseNbCandidate(),5);
		this.g=g;
		this.currentWindow=g.getWindow(currentVertex).ordinal()-1;
		if(currentWindow==-1){
			currentWindow=0;
		}
		
	}
	
	
	public boolean hasNext() {
		if(followingCandidate()==-1){
			if(isEmpty()){
				return !(this.currentWindow+1==g.getSizeNbTimeWindow());
			}
			else{
				return false;
			}
			
				
		}
		return true;

	

		
	}
	public boolean hasNextForBound() {
		if(followingCandidate()==-1){
				return !(this.currentWindow+1==g.getSizeNbTimeWindow());
		}
			
		return true;

	

		
	}
	
	public boolean isEmpty(){
		for(Integer i:candidates[currentWindow]){
			if(i!=null && i!=-1){
				return false;
			}
		}
		return true;
	}
	public int followingCandidate(){
		for(int i=nbCandidates[currentWindow]-1;i>=0;i--){
			if(candidates[currentWindow][i]!=-1){
				return candidates[currentWindow][i];
			}
		}
		return -1;
	}
	
	public int putFollowingCandidate(){
		for(int i=nbCandidates[currentWindow]-1;i>=0;i--){
			nbCandidates[currentWindow]-=1;
			if(candidates[currentWindow][i]!=-1){
				return candidates[currentWindow][i];
			}
		}
		return -1;
	}

	public Integer next() {
		if(followingCandidate()==-1){
			while(followingCandidate()==-1){
				currentWindow++;

			}
			
		}
		
		return putFollowingCandidate();
	}
	public Integer remove(Integer deliveryPoint){
		int window=g.getWindow(deliveryPoint).ordinal()-1;
		Integer elemDelete=candidates[window][nbCandidates[window]];
		candidates[window][nbCandidates[window]]=-1;
		return elemDelete;
	}
	public void addFollowing(Integer deliveryPoint){
		int window=g.getWindow(deliveryPoint).ordinal()-1;
		candidates[window][nbCandidates[window]]=deliveryPoint;
	}
	public  boolean isFinish(){
		this.nbCandidates=Arrays.copyOf(unvisited.initiliseNbCandidate(),5);
		return hasNext();
	}
	public String toString(){
		String string="";
		for(Integer[] i:candidates){
			string+="////";
			for(Integer j:i){
				string+="/"+j;
			}
		}
		return string;
	}


}
