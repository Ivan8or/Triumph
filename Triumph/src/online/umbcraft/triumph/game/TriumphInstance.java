package online.umbcraft.triumph.game;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import online.umbcraft.triumph.map.TriumphMapData;
import online.umbcraft.triumph.player.TriumphPlayer;
import online.umbcraft.triumph.team.TeamColor;
import online.umbcraft.triumph.team.TeamManager;
import online.umbcraft.triumph.team.TriumphTeam;

public class TriumphInstance {

	private Triumph plugin;

	private TeamManager teamManager;
	private TriumphMapData map;
	private World world;

	public TriumphInstance(Triumph plugin, String world_name, TriumphMapData map) {
		this.plugin = plugin;
		this.map = map;

		map.realizeInWorld(world_name);
		world = plugin.getServer().getWorld(world_name);
		world.setAutoSave(false);

		teamManager = new TeamManager(plugin, this, map.getSpawnLocations());
	}	
	public TeamManager getTeamManager() {
		return teamManager;
	}
	public Triumph getPlugin() {
		return plugin;
	}
	public World getWorld() {
		return world;
	}
	public TriumphMapData getMapData() {
		return map;
	}
	public void send(TriumphPlayer p, TeamColor c) {
		TriumphTeam to_add_to = teamManager.getByColor(c);
		if(to_add_to != null) {
			to_add_to.addPlayer(p);
			p.setTeam(c);
		}
		else {
			teamManager.getByColor(TeamColor.NONE).addPlayer(p);
			p.setTeam(TeamColor.NONE);
		}
		
		p.setLobby(this);
		if(p.getTeam() == TeamColor.NONE && !(this instanceof TriumphGame) && this != plugin.getGameManager().getLobby())
			p.getPlayer().setGameMode(GameMode.SPECTATOR);
		else
			p.getPlayer().setGameMode(GameMode.ADVENTURE);
		
		
		teamManager.getSpawnManager().respawnPlayer(p);
		

	}
	public void unload() {
		plugin.getGameManager().removeInstance(this);
		for(Player p: world.getPlayers()) {
			TriumphPlayer t_p = plugin.getGameManager().getTriumphPlayer(p);
			plugin.getGameManager().getLobby().send(t_p, TeamColor.NONE);
		}
		plugin.getServer().unloadWorld(world.getName(), false);
		TriumphMapData.deleteDirectory(new File("/srv/spigot/"+world.getName()));
	}

}
