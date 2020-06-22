/**
Triumph Map Data class
holds information about a single game map
 */

package online.umbcraft.triumph.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.map.object.TriumphKitSelectorLocation;
import online.umbcraft.triumph.map.object.beacon.TriumphBeaconLocation;
import online.umbcraft.triumph.map.object.powerup.TriumphPowerupLocation;
import online.umbcraft.triumph.team.TeamColor;
import online.umbcraft.triumph.team.TriumphTeamSpawnLocation;

public class TriumphMapData {

	private Triumph plugin;

	private String map_name;
	private String map_world_name;
	private int min_size;
	private int max_size;

	private List<TriumphBeaconLocation> beacon_locations;
	private List<TriumphPowerupLocation> powerup_locations;
	private List<TriumphKitSelectorLocation> selector_locations;
	private List<TriumphTeamSpawnLocation> spawn_locations;

	private FileConfiguration map_config;
	private File map_config_file;

	// loads information about a map; if the map does not exist it is created using a template
	public TriumphMapData (Triumph plugin, String map_world_name) {
		plugin.getLog().info(" [TriumphMapData] reading in map "+map_world_name);
		this.plugin = plugin;
		this.map_world_name = map_world_name;

		map_config = new YamlConfiguration();
		map_config_file = new File(plugin.getDataFolder()+"/data/"+map_world_name, map_world_name+".yml");
		File map_world_file = new File(plugin.getDataFolder()+"/data/"+map_world_name, map_world_name);

		if(map_world_file.exists()) {
			plugin.getLog().info(" [TriumphMapData] -- map exists; interpreting config");
		}
		else {
			plugin.getLog().info(" [TriumphMapData] -- map does not exist; using default world + config");
			String default_world_file = plugin.getDataFolder()+"/default-world/default";
			File new_data = new File(plugin.getDataFolder().getAbsolutePath()+"/data/"+map_world_name);
			new_data.mkdir();

			copyDirectory(default_world_file,
					plugin.getDataFolder().getAbsolutePath()+"/data/"+map_world_name+"/"+map_world_name);
		}
		try {
			map_config_file.createNewFile();
			map_config.load(map_config_file);
		} catch (FileNotFoundException e) {				e.printStackTrace();
		} catch (IOException e) {						e.printStackTrace();
		} catch (InvalidConfigurationException e) {		e.printStackTrace();}		
		if(map_config != null)
			loadAllFromConfig();
	}

	// gets all spawn locations from config and puts them into memory, along with general data
	public void loadAllFromConfig() {
		if(map_config.getString("general-info.map-name") != null) {
			map_name = map_config.getString("general-info.map-name");
			min_size = map_config.getInt("general-info.min-team-size") * 2;
			max_size = map_config.getInt("general-info.max-team-size") * 2;
		}
		else {
			map_name = map_world_name;
			min_size = 2;
			max_size = 16;
		}
		readBeaconLocations();
		readSelectorLocations();
		readPowerupLocations();
		readSpawnLocations();
	}

	// creates a new minecraft world using this map's terrain
	public void realizeInWorld(String destination_world_name) {
		plugin.getLog().info(ChatColor.LIGHT_PURPLE+"Realizing map "+map_name+" in world "+destination_world_name);

		File source_file = new File(plugin.getDataFolder().getAbsolutePath()+"/data/"+getWorldName(),getWorldName());
		File destination_file = new File(
				"/srv/spigot",destination_world_name);

		if(destination_file.exists())
			deleteDirectory(destination_file);

		copyDirectory(source_file.getPath(), destination_file.getPath());
		World new_world = plugin.getServer().createWorld(new WorldCreator(destination_world_name));
		
		for(TriumphTeamSpawnLocation loc: spawn_locations) {
			if(loc.getColor() == TeamColor.NONE) {
				Location spawnp = new Location(new_world, loc.getX()+.5, loc.getY()+1.1, loc.getZ()+.5, (float)loc.getYaw(),0);
				new_world.setSpawnLocation(spawnp);
				break;
			}
		}
	}

