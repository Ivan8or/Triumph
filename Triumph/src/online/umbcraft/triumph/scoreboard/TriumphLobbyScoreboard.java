package online.umbcraft.triumph.scoreboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import online.umbcraft.triumph.game.TriumphLobby;
import online.umbcraft.triumph.player.TriumphPlayer;

public class TriumphLobbyScoreboard {

	private TriumphLobby lobby;
	private Map<String, LobbyScoreboard> player_boards;
	

	public TriumphLobbyScoreboard(TriumphLobby lobby) {
		this.lobby = lobby;
		player_boards = new HashMap<String, LobbyScoreboard>();
	}

	public void includePlayer(TriumphPlayer p) {
		LobbyScoreboard new_board = new LobbyScoreboard(p, lobby);
		player_boards.put(p.getPlayer().getUniqueId()+"", new_board);
	}
	public void removePlayer(TriumphPlayer p) {
		LobbyScoreboard to_remove = player_boards.remove(p.getPlayer().getUniqueId()+"");
		if(to_remove != null)
			to_remove.remove();

	}
	public LobbyScoreboard getPlayerScoreboard(TriumphPlayer p) {
		return player_boards.get(p.getPlayer().getUniqueId()+"");
	}
	public void setPlayerCount(int current, int max) {
		for(String key: player_boards.keySet())
			player_boards.get(key).setPlayerCount(current, max);
	}

	public void updateScoreboards() {
		
	}
}
