package online.umbcraft.triumph.game;

import online.umbcraft.triumph.map.TriumphMapData;
import online.umbcraft.triumph.player.TriumphPlayer;
import online.umbcraft.triumph.scoreboard.LobbyModeIn;
import online.umbcraft.triumph.scoreboard.TriumphLobbyScoreboard;
import online.umbcraft.triumph.team.TeamColor;

public class TriumphLobby extends TriumphInstance{
	
	private TriumphLobbyScoreboard scoreboard;
	private LobbyModeIn mode_in;

	public TriumphLobby(Triumph plugin, String world_name, TriumphMapData map) {
		super(plugin, world_name, map);
		scoreboard = new TriumphLobbyScoreboard(this);
		mode_in = LobbyModeIn.WAITING;
 	}	
	public void removePlayer(TriumphPlayer p) {
		super.removePlayer(p);
		scoreboard.removePlayer(p);
		scoreboard.setPlayerCount(this.getNumConnected(), 6);
		
	}
	public void send(TriumphPlayer p, TeamColor c) {
		super.send(p,c);
		scoreboard.includePlayer(p);
		scoreboard.setPlayerCount(this.getNumConnected(), 6);
	}
	
	public void advanceLobby() {
		
	}
	
	public void waitingForPlayersMode() {
		mode_in = LobbyModeIn.WAITING;
	}

	public void selectingMapMode() {
		mode_in = LobbyModeIn.SELECTING;
	}

	public void gameStartingMode() {
		mode_in = LobbyModeIn.STARTING;
	}

	public LobbyModeIn getModeIn() {
		return mode_in;
	}
	
	public class LobbyRunnable implements Runnable {

		TriumphLobby parent;
		public LobbyRunnable(TriumphLobby parent) {
			this.parent = parent;
		}
		@Override
		public void run() {
			parent.advanceLobby();
		}
	}
}
