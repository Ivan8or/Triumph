/**
Triumph Player Spawn class
holds information about a single team spawn location in a map
 */

package online.umbcraft.triumph.team;

public class TriumphTeamSpawnLocation {

	private final TeamColor color; 

	private final double x_coord;
	private final double y_coord;
	private final double z_coord;
	private final double yaw_val;

	public TriumphTeamSpawnLocation(String color, double x, double y, double z, double yaw) {
		System.out.println("creating spawn location with color of "+color);
		this.color = TeamColor.valueOf(color);
		if(color != null)
			System.out.println("value of teamColor is now "+this.color.name());
		else
			System.out.println("value of teamColor is now null");
		x_coord = x;
		y_coord = y;
		z_coord = z;
		yaw_val = yaw;
	}

	public boolean equals(TriumphTeamSpawnLocation other) {
		return( (int)x_coord == (int)(other.x_coord) && 
				(int)y_coord == (int)(other.y_coord) &&
				(int)z_coord == (int)(other.z_coord));
	}

	// getters
	public TeamColor getColor() {
		return color;
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
