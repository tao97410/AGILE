package h4131.calculus;

import java.util.Arrays;



/**
 * Does not implement Iterator because some method need a complete redesign
 */
public class SeqIter {
	private Integer[][] candidates;
	private int[] nbCandidates;
	private TemplateGraph g;
	private int currentWindow;
	private Unvisited unvisited;


	/**
	 * Create an customize iterator to traverse the set of vertices in <code>candidates</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>candidates</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */

	public SeqIter(Unvisited unvisited, TemplateGraph g,Integer currentVertex){
		this.unvisited=unvisited;
		this.candidates =unvisited.getCandidates();
		this.nbCandidates=Arrays.copyOf(unvisited.getInitialiseNbCandidate(),5);
		this.g=g;
		this.currentWindow=g.getWindow(currentVertex).ordinal()-1;
		if(currentWindow==-1){
			currentWindow=0;
		}
		
	}
	
	/**
	 * This method has been conceived especially for the 2D tab candidates
	 * @return true if there is a next element and false otherwise
	 */
	public boolean hasNext() {
		int currentWindowMem=this.currentWindow;
		while(followingCandidate()==-1){
			if(isEmpty()){
				if(currentWindow==g.getSizeNbTimeWindow()){
					return false;
				}
				currentWindow++;
			}
			else{
				this.currentWindow=currentWindowMem;
				return false;
			}		
		}
		this.currentWindow=currentWindowMem;
		return true;
	}

	/**
	 *This method has been conceived especially for the 2D tab candidates
	 *@return true if the currentWindow's column of candidates is empty(full of -1), false otherwise
	 */
	public boolean isEmpty(){
		
		return unvisited.getNbCandidateTotal(currentWindow)==0;
	}

	/**
	 * This method has been conceived especially for the 2D tab candidates
	 * @return -1 if the next candidate doesn't exist and the candidate otherwise 
	 */
	private int followingCandidate(){
		for(int i=nbCandidates[currentWindow]-1;i>=0;i--){
			if(candidates[currentWindow][i]!=-1){
				return candidates[currentWindow][i];
			}
		}
		return -1;
	}
	/**
  	 * This method has been conceived especially for the 2D tab candidates
	 * @return -1 if the next candidate doesn't exist and the candidate otherwise and place
	 * the iterator on the next candidate if there is one
	 */
	private int putFollowingCandidate(){
		int memNbCandidate=nbCandidates[currentWindow];
		Integer folowingCandidate=-1;
		for(int i=nbCandidates[currentWindow]-1;i>=0;i--){
			nbCandidates[currentWindow]-=1;
			if(candidates[currentWindow][i]!=-1){
				folowingCandidate= candidates[currentWindow][i];
				break;
			}
		}
		if(folowingCandidate==-1){
			nbCandidates[currentWindow]=memNbCandidate;
		}
		return folowingCandidate;
	}

	/**
	 * This method has been conceived especially for the 2D tab candidates
	 * @return the followingCandidate in candidates 
	 */
	public Integer next() {

		Integer folowingCandidate;
		while((folowingCandidate=putFollowingCandidate())==-1){
				currentWindow++;

		
			
		}
		return folowingCandidate;
	}
	
	/**
	 * Delete an element in candidates
	 * @param deliveryPoint to delete
	 * @return the deleted element
	 */
	public Integer remove(){
		// int window=g.getWindow(deliveryPoint).ordinal()-1;
		Integer elemDelete=candidates[currentWindow][nbCandidates[currentWindow]];
		candidates[currentWindow][nbCandidates[currentWindow]]=-1;
		unvisited.reduceNbCandidateTotal(currentWindow);
		return elemDelete;
	}

	/**
	 * Add an element to the right spot in candidates
	 * @param deliveryPoint to add
	 */
	public void addFollowing(Integer deliveryPoint){

		int window=g.getWindow(deliveryPoint).ordinal()-1;
		unvisited.addNbCandidateTotal(window);
		candidates[window][nbCandidates[window]]=deliveryPoint;
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


	public int getCurrentWindow(){
		return this.currentWindow;
	}

	public Integer[][] getCandidates(){
		return this.candidates;
	}

	public int[] getNbCandidates(){
		return this.nbCandidates;
	}

}
