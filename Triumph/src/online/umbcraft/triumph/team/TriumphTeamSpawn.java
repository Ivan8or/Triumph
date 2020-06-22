package online.umbcraft.triumph.team;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.World;

import online.umbcraft.triumph.player.TriumphPlayer;

public class TriumphTeamSpawn {

	private TriumphTeamSpawnLocation loc;
	private World world;
	private Collection<TriumphPlayer> players;
	private TeamColor color;
	
	public TriumphTeamSpawn(TriumphTeamSpawnLocation loc, World world) {
		this.loc = loc;
		this.color = loc.getColor();
		this.world = world;
		players = new ArrayList<TriumphPlayer>();
	}
	public TeamColor getColor() {
		return color;
	}
	public boolean hasPlayer(TriumphPlayer p) {
		return players.contains(p);
	}
	public Location getLoc() {
		return new Location(world, loc.getX()+.5, loc.getY()+1.1, loc.getZ()+.5, (float)(loc.getYaw()+180), 0f);
	}
	public int numPlayers() {
		return players.size();
	}
	public void addPlayer(TriumphPlayer p) {
		players.add(p);
	}
	public void removePlayer(TriumphPlayer p) {
		players.remove(p);
	}
}
