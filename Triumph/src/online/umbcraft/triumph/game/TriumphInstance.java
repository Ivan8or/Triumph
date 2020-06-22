package online.umbcraft.triumph.game;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
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
		world.setTime(4000);
		world.setGameRule(GameRule.KEEP_INVENTORY, true);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.setGameRule(GameRule.DO_MOB_LOOT, false);
		world.setGameRule(GameRule.DO_TILE_DROPS, false);
		world.setGameRule(GameRule.KEEP_INVENTORY, true);
		world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		world.setGameRule(GameRule.DO_INSOMNIA, false);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		world.setGameRule(GameRule.DISABLE_RAIDS, true);
		world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
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
	public int getNumConnected() {
		int to_return = 0;
		for(TriumphPlayer t: teamManager.getAllPlayers()) 
			if(t.getPlayer().isOnline())
				to_return++;
		return to_return;

	}
	public void removePlayer(TriumphPlayer p) {
		teamManager.getByColor(p.getTeam()).removePlayer(p);
	}

	public void send(TriumphPlayer p, TeamColor c) {
		p.getPlayer().setGameMode(GameMode.ADVENTURE);
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
			p.setDead(true);
		else
			p.setDead(false);

		for(TriumphPlayer dead_pl: teamManager.getAllPlayers())
			if(dead_pl.isDead())
				p.getPlayer().hidePlayer(plugin, dead_pl.getPlayer());
			else
				p.getPlayer().showPlayer(plugin, dead_pl.getPlayer());

		p.respawn();
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
