package online.umbcraft.triumph.player;

import org.bukkit.entity.Player;

import online.umbcraft.triumph.game.TriumphInstance;
import online.umbcraft.triumph.team.TeamColor;
import online.umbcraft.triumph.team.TriumphTeam;

public class TriumphPlayer {
	
	private TriumphInstance lobby_in;
	private TeamColor team;
	private Player player;
	private boolean is_dead;
	
	public TriumphPlayer(Player p) {
		this.player = p;
		this.team = TeamColor.NONE;
	}
	
	public Player getPlayer() {
		return player;
	}
	public String getUID() {
		return player.getUniqueId()+"";
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
	public int getCredits() {
		return 100;
	}
	public void respawn() {
		lobby_in.getTeamManager().getSpawnManager().respawnPlayer(this);
	}
	public void setDead(boolean set_dead) {
		if(is_dead == set_dead)
			return;
		is_dead = set_dead;
		
		if(set_dead) {
			player.setAllowFlight(true);
			player.setFlying(true);
			player.setHealth(20);
			player.setFireTicks(0);
			player.setFoodLevel(20);
			for(TriumphPlayer p: lobby_in.getTeamManager().getAllPlayers())
				p.getPlayer().hidePlayer(lobby_in.getPlugin(), player);
		}
		else {
			player.setAllowFlight(false);
			player.setFlying(false);
			player.setFireTicks(0);
			player.setHealth(20);
			player.setFoodLevel(20);
			for(TriumphPlayer p: lobby_in.getTeamManager().getAllPlayers())
				p.getPlayer().showPlayer(lobby_in.getPlugin(), player);
			
		}
	}
	public boolean isDead() {
		return is_dead;
	}
}