	// helper function which copies a directory into a new location
	public static void copyDirectory(String source, String destination) {
		try {
			Files.walkFileTree(Paths.get(source),
					new FileCopier(Paths.get(source),
							Paths.get(destination)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// recursive helper function to delete old world file
	public static void deleteDirectory(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDirectory(f);
			}
		}
		file.delete();
	}

	// returns the absolute difference between the amount of players available
	// and the amount of players needed to use this map
	public int amountOffBy(int num_players) {
		if(min_size > num_players)
			return min_size - num_players;
		if(max_size < num_players)
			return num_players - max_size;
		return 0;
	}

	// returns a list of each color this map supports 
	public List<TeamColor> getColors() {
		List<TeamColor> toReturn = new ArrayList<TeamColor>();
		for(TriumphTeamSpawnLocation t: spawn_locations) 
			if(!toReturn.contains(t.getColor()))
				toReturn.add(t.getColor());

		return toReturn;
	}

	// returns the name of the map
	public String getMapName() {
		return map_name;
	}
	// returns the name of the world file containing this map
	public String getWorldName() {
		return map_world_name;
	}
	public List<TriumphBeaconLocation> getBeaconLocations() {
		return beacon_locations;
	}
	public List<TriumphPowerupLocation> getPowerupLocations() {
		return powerup_locations;
	}
	public List<TriumphKitSelectorLocation> getSelectorLocations() {
		return selector_locations;
	}
	public List<TriumphTeamSpawnLocation> getSpawnLocations() {
		return spawn_locations;
	}


	public void addBeaconLocation(TriumphBeaconLocation loc) {
		beacon_locations.add(loc);
	}
	public void addPowerupLocation(TriumphPowerupLocation loc) {
		powerup_locations.add(loc);
	}
	public void addSelectorLocation(TriumphKitSelectorLocation loc) {
		selector_locations.add(loc);
	}
	public void addSpawnLocation(TriumphTeamSpawnLocation loc) {
		spawn_locations.add(loc);
	}

	public void removeBeaconLocation(TriumphBeaconLocation loc) {
		beacon_locations.remove(loc);
	}
	public void removePowerupLocation(TriumphPowerupLocation loc) {
		powerup_locations.remove(loc);
	}
	public void removeSelectorLocation(TriumphKitSelectorLocation loc) {
		selector_locations.remove(loc);
	}
	public void removeSpawnLocation(TriumphTeamSpawnLocation loc) {
		spawn_locations.remove(loc);
	}
	public void writeWorldFile(World world) {
		plugin.getLog().info(ChatColor.RED+"Writing world "+world.getName()+" to file namede "+map_world_name);
		
		File source_file = new File(
				"/srv/spigot",world.getName());
		File dest_file = new File(plugin.getDataFolder().getAbsolutePath()+"/data/"+map_world_name,map_world_name);
		world.setAutoSave(false);
		world.save();
		if(dest_file.exists())
			deleteDirectory(dest_file);

		copyDirectory(source_file.getPath(), dest_file.getPath());
		
		File uid_dat_file = new File(dest_file,"uid.dat");
		uid_dat_file.delete();
		
		File playerdata_file = new File(dest_file, "playerdata");
		for(String f: playerdata_file.list()) {
			new File(f).delete();
		}
		
		plugin.getLog().info(ChatColor.RED+"Writing finished");
	}

	public void writeAllDataChanges() { 
		writeSpawnLocations();
		writeBeaconLocations();
		writeSelectorLocations();
		writePowerupLocations();
	}
	// puts team spawns information into map config
	public void writeSpawnLocations() {
		map_config.set("teams", null);
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}

		for(TriumphTeamSpawnLocation next_spawn: spawn_locations) {
			double x_loc = next_spawn.getX();
			double y_loc = next_spawn.getY();
			double z_loc = next_spawn.getZ();
			double yaw = next_spawn.getYaw();
			String team_color = next_spawn.getColor().name();

			String spawn_uuid = ((int)x_loc)+","+((int)y_loc)+","+((int)z_loc);

			map_config.set("teams."+spawn_uuid+".x",x_loc);
			map_config.set("teams."+spawn_uuid+".y",y_loc);
			map_config.set("teams."+spawn_uuid+".z",z_loc);
			map_config.set("teams."+spawn_uuid+".yaw",yaw);
			map_config.set("teams."+spawn_uuid+".color",team_color);
		}
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}
	}

	// gets team spawns information from map config loaded into memory
	private void readSpawnLocations() {
		spawn_locations = new ArrayList<TriumphTeamSpawnLocation>();
		System.out.println("reading spawn locations for map "+map_name);
		ConfigurationSection spawns_config = map_config.getConfigurationSection("teams");
		if(spawns_config == null)
			return;

		for(String next_spawn_id: spawns_config.getKeys(false)) {
			System.out.println("reading spawn "+next_spawn_id);
			ConfigurationSection next_spawn_config = spawns_config.getConfigurationSection(next_spawn_id);
			double x_loc = next_spawn_config.getDouble("x");
			double y_loc = next_spawn_config.getDouble("y");
			double z_loc = next_spawn_config.getDouble("z");
			double spawn_yaw = next_spawn_config.getDouble("yaw");
			String spawn_color = next_spawn_config.getString("color");
			System.out.println("color is "+spawn_color);
			TriumphTeamSpawnLocation spawn_data = new TriumphTeamSpawnLocation(spawn_color, x_loc, y_loc, z_loc, spawn_yaw);
			spawn_locations.add(spawn_data);
		}
	}

	// puts beacon spawns information into map config
	public void writeBeaconLocations() {
		map_config.set("beacons", null);
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}

