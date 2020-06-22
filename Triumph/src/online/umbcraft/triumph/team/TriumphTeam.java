package online.umbcraft.triumph.team;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.player.TriumphPlayer;

public class TriumphTeam {

	private Triumph plugin;
	private TeamManager manager;

	private TeamColor color;
	private int score;
	private List<TriumphPlayer> players;

	public TriumphTeam(Triumph plugin, TeamManager manager, TeamColor color) {
		this.plugin = plugin;
		this.manager = manager;
		this.color = color;
		score = 0;
		
		players = new ArrayList<TriumphPlayer>();
	}
	public TeamManager getTeamManager() {
		return manager;
	}
	public TeamColor getColor() {
		return color;
	}
	public Triumph getPlugin() {
		return plugin;
	}
	public synchronized int getScore() {
		return score;
	}
	public List<TriumphPlayer> getPlayers() {
		return players;
	}
	public synchronized void addToScore(int amount) {
		score+=amount;
	}
	public synchronized void addPlayer(TriumphPlayer p) {
		p.setTeam(color);
		players.add(p);
	}
	public synchronized void removePlayer(TriumphPlayer p) {
		System.out.println("TriumphTeam removing a player!");
		for(TriumphPlayer pp: players)
			System.out.println("has: "+pp.getPlayer().getName());
		System.out.println("--------------");
		if(players.contains(p)) {
			p.setTeam(TeamColor.NONE);
			players.remove(p);
			System.out.println("removing "+p.getPlayer().getName());
		}
	}



}
