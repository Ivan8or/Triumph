package online.umbcraft.triumph.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitScheduler;

import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.game.TriumphGame;
import online.umbcraft.triumph.player.TriumphPlayer;

public class TriumphPlayerDeathListener implements Listener{

	private Triumph plugin;

	private Map<String, Integer> dead_players;

	public TriumphPlayerDeathListener(Triumph plugin) {
		this.plugin = plugin;
		dead_players = new HashMap<String, Integer>();
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = (Player)e.getEntity();
		e.setDeathMessage(null);
		p.setHealth(20);
		TriumphPlayer t_p = plugin.getGameManager().getTriumphPlayer(p);

		int seconds_till_respawn = 10;
		if(t_p.getLobby() == plugin.getGameManager().getLobby())
			seconds_till_respawn = 0;

		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		if(p.getLocation().getY() < 1 || !(t_p.getLobby() instanceof TriumphGame))
			t_p.getLobby().getTeamManager().getSpawnManager().respawnPlayer(t_p);
		
		else if(dead_players.get(p.getPlayer().getUniqueId()+"") == null)
				dead_players.put(p.getPlayer().getUniqueId()+"", 
						scheduler.scheduleSyncRepeatingTask(
								plugin,new DeathSpectatorRunnable(t_p,seconds_till_respawn),0,20));
	}
	private class DeathSpectatorRunnable implements Runnable {

		private TriumphPlayer p;
		private int seconds_left;
		public DeathSpectatorRunnable(TriumphPlayer p, int seconds) {
			p.getPlayer().setGameMode(GameMode.SPECTATOR);
			this.p = p;
			if(seconds > 0)
				sendMessage();
			seconds_left = seconds;

		}
		private void sendMessage() {
			p.getPlayer().sendTitle(ChatColor.GREEN+"Respawning in "+seconds_left+"...","",2,15,5);
		}
		@Override
		public void run() {
			if(seconds_left <= 0) {
				p.getPlayer().setGameMode(GameMode.ADVENTURE);
				p.getPlayer().setHealth(20);
				p.getPlayer().setFireTicks(0);
				p.getPlayer().setFoodLevel(20);
				p.getLobby().getTeamManager().getSpawnManager().respawnPlayer(p);
				plugin.getServer().getScheduler().cancelTask(dead_players.get(p.getPlayer().getUniqueId()+""));
				dead_players.remove(p.getPlayer().getUniqueId()+"");
			}
			else {
				sendMessage();
			}
			seconds_left--;
		}
	}
}
