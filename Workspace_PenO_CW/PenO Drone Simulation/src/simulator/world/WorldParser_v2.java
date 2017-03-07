package simulator.world;

import simulator.parser.Parser_v2;

@SuppressWarnings("serial")
public class WorldParser_v2 extends WorldParser{
	
	public WorldParser_v2() {
		super();
		this.setParser(new Parser_v2(this));
	}

}
