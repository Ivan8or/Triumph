/**
GameManager class

organizes and starts game instances, creates 
a smooth transition between games for the player

 */

package online.umbcraft.triumph.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import online.umbcraft.triumph.map.MapDataManager;
import online.umbcraft.triumph.map.TriumphMapData;
import online.umbcraft.triumph.game.TriumphGame;
import online.umbcraft.triumph.player.TriumphPlayer;
import online.umbcraft.triumph.team.TeamColor;

public class GameManager {
	
	private Map<String, TriumphPlayer> all_online;
	
	private Triumph plugin;
	
	private MapDataManager mapManager;
	private List<TriumphGame> games;
	private List<TriumphInstance> other_instances;
	private TriumphLobby lobby;
	
	public GameManager(Triumph plugin) {
		this.plugin = plugin;
		all_online = new HashMap<String, TriumphPlayer>();
		mapManager = new MapDataManager(plugin);  
		games = new ArrayList<TriumphGame>();
		other_instances = new ArrayList<TriumphInstance>();
		lobby = new TriumphLobby(plugin, "lobby", mapManager.getMap("lobby"));
	}
	public TriumphLobby getLobby() {
		return lobby;
	}
	public MapDataManager getMapmanager() {
		return mapManager;
	}
	public TriumphInstance getInstance(String world_name) {
		for(TriumphGame g: games) 
			if(g.getWorld().getName().contentEquals(world_name))
				return g;
		for(TriumphInstance i: other_instances) 
			if(i.getWorld().getName().contentEquals(world_name))
				return i;
		if(lobby.getWorld().getName().contentEquals(world_name))
			return lobby;
		
		return null;
	}
	public void addInstance(TriumphInstance inst) {
		if(inst instanceof TriumphGame)
			games.add((TriumphGame)inst);
		else
			other_instances.add(inst);
	}
	public void removeInstance(TriumphInstance inst) {
		if(inst instanceof TriumphGame)
			games.remove((TriumphGame)inst);
		else
			other_instances.remove(inst);
	}
	public Triumph getPlugin() {
		return plugin;
	}
	public TriumphPlayer getTriumphPlayer(Player p) {
		return all_online.get(p.getUniqueId()+"");
	}
	public void addTriumphPlayer(Player p) {
		p.getInventory().clear();
		if(!all_online.containsKey(p.getUniqueId()+"")) {
			TriumphPlayer new_player = new TriumphPlayer(p);
			all_online.put(p.getUniqueId()+"", new_player); 
		}
		else {
			all_online.get(p.getUniqueId()+"").setPlayer(p);
		}
		TriumphPlayer existing_player = all_online.get(p.getUniqueId()+"");
		lobby.send(existing_player, TeamColor.NONE);
		
	}
	public List<TriumphInstance> getAllWorlds() {
		List<TriumphInstance> to_return = new ArrayList<TriumphInstance>();
		to_return.add(lobby);
		to_return.addAll(other_instances);
		to_return.addAll(games);
		return to_return;
	}
	public synchronized String getFreeWorldName(String prefix) {
		int num_on = 1;
		while(plugin.getServer().getWorld(prefix+num_on) != null) {
			num_on++;
		}
		return prefix + num_on;
	}
	public Map<String, TriumphPlayer> getAllOnline() {
		return all_online;
	}
	
	public void unloadAllWorlds() {
		for(TriumphGame g: games)
			g.unload();
		for(TriumphInstance i: other_instances)
			i.unload();
		
		lobby.unload();
	}
}
