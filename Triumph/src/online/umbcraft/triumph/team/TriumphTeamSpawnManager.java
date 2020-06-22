package online.umbcraft.triumph.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;

import online.umbcraft.triumph.player.TriumphPlayer;

public class TriumphTeamSpawnManager {

	private TeamManager teamManager;
	List<TriumphTeamSpawn> spawns;

	public TriumphTeamSpawnManager(TeamManager manager) {
		spawns = new ArrayList<TriumphTeamSpawn>();
		this.teamManager = manager;
	}
	public void addTeamSpawn(TriumphTeamSpawnLocation loc, World world) {
		spawns.add(new TriumphTeamSpawn(loc, world));
	}
	public void addPlayerToSpawnList(TriumphPlayer p) {
		TriumphTeamSpawn lowest = null;
		int amount = Integer.MAX_VALUE;
		for(TriumphTeamSpawn s: spawns) {
			if(amount > s.numPlayers() && s.getColor() == p.getTeam()) {
				lowest = s;
				amount = s.numPlayers();
			}
		}
		lowest.addPlayer(p);
	}
	public void removePlayerFromSpawnList(TriumphPlayer p) {
		for(TriumphTeamSpawn s: spawns)
			if(s.hasPlayer(p)) {
				s.removePlayer(p);
				return;
			}
	}
	
	public Location getDefaultSpawn() {
		for(TriumphTeamSpawn s: spawns) {
			if(s.getColor() == TeamColor.NONE) {
				return s.getLoc();
			}
		}
		return new Location(teamManager.getInstance().getWorld(), 0, 100, 0);
	}
	public void respawnPlayer(TriumphPlayer p) {
		System.out.println("respawning player "+p.getPlayer().getName());
		for(TriumphTeamSpawn s: spawns) {
			if(s.hasPlayer(p) || (s.getColor() == TeamColor.NONE && p.getTeam() == TeamColor.NONE)) {
				s.getLoc().getChunk().load();
				p.getPlayer().teleport(s.getLoc());
				return;
			}
		}
		
		if(p.getTeam() != null && p.getTeam() != TeamColor.NONE) {
			p.setTeam(TeamColor.NONE);
			respawnPlayer(p);
		}
		else 
			p.getPlayer().teleport(new Location(
					teamManager.getInstance().getWorld(),0,100,0));
	}
}
