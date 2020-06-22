package online.umbcraft.triumph.team;

import java.util.ArrayList;
import java.util.List;

import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.game.TriumphInstance;
import online.umbcraft.triumph.player.TriumphPlayer;

public class TeamManager {

	private Triumph plugin;
	private TriumphInstance instance;
	
	private List<TriumphTeam> teams;
	private TriumphTeamSpawnManager spawnManager;
	
	public TeamManager(Triumph plugin, TriumphInstance instance, List<TriumphTeamSpawnLocation> spawn_locations) {
		this.plugin = plugin;
		this.instance = instance;
		
		teams = new ArrayList<TriumphTeam>();
		teams.add(new TriumphTeam(plugin, this, TeamColor.NONE));
		
		spawnManager = new TriumphTeamSpawnManager(this);
		
		for(TriumphTeamSpawnLocation new_loc: spawn_locations) {
			spawnManager.addTeamSpawn(new_loc, instance.getWorld());
			if(getByColor(new_loc.getColor()) == null)
				teams.add(new TriumphTeam(plugin, this, new_loc.getColor()));
		}
	}
	public List<TriumphPlayer> getAllPlayers() {
		List<TriumphPlayer> to_return = new ArrayList<TriumphPlayer>();
		for(TriumphTeam t: teams)
			to_return.addAll(t.getPlayers());
		
		return to_return;
	}
	public TriumphTeamSpawnManager getSpawnManager() {
		return spawnManager;
	}
	public TriumphInstance getInstance() {
		return instance;
	}
	public TriumphTeam getByColor(TeamColor color) {
		for(TriumphTeam t: teams) {
			if(t.getColor() == color)
				return t;
		}
		return null;
	}
}
