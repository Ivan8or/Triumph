package online.umbcraft.triumph.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.kit.KitClass;
import online.umbcraft.triumph.map.TriumphMapData;
import online.umbcraft.triumph.map.object.beacon.TriumphBeaconLocation;
import online.umbcraft.triumph.map.object.powerup.PowerupType;
import online.umbcraft.triumph.player.TriumphPlayer;
import online.umbcraft.triumph.team.TeamColor;
import online.umbcraft.triumph.team.TriumphTeamSpawnLocation;

public class TriumphBuilderListener implements Listener{

	private Triumph plugin;

	private Map<String, WandType> wands;
	private Map<String, TriumphMapData> maps;
	private Map<String, KitClass> kit_class;
	private Map<String, PowerupType> powerup_type;
	private Map<String, TeamColor> spawn_color;


	public TriumphBuilderListener(Triumph plugin) {
		this.plugin = plugin;
		wands = new HashMap<String, WandType>();
		kit_class = new HashMap<String, KitClass>();
		powerup_type = new HashMap<String, PowerupType>();
		spawn_color = new HashMap<String, TeamColor>();
		maps = new HashMap<String, TriumphMapData>();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onUseBuildWand(PlayerInteractEvent event) {

		if(event.getItem() == null ||
				event.getItem().getType() != Material.BAMBOO ||
				event.getItem().getItemMeta().getLore() == null ||
				!event.getItem().getItemMeta().getLore().contains(
						ChatColor.DARK_GRAY+"builder-wand")) 
			return;

		event.setCancelled(true);
		Player player = event.getPlayer();
		TriumphPlayer t_p = plugin.getGameManager().getTriumphPlayer(player);

		String player_map_name =
				t_p.getLobby()
				.getMapData()
				.getWorldName();

		if(!maps.containsKey(player.getUniqueId()+"") || 
				!maps.get(player.getUniqueId()+"").getMapName().equals(player_map_name)) 
			maps.put(player.getUniqueId()+"", new TriumphMapData(plugin, player_map_name));

		if(!wands.containsKey(player.getUniqueId()+"")) {
			wands.put(player.getUniqueId()+"", WandType.BEACON);
			spawn_color.put(player.getUniqueId()+"", null);
			powerup_type.put(player.getUniqueId()+"", PowerupType.POINT);
			kit_class.put(player.getUniqueId()+"", KitClass.ASSASSIN);
			spawn_color.put(player.getUniqueId()+"", TeamColor.RED);
		}

		WandType type = wands.get(player.getUniqueId()+"");

		if(event.getAction() == Action.RIGHT_CLICK_AIR || 
				event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(player.isSneaking())type = type.prev();

			else
				type = type.next();
			wands.put(player.getUniqueId()+"", type);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
					ChatColor.RED+"wand mode - "+type.name()
					));
			return;
		}

		TriumphMapData map_data = maps.get(player.getUniqueId()+"");





		boolean clicked_block = event.getAction() == Action.LEFT_CLICK_BLOCK;
		boolean shifting = player.isSneaking();

