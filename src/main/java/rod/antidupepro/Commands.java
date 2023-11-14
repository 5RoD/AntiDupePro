package rod.antidupepro;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Commands implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has permission to change their game mode
            if (player.hasPermission("gravemc.staff")) {
                if (label.equalsIgnoreCase("gmc")) {
                    // Change the player's game mode to Creative (GM creative)
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(ChatColor.GREEN + "Your game mode has been set to Creative!");
                } else if (label.equalsIgnoreCase("gms")) {
                    // Change the player's game mode to Survival (GM survival)
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ChatColor.GREEN + "Your game mode has been set to Survival!");
                } else if (label.equalsIgnoreCase("gmsp")) {
                    // Change the player's game mode to Spectator (GM spectator)
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(ChatColor.GREEN + "Your game mode has been set to Spectator!");
                } else if (label.equalsIgnoreCase("heal")) {
                    player.setHealth(20);
                    player.sendMessage(ChatColor.GREEN + "You have been healed!");
                } else if (label.equalsIgnoreCase("feed")) {
                    player.setFoodLevel(20);
                    player.sendMessage(ChatColor.GREEN + "You have been healed!");
                } else if (label.equalsIgnoreCase("fly")) {
                    toggleFlight(player);
                } else {
                    // Unknown command label
                    player.sendMessage("Unknown command label!");
                }
            } else {
                // Player doesn't have permission
                player.sendMessage("You don't have permission to change your game mode!");
            }
        } else {
            // Console or command block sender
            sender.sendMessage("Only players can use this command!");
        }

        return true;
    }

    private void toggleFlight(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage("You cannot toggle flight in Creative or Spectator mode.");
            return;
        }

        boolean isFlying = player.isFlying();
        player.setAllowFlight(!isFlying);
        player.setFlying(!isFlying);

        if (isFlying) {
            player.sendMessage("Flight mode disabled.");
        } else {
            player.sendMessage("Flight mode enabled.");
        }
    }
}
