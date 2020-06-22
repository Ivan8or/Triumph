package online.umbcraft.triumph.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import online.umbcraft.triumph.game.Triumph;

public class TriumphEditInventoryListener implements Listener{
	
	private Triumph plugin;
	
	public TriumphEditInventoryListener(Triumph plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSwapHandItem(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEditInventory(InventoryClickEvent e) {
		if(e.getWhoClicked().getGameMode() != GameMode.CREATIVE)
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEditInventory(PlayerDropItemEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
			e.setCancelled(true);
	}
}
