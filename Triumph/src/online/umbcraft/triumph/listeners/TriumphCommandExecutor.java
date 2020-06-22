package online.umbcraft.triumph.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import net.md_5.bungee.api.ChatColor;
import online.umbcraft.triumph.game.Triumph;
import online.umbcraft.triumph.game.TriumphInstance;
import online.umbcraft.triumph.map.TriumphMapData;
import online.umbcraft.triumph.player.TriumphPlayer;

public class TriumphCommandExecutor implements CommandExecutor, TabCompleter{

	private Triumph plugin;

	public TriumphCommandExecutor(Triumph plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph instance [list, spectate]");
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph map [list, edit, savemap]");
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph wand");
			return true;
		}

		if(args[0].contentEquals("wand")) 
			return wandCommand(sender, cmd, label, args);

		if(args[0].contentEquals("respawn")) 
			return respawnCommand(sender, cmd, label, args);

		if(args[0].contentEquals("map")) 
			return mapCommand(sender, cmd, label, args);

		if(args[0].contentEquals("instance")) 
			return instanceCommand(sender, cmd, label, args);
		return false;
	}

	private boolean respawnCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.getName().contentEquals("CONSOLE")) {
			sender.sendMessage(ChatColor.RED+"Console may not run this command.");
			return false;
		}

		Player pl = (Player) sender;
		TriumphPlayer tp = plugin.getGameManager().getTriumphPlayer(pl);
		tp.getLobby().getTeamManager().getSpawnManager().respawnPlayer(tp);
		sender.sendMessage(ChatColor.GREEN+"You were respawned");

		return false;
	}

	private boolean instanceCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("triumph.move")) {
			sender.sendMessage(ChatColor.RED+"You do not have permission to do this. An administrator will be notified.");
			return false;
		}
		if(args.length == 1) {
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph instance list");
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph instance spectate <game>");
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph instance close");
			return true;
		}
		if(args.length == 2) {
			if(args[1].contentEquals("list")) {
				sender.sendMessage(ChatColor.YELLOW+"All instances:");
				for(TriumphInstance i: plugin.getGameManager().getAllWorlds()) 
					sender.sendMessage(ChatColor.GREEN+i.getMapData().getMapName()+" - (" + i.getWorld().getName()+")");
				return true;
			}
			return false;
		}
		if(args.length == 3) {
			if(args[1].contentEquals("spectate")) {
				if(sender.getName().contentEquals("CONSOLE")) {
					sender.sendMessage(ChatColor.RED+"Console may not run this command.");
					return false;
				}

				TriumphPlayer player = plugin.getGameManager().getTriumphPlayer((Player)sender);
				TriumphInstance world_instance = plugin.getGameManager().getInstance(args[2]);
				if(world_instance == null) {
					sender.sendMessage(ChatColor.RED+"World does not exist.");
					return false;
				}
				world_instance.send(player, null);
				return true;
			}
			if(args[1].contentEquals("close")) {
				TriumphInstance world_instance = plugin.getGameManager().getInstance(args[2]);
				if(world_instance == null) {
					sender.sendMessage(ChatColor.RED+"Instance does not exist.");
					return false;
				}
				if(world_instance == plugin.getGameManager().getLobby()) {
					sender.sendMessage(ChatColor.RED+"Cannot close lobby");
					return false;
				}
				world_instance.unload();
				sender.sendMessage(ChatColor.GREEN+"Closed Instance "+args[2]);
				return true;
			}
			sender.sendMessage(ChatColor.RED+"Invalid arguments. .");
			return false;
		}
		sender.sendMessage(ChatColor.RED+"Invalid arguments. .");
		return false;
	}


	private boolean mapCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("triumph.admin")) {
			sender.sendMessage(ChatColor.RED+"You do not have permission to do this. An administrator will be notified.");
			return false;
		}
		if(args.length == 1) {
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph map edit <existing_map_name / new_map_name>");
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph map savemap");
			sender.sendMessage(ChatColor.GREEN+"useage: /triumph map list");
			return true;
		}
		if(args.length == 2) {
			if(args[1].contentEquals("list")) {
				sender.sendMessage(ChatColor.YELLOW+"All maps:");
				for(TriumphMapData m: plugin.getGameManager().getMapmanager().getAllMaps()) 
					sender.sendMessage(ChatColor.GREEN+m.getMapName()+" - (" + m.getWorldName()+")");
				return true;
			}
			if(args[1].contentEquals("savemap")) {
				if(sender.getName().contentEquals("CONSOLE")) {
					sender.sendMessage(ChatColor.RED+"Console may not run this command.");
					return false;
				}

				sender.sendMessage(ChatColor.GREEN+"Updating world file...");

				TriumphPlayer pl = plugin.getGameManager().getTriumphPlayer((Player) sender);
				pl.getLobby().getMapData().writeWorldFile(pl.getPlayer().getWorld());

				sender.sendMessage(ChatColor.GREEN+"World file for map "+pl.getLobby().getMapData().getMapName()+" has been updated");
				return true;
			}
			sender.sendMessage(ChatColor.RED+"Invalid arguments. .");
			return false;
		}
		if(args.length == 3) {
			if(args[1].contentEquals("edit")) {
				if(sender.getName().contentEquals("CONSOLE")) {
					sender.sendMessage(ChatColor.RED+"Console may not run this command.");
					return false;
				}

				TriumphMapData to_edit = plugin.getGameManager().getMapmanager().getMap(args[2]);
				String world_name = plugin.getGameManager().getFreeWorldName("edit");
				TriumphInstance edit_map = new TriumphInstance(plugin, world_name, to_edit);
				plugin.getGameManager().addInstance(edit_map);
				edit_map.send(plugin.getGameManager().getTriumphPlayer((Player) sender), null);
				((Player) sender).setGameMode(GameMode.CREATIVE);
				return true;

			}
			sender.sendMessage(ChatColor.RED+"Invalid arguments. .");
			return false;
		}
		sender.sendMessage(ChatColor.RED+"Invalid arguments. .");
		return false;
	}
	private boolean wandCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("triumph.build")) {
			sender.sendMessage(ChatColor.RED+"You do not have permission to do this. An administrator will be notified.");
			return false;
		}
		if(sender.getName().contentEquals("CONSOLE")) {
			sender.sendMessage(ChatColor.RED+"Console may not run this command.");
			return false;
		}
		Player pl = (Player) sender;
		pl.sendMessage(ChatColor.GREEN+"you have received a Builder Wand!");
		ItemStack builder_wand = new ItemStack(Material.BAMBOO,1);

		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_GRAY+"builder-wand");
		ItemMeta meta = builder_wand.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.GREEN+"Builder Wand");

		builder_wand.setItemMeta(meta);
		builder_wand.getItemMeta().getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);

		pl.getInventory().addItem(builder_wand);
		pl.updateInventory();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		List<String> completions = new ArrayList<>();
		List<String> commands = new ArrayList<>();

		if (args.length == 1) {
			if (sender.hasPermission("triumph.build")) {
				commands.add("wand");
				commands.add("map");
				commands.add("instance");
			}
			StringUtil.copyPartialMatches(args[0], commands, completions);
		}
		else if (args.length == 2) {
			if (args[0].contentEquals("map")) {
				commands.add("edit");
				commands.add("savemap");
				commands.add("list");
			}
			else if (args[0].contentEquals("instance")) {
				commands.add("list");
				commands.add("spectate");
				commands.add("close");
			}
			StringUtil.copyPartialMatches(args[args.length-1], commands, completions);
		}
		else if (args.length == 3) {
			if (args[0].contentEquals("map")) {
				if(args[1].contentEquals("edit")) {
					for(TriumphMapData m: plugin.getGameManager().getMapmanager().getAllMaps()) {
						commands.add(m.getWorldName());
					}
				}
			}
			else if (args[0].contentEquals("instance")) {
				if(args[1].contentEquals("spectate") || args[1].contentEquals("close")) {
					for(TriumphInstance i: plugin.getGameManager().getAllWorlds()) {
						commands.add(i.getWorld().getName());
					}
				}
			}
			StringUtil.copyPartialMatches(args[args.length-1], commands, completions);
		}
		Collections.sort(completions);
		return completions;
	}
}
