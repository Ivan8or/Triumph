/**
Triumph Kit Selector Spawn class
holds information about a single kit selector spawn location in a map
 */

package online.umbcraft.triumph.map.object;

import online.umbcraft.triumph.kit.KitClass;

public class TriumphKitSelectorLocation {
	
	private KitClass type;

	private double x_coord;
	private double y_coord;
	private double z_coord;
	private double yaw_val;

	public TriumphKitSelectorLocation(String type, double x, double y, double z, double yaw) {
		this.type = KitClass.valueOf(type);
		x_coord = x;
		y_coord = y;
		z_coord = z;
		yaw_val = yaw;
	}
	public boolean equals(TriumphKitSelectorLocation other) {
		return( (int)x_coord == (int)(other.x_coord) && 
				(int)y_coord == (int)(other.y_coord) &&
				(int)z_coord == (int)(other.z_coord));
	}
	
	// getters
	public KitClass getKitType() {
		return type;
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
	public double getYaw() {
		return yaw_val;
	}
}
