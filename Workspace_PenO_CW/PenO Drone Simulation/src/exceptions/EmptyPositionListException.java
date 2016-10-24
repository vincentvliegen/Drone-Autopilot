package exceptions;

import java.util.ArrayList;

public class EmptyPositionListException extends Exception {
	
	public EmptyPositionListException(ArrayList<int[]> list){
		this.list = list;
	}

	public ArrayList<int[]> getList(){
		return this.list;
	}
	

	private final ArrayList<int[]> list;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
