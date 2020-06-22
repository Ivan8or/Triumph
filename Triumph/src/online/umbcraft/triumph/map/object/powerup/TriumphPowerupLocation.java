/**
Triumph Powerup Spawn class
holds information about a single powerup spawn location in a map
 */

package online.umbcraft.triumph.map.object.powerup;

import online.umbcraft.triumph.team.TriumphTeamSpawnLocation;

public class TriumphPowerupLocation {

	private PowerupType type;

	private double x_coord;
	private double y_coord;
	private double z_coord;


	public TriumphPowerupLocation(String type, double x, double y, double z) {
		this.type = PowerupType.valueOf(type);
		x_coord = x;
		y_coord = y;
		z_coord = z;
	}
	public boolean equals(TriumphPowerupLocation other) {
		return( (int)x_coord == (int)(other.x_coord) && 
				(int)y_coord == (int)(other.y_coord) &&
				(int)z_coord == (int)(other.z_coord));
	}
	// getters
	public PowerupType getType() {
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
	
}
