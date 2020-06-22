package online.umbcraft.triumph.player;

import org.bukkit.entity.Player;

import online.umbcraft.triumph.game.TriumphInstance;
import online.umbcraft.triumph.team.TeamColor;
import online.umbcraft.triumph.team.TriumphTeam;

public class TriumphPlayer {
	
	private TriumphInstance lobby_in;
	private TeamColor team;
	private Player player;
	
	public TriumphPlayer(Player p) {
		this.player = p;
		this.team = TeamColor.NONE;
	}
	
	public Player getPlayer() {
		return player;
	}
	public synchronized void setPlayer(Player p) {
		player = p;
	}
	public TriumphInstance getLobby() {
		return lobby_in;
	}
	public void setLobby(TriumphInstance lobby) {
		lobby_in = lobby;
	}
	public TeamColor getTeam() {
		return team;
	}
	public void setTeam(TeamColor team) {
		this.team = team;
	}
	
}
