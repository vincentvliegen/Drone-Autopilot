package DroneAutopilot.graphicalrepresentation;

import java.util.ArrayList;

public class WorldAPData {

	/*
	 * Idee: dit als klasse voor openGL (opsplitsing tussen GL-gedeelte en
	 * data-gedeelte bevat gescande objecten (en evt. een kleine sphere als
	 * drone-object om aan te geven waar de drone zich bevindt kunnen dan ahv
	 * missie een camera selecteren (het te scannen object weergeven, evt. al
	 * roterend, of algemeen 2D/3D beeld van wat reeds gescand is)
	 * 
	 */

	/*
	 * De wereld bevat: lijst met polyhedrons evt een drone functies voor het
	 * setten van camera een GL-wereld met init etc. ......
	 */
	
	
	private ArrayList<PolyhedronAPData> polyhedrons;
	
	
	public ArrayList<PolyhedronAPData> getPolyhedrons() {
		return polyhedrons;
	}
	
	public void addPolyhedron(PolyhedronAPData poly) {
		this.polyhedrons.add(poly);
	}
	
	
	
	
	
	

}
