package simulator.objects;

import simulator.world.World;

public abstract class WorldObject {
	public WorldObject(World world) {
		this.world = world;
	}
	
	private World world;
	
	public abstract void draw();
	public abstract double[] getPosition();
	public abstract float getRadius();
	
	public World getWorld() {
		return world;
	}

}
