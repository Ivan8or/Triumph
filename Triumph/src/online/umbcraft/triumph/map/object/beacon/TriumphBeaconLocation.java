/**
Triumph Beacon Spawn class
holds information about a single beacon spawn location in a map
 */

package online.umbcraft.triumph.map.object.beacon;


public class TriumphBeaconLocation {

	private String name;
	
	private double x_coord;
	private double y_coord;
	private double z_coord;
	
	
	public TriumphBeaconLocation(String name, double x, double y, double z) {
		this.name = name;
		x_coord = x;
		y_coord = y;
		z_coord = z;
	}
	
	public boolean equals(TriumphBeaconLocation other) {
		return( (int)x_coord == (int)(other.x_coord) && 
				(int)y_coord == (int)(other.y_coord) &&
				(int)z_coord == (int)(other.z_coord));
	}
	// getters
	public String getName() {
		return name;
	}
	public double getX() {
		return x_coord;
	}
	public double getY() {
		return y_coord;
	}
	public double getZ() {
		return z_coord;
	}
}
