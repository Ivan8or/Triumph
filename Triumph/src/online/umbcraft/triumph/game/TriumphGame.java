package online.umbcraft.triumph.game;

import org.bukkit.World;

import online.umbcraft.triumph.map.TriumphMapData;
import online.umbcraft.triumph.map.object.beacon.BeaconManager;

public class TriumphGame extends TriumphInstance{
	
	private BeaconManager beaconManager;

	public TriumphGame(Triumph plugin, String world_name, TriumphMapData map) {
		super(plugin, world_name, map);
 	}	
	public void startGame() { 
		
	}
}
