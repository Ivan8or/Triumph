package online.umbcraft.triumph.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import online.umbcraft.triumph.game.Triumph;

public class TriumphPlayerJoinListener implements Listener{
	
	Triumph plugin;

	public TriumphPlayerJoinListener(Triumph plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player new_player = e.getPlayer();
		plugin.getGameManager().addTriumphPlayer(new_player);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinWorld(PlayerSpawnLocationEvent e) {
		Location default_spawn = plugin.getGameManager().getLobby().getTeamManager().getSpawnManager().getDefaultSpawn();
		e.setSpawnLocation(default_spawn);
		
	}
	
}
