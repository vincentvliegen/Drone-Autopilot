package exceptions;

public class FirstOrbNotVisibleException extends Exception{

	public FirstOrbNotVisibleException(int colorFirstOrb){
		this.colorFirstOrb = colorFirstOrb;
	}
	
	private final int colorFirstOrb;
	
	/**
	 * @return the colorFirstOrb
	 */
	public int getColorFirstOrb() {
		return colorFirstOrb;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
