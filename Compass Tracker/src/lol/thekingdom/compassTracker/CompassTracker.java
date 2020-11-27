package lol.thekingdom.compassTracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Ethan Gervais
 *
 */
public class CompassTracker extends JavaPlugin implements Listener {
	private Player targetPlayer;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		targetPlayer = null;
	}

	@Override
	public void onDisable() {

	}

	@EventHandler
	public void onCompassClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if ((player.getInventory().getItemInMainHand().getType() != null
				&& player.getInventory().getItemInMainHand().getType() == Material.COMPASS)
				&& (event.getAction().equals(Action.RIGHT_CLICK_AIR)
						|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			if (targetPlayer == null) {
				player.sendMessage(ChatColor.AQUA + "No target has been selected.");
				return;
			} else {
				if (!(targetPlayer.getWorld().getEnvironment() == Environment.NETHER
						|| targetPlayer.getWorld().getEnvironment() == Environment.THE_END)) {
					event.getPlayer().setCompassTarget(targetPlayer.getLocation());
					player.sendMessage(
							ChatColor.AQUA + "Tracker updated to " + targetPlayer.getName() + "'s current location.");
				} else {
					player.sendMessage(ChatColor.RED + "Could not locate player.");
				}
			}
			event.setCancelled(true);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		if (sender.isOp() && cmd.getName().equalsIgnoreCase("settarget")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Incomplete command. Please provide a username for the target.");
			} else {
				targetPlayer = Bukkit.getPlayerExact(args[0]);
				if (targetPlayer == null) {
					sender.sendMessage("This player is offline!");
				} else {
					Bukkit.broadcastMessage(ChatColor.GREEN + args[0]
							+ " has been selected. Right click on your compass to track them.");
				}
			}
		} else if (sender.isOp() && cmd.getName().equalsIgnoreCase("removetarget")) {
			targetPlayer = null;
		}

		return true;
	}
}
