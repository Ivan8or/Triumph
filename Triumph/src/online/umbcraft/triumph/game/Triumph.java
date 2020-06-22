package online.umbcraft.triumph.game;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import online.umbcraft.triumph.listeners.TriumphAbilityListener;
import online.umbcraft.triumph.listeners.TriumphBuilderListener;
import online.umbcraft.triumph.listeners.TriumphCommandExecutor;
import online.umbcraft.triumph.listeners.TriumphKitSelectorListener;
import online.umbcraft.triumph.listeners.TriumphPlayerJoinListener;
import online.umbcraft.triumph.listeners.TriumphPlayerLeaveListener;
import online.umbcraft.triumph.listeners.TriumphPowerupListener;
import online.umbcraft.triumph.listeners.TriumphEditInventoryListener;
import online.umbcraft.triumph.listeners.TriumphPlayerDeathListener;
import online.umbcraft.triumph.listeners.TriumphTeamSelectorListener;


public class Triumph extends JavaPlugin {

	private GameManager gameManager;
	private Logger log;

	public void onEnable() {
		log = getLogger();
		gameManager = new GameManager(this);
		registerListeners();
		
	}
	
	public void onDisable() {
		System.out.println("stopping...");
		gameManager.unloadAllWorlds();
	}
	public Logger getLog() {
		return log;
	}
	public GameManager getGameManager() {
		return gameManager;
	}
	
	private void registerListeners() {
		getCommand("triumph").setExecutor(new TriumphCommandExecutor(this));
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphAbilityListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphKitSelectorListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphPlayerJoinListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphPowerupListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphTeamSelectorListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphPlayerDeathListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphBuilderListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphEditInventoryListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(
				new TriumphPlayerLeaveListener(this), this);
	}
}
