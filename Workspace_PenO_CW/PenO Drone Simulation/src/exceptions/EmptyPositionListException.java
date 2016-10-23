package exceptions;

public class EmptyPositionListException extends Exception {
	
	public EmptyPositionListException(int[][] list){
		this.list = list;
	}

	public int[][] getList(){
		return this.list;
	}
	

	private final int[][] list;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