		for(TriumphBeaconLocation next_spawn: beacon_locations) {

			double x_loc = next_spawn.getX();
			double y_loc = next_spawn.getY();
			double z_loc = next_spawn.getZ();

			String beacon_uuid = ((int)x_loc)+","+((int)y_loc)+","+((int)z_loc);

			map_config.set("beacons."+beacon_uuid+".x",x_loc);
			map_config.set("beacons."+beacon_uuid+".y",y_loc);
			map_config.set("beacons."+beacon_uuid+".z",z_loc);
		}
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}
	}

	// gets beacon spawns information from map config loaded into memory
	private void readBeaconLocations() {
		beacon_locations = new ArrayList<TriumphBeaconLocation>();

		ConfigurationSection beacons_config = map_config.getConfigurationSection("beacons");
		if(beacons_config == null)
			return;

		for(String next_beacon_id: beacons_config.getKeys(false)) {
			ConfigurationSection next_beacon_config = beacons_config.getConfigurationSection(next_beacon_id);
			double x_loc = next_beacon_config.getDouble("x");
			double y_loc = next_beacon_config.getDouble("y");
			double z_loc = next_beacon_config.getDouble("z");
			String beacon_name = next_beacon_config.getString("name");

			TriumphBeaconLocation beacon_data = new TriumphBeaconLocation(beacon_name, x_loc, y_loc, z_loc);
			beacon_locations.add(beacon_data);
		}
	}

	// puts selector spawns information into map config
	public void writeSelectorLocations() {
		map_config.set("selectors", null);
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}

		for(TriumphKitSelectorLocation next_spawn: selector_locations) {

			double x_loc = next_spawn.getX();
			double y_loc = next_spawn.getY();
			double z_loc = next_spawn.getZ();
			double yaw = next_spawn.getYaw();
			String kit_class = next_spawn.getKitType().name();

			String selector_uuid = ((int)x_loc)+","+((int)y_loc)+","+((int)z_loc);

			map_config.set("selectors."+selector_uuid+".x",x_loc);
			map_config.set("selectors."+selector_uuid+".y",y_loc);
			map_config.set("selectors."+selector_uuid+".z",z_loc);
			map_config.set("selectors."+selector_uuid+".yaw",yaw);
			map_config.set("selectors."+selector_uuid+".class",kit_class);
		}
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}
	}

	// gets kit selectors spawns information from map config loaded into memory
	private void readSelectorLocations() {
		selector_locations = new ArrayList<TriumphKitSelectorLocation>();

		ConfigurationSection selectors_config = map_config.getConfigurationSection("selectors");
		if(selectors_config == null)
			return;

		for(String next_selector_id: selectors_config.getKeys(false)) {
			ConfigurationSection next_selector_config = selectors_config.getConfigurationSection(next_selector_id);
			double x_loc = next_selector_config.getDouble("x");
			double y_loc = next_selector_config.getDouble("y");
			double z_loc = next_selector_config.getDouble("z");
			double yaw_loc = next_selector_config.getDouble("yaw");
			String selector_class = next_selector_config.getString("class");

			TriumphKitSelectorLocation selector_data = new TriumphKitSelectorLocation(selector_class, x_loc, y_loc, z_loc, yaw_loc);
			selector_locations.add(selector_data);
		}
	}

	// puts powerup spawns information into map config
	public void writePowerupLocations() {
		map_config.set("powerups", null);
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}

		for(TriumphPowerupLocation next_spawn: powerup_locations) {

			double x_loc = next_spawn.getX();
			double y_loc = next_spawn.getY();
			double z_loc = next_spawn.getZ();
			String powerup_type = next_spawn.getType().name();

			String powerup_uuid = ((int)x_loc)+","+((int)y_loc)+","+((int)z_loc);

			map_config.set("powerups."+powerup_uuid+".x",x_loc);
			map_config.set("powerups."+powerup_uuid+".y",y_loc);
			map_config.set("powerups."+powerup_uuid+".z",z_loc);
			map_config.set("powerups."+powerup_uuid+".type",powerup_type);
		}
		try {
			map_config.save(map_config_file);
		} catch (IOException e) {	e.printStackTrace();	}
	}

	// gets powerup spawns information from map config loaded into memory
	private void readPowerupLocations() {
		powerup_locations = new ArrayList<TriumphPowerupLocation>();

		ConfigurationSection powerups_config = map_config.getConfigurationSection("powerups");
		if(powerups_config == null)
			return;

		for(String next_powerup_id: powerups_config.getKeys(false)) {
			ConfigurationSection next_powerup_config = powerups_config.getConfigurationSection(next_powerup_id);
			double x_loc = next_powerup_config.getDouble("x");
			double y_loc = next_powerup_config.getDouble("y");
			double z_loc = next_powerup_config.getDouble("z");
			String powerup_type = next_powerup_config.getString("type");

			TriumphPowerupLocation powerup_data = new TriumphPowerupLocation(powerup_type, x_loc, y_loc, z_loc);
			powerup_locations.add(powerup_data);
		}

	}

}
