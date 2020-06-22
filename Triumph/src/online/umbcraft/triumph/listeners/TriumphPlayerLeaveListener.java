package online.umbcraft.triumph.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.player.TriumphPlayer;
import online.umbcraft.triumph.team.TeamColor;

public class TriumphPlayerLeaveListener implements Listener{

	Triumph plugin;

	public TriumphPlayerLeaveListener(Triumph plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent e) {
		System.out.println("PlayerQuitEvent - player left");
		e.setQuitMessage(null);
		TriumphPlayer p = plugin.getGameManager().getTriumphPlayer(e.getPlayer());
		if(p.getTeam() == TeamColor.NONE) {
			p.getLobby().removePlayer(p);
			plugin.getGameManager().getAllOnline().remove(p.getPlayer().getUniqueId()+"");
		}
		
	}
}
