package exceptions;

public class SmallCircleException extends Exception {

	public SmallCircleException(int size){
		this.setSizeListCircPos(size);
	}
	
	/**
	 * @return the sizeListCircPos
	 */
	public int getSizeListCircPos() {
		return sizeListCircPos;
	}

	/**
	 * @param sizeListCircPos the sizeListCircPos to set
	 */
	public void setSizeListCircPos(int sizeListCircPos) {
		this.sizeListCircPos = sizeListCircPos;
	}
	private int sizeListCircPos;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6132733826891303523L;

}
