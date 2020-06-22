/**
Map Data Manager clss
holds data on all maps available for use by the game
 */
package online.umbcraft.triumph.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

import online.umbcraft.triumph.game.Triumph;

public class MapDataManager {

	private Triumph plugin;
	private List<TriumphMapData> maps;

	public MapDataManager(Triumph plugin) {
		maps = new ArrayList<TriumphMapData>();
		this.plugin = plugin;
		readAllAvailableMaps();
	}

	public TriumphMapData getMap(String world_name) {
		for(TriumphMapData m: maps) {
			if(m.getWorldName().contentEquals(world_name))
				return m;
		}
		TriumphMapData new_map = new TriumphMapData(plugin, world_name);
		maps.add(new_map);
		return new_map;
	}
	public List<TriumphMapData> getAllMaps() {
		return maps;
	}

	// public access to recursive function below
	public List<TriumphMapData> getSuitableMaps(int num_players, int num_maps) {
		return getSuitableMaps(num_players, num_maps, 0);
	}

	// recursive function that returns all maps which come close to matching the amount of players who are ready to play
	private List<TriumphMapData> getSuitableMaps(int num_players, int num_maps, int max_off_by) {
		List<TriumphMapData> all_suited = new ArrayList<TriumphMapData>();
		if(num_maps == 0 || max_off_by >= 8)
			return all_suited;

		for(TriumphMapData next_map : maps) 
			if(!next_map.getMapName().contentEquals("lobby"))
				if(next_map.amountOffBy(num_players) == max_off_by) 
					all_suited.add(next_map);

		while(all_suited.size() > num_maps)
			all_suited.remove((int)(Math.random()*all_suited.size()));

		all_suited.addAll(getSuitableMaps(num_players, num_maps - all_suited.size(), max_off_by));
		return all_suited;
	}

	// loads all map data in from file system 
	public void readAllAvailableMaps() {
		plugin.getLog().info("[MapDataManager] initial reading of all available Triumph maps");
		File all_data = new File(plugin.getDataFolder()+"/data");
		String[] all_maps = all_data.list();

		for(String map_name: all_maps) 
			maps.add(new TriumphMapData(plugin, map_name));
		
		plugin.getLog().info("[MapDataManager] initial reading completed");
		
	}
}

