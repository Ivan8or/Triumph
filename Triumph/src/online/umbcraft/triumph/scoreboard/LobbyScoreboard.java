package online.umbcraft.triumph.scoreboard;

import org.bukkit.ChatColor;

import online.umbcraft.triumph.game.TriumphLobby;
import online.umbcraft.triumph.kit.KitClass;
import online.umbcraft.triumph.player.TriumphPlayer;
import online.umbcraft.triumph.scoreboard.IndividualScoreboard;

public class LobbyScoreboard extends IndividualScoreboard {

	public LobbyScoreboard(TriumphPlayer player, TriumphLobby lobby) {
		super(player);
		if(lobby.getModeIn() == LobbyModeIn.WAITING)
			setTitle(  ChatColor.GREEN+""+ChatColor.BOLD+"  Waiting for Players  ");
		else if(lobby.getModeIn() == LobbyModeIn.SELECTING)
			setTitle( ChatColor.YELLOW+""+ChatColor.BOLD+"    Choosing a Map     ");
		else if(lobby.getModeIn() == LobbyModeIn.STARTING)
			setTitle(" Starting in"+ChatColor.GREEN+""+ChatColor.BOLD+" 20 seconds");
		
		int index = 99;
		insertRow(" ",index--,"empty-1");
		insertRow(ChatColor.YELLOW+""+ChatColor.BOLD+"Players",index--,"players-label");
		insertRow("0/6",index--,"player-count");
		insertRow("  ",index--,"empty-2");
		insertRow(ChatColor.RED+""+ChatColor.BOLD+"Kit",index--,"kit-label");
		insertRow("None",index--,"kit-type");
		insertRow("   ",index--,"empty-3");
		insertRow(ChatColor.GOLD+""+ChatColor.BOLD+"Credits",index--,"credits-label");
		insertRow("0",index--,"credits-count");
		insertRow("    ",index--,"empty-4");
		insertRow(ChatColor.AQUA+""+ChatColor.BOLD+"Map",index--,"map-label");
		insertRow("N/A",index--,"map-name");
	}
	public void setTitle(String new_title) {
		super.setTitle(new_title);
	}
	public void setPlayerCount(int new_count, int max_players) {
		getByTag("player-count").setContent(new_count+"/"+max_players);
	}
	public void setKitType(KitClass kit_type) {
		getByTag("kit-class").setContent(kit_type.name());
	}
	public void setCreditCount(int new_count) {
		getByTag("credits-count").setContent(new_count+"");
	}
	public void setServerName(String new_name) {
		getByTag("kit-class").setContent(new_name);
	}
}