		if(type == WandType.BEACON) {
			if(clicked_block)
				beaconInteract(t_p,event.getClickedBlock().getLocation(), map_data);
			else
				beaconAdjustSetting(player, shifting);
		}
		else if(type == WandType.POWERUP) {
			if(clicked_block)
				powerupInteract(t_p,event.getClickedBlock().getLocation(), map_data);
			else
				powerupAdjustSetting(player, shifting);
		}
		else if(type == WandType.KIT) {
			if(clicked_block)
				kitInteract(t_p,event.getClickedBlock().getLocation(), map_data);
			else
				kitAdjustSetting(player, shifting);
		}
		else if(type == WandType.SPAWN) {
			if(clicked_block)
				spawnInteract(t_p,event.getClickedBlock().getLocation(), map_data);
			else
				spawnAdjustSetting(player, shifting);
		}
		
	}

	private void beaconInteract(TriumphPlayer p, Location loc, TriumphMapData data) {

		for(TriumphBeaconLocation b: data.getBeaconLocations()) 
			if((int)b.getX() == (int)loc.getX() && (int)b.getY() == (int)loc.getY() && (int)b.getZ() == (int)loc.getZ()) {
				loc.getBlock().setType(Material.GRASS_BLOCK);
				data.removeBeaconLocation(b);
				p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("removed beacon"));
				data.writeBeaconLocations();
				return;
			}
		loc.getBlock().setType(Material.WHITE_STAINED_GLASS);
		data.addBeaconLocation(new TriumphBeaconLocation(
				loc.getX()+":"+loc.getZ(),
				loc.getX(),	loc.getY(),	loc.getZ()));
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("placed beacon"));
		data.writeBeaconLocations();
	}
	private void powerupInteract(TriumphPlayer p, Location loc, TriumphMapData data) {
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("used powerup"));
	}
	private void kitInteract(TriumphPlayer p, Location loc, TriumphMapData data) {
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("used kit"));
	}
	private void spawnInteract(TriumphPlayer p, Location loc, TriumphMapData data) {
		
		for(TriumphTeamSpawnLocation b: data.getSpawnLocations()) 
			if((int)b.getX() == (int)loc.getX() && (int)b.getY() == (int)loc.getY() && (int)b.getZ() == (int)loc.getZ()) {
				loc.getBlock().setType(Material.GRASS_BLOCK);
				data.removeSpawnLocation(b);
				p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("removed spawn"));
				data.writeBeaconLocations();
				return;
			}
		loc.getBlock().setType(Material.RED_STAINED_GLASS);
		data.addSpawnLocation(new TriumphTeamSpawnLocation(
				spawn_color.get(p.getPlayer().getUniqueId()+"").name(),
				loc.getX(),	loc.getY(),	loc.getZ(), p.getPlayer().getLocation().getYaw()+180));
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("placed spawn"));
		data.writeSpawnLocations();
	}

	// nothing to do here :)
	private void beaconAdjustSetting(Player p, boolean is_shifting) {
	}

	// swaps between different powerup types
	private void powerupAdjustSetting(Player p, boolean is_shifting) {
		if(!powerup_type.containsKey(p.getUniqueId()+"")) {
			powerup_type.put(p.getUniqueId()+"", PowerupType.POINT);
		}
		PowerupType type = powerup_type.get(p.getUniqueId()+"");

		if(p.isSneaking()) 
			type = type.prev();
		else 
			type = type.next();
		powerup_type.put(p.getUniqueId()+"", type);
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
				ChatColor.GREEN+"selected powerup type - "+type.name()
				));
	}

	// swaps between kit classes for the kit selector
	private void kitAdjustSetting(Player p, boolean is_shifting) {
		if(!kit_class.containsKey(p.getUniqueId()+"")) {
			kit_class.put(p.getUniqueId()+"", KitClass.ASSASSIN);
		}
		KitClass type = kit_class.get(p.getUniqueId()+"");

		if(p.isSneaking()) 
			type = type.prev();
		else 
			type = type.next();
		kit_class.put(p.getUniqueId()+"", type);
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
				ChatColor.GREEN+"selected class - "+type.name()
				));
	}

	// swaps between team colors for the team spawns
	private void spawnAdjustSetting(Player p, boolean is_shifting) {
		if(!spawn_color.containsKey(p.getUniqueId()+"")) {
			spawn_color.put(p.getUniqueId()+"", TeamColor.RED);
		}
		TeamColor type = spawn_color.get(p.getUniqueId()+"");

		if(p.isSneaking()) 
			type = type.prev();
		else 
			type = type.next();
		spawn_color.put(p.getUniqueId()+"", type);
		p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
				ChatColor.GREEN+"selected team color - "+type.name()
				));
	}

	// represents what type of game object the wand is affecting at any time
	private enum WandType {
		BEACON,
		POWERUP,
		KIT,
		SPAWN;

		public WandType next() {
			if(values().length -1  == ordinal())
				return values()[0];
			return values()[ordinal() + 1];
		}

		public WandType prev() {
			if(0  == ordinal())
				return values()[values().length - 1];
			return values()[ordinal() - 1];
		}
	}

}
