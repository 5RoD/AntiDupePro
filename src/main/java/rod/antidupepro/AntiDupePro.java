package rod.antidupepro;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class AntiDupePro extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            GuiCommand.openFlaggedPlayersGUI(player);
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return true;
    }


    // Listen for the /dupealerts command
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/dupealerts")) {
            event.setCancelled(true); // Cancel the default behavior of the command
            GuiCommand.openFlaggedPlayersGUI(event.getPlayer());
        }
    }

    private static final String LOG_FILE_NAME = "antidupe_messages.yml";
    private static final String JOIN_MESSAGE = "&aWelcome to the server, %player%";
    private static final String DUPLICATE_ITEM_MESSAGE = "&c&l%player% &chas more than 20 items of type";

    @Override
    public void onEnable() {
        getCommand("dupealerts").setExecutor(this);
        getServer().getPluginManager().registerEvents(new Commands(), this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new GuiCommand(), this);


        getLogger().info("AntiDupePro plugin enabled.");
        getLogger().info("                           ");
        System.out.println("                      _        _             _____               _ ");
        System.out.println("                     | |      | |           | ____|             | |");
        System.out.println("  _ __ ___   __ _  __| | ___  | |__  _   _  | |__  _ __ ___   __| |");
        System.out.println(" | '_ ` _ \\ / _` |/ _` |/ _ \\ | '_ \\| | | | |___ \\| '__/ _ \\ / _` |");
        System.out.println(" | | | | | | (_| | (_| |  __/ | |_) | |_| |  ___) | | | (_) | (_| |");
        System.out.println(" |_| |_| |_|\\__,_|\\__,_|\\___| |_.__/ \\__, | |____/|_| |___/ \\__,_|");
        System.out.println("                                      __/ |                        ");
        System.out.println("                                     |___/                         ");
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String formattedJoinMessage = translateColorCodes(JOIN_MESSAGE, player);
        player.sendMessage(formattedJoinMessage);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        checkInventory(player);
    }

    private void checkInventory(Player player) {
        Map<Material, Integer> itemCounts = new HashMap<>();

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                continue;
            }

            if (itemCounts.containsKey(itemStack.getType())) {
                int currentCount = itemCounts.get(itemStack.getType());
                itemCounts.put(itemStack.getType(), currentCount + itemStack.getAmount());
            } else {
                itemCounts.put(itemStack.getType(), itemStack.getAmount());
            }
        }


        String duplicateMessage = "";
        boolean hasDuplicate = false;

        for (Map.Entry<Material, Integer> entry : itemCounts.entrySet()) {
            Material itemType = entry.getKey();
            int totalCount = entry.getValue();

            if (totalCount > 20) {
                int stacks = totalCount / 64;
                if (stacks > 0) {
                    duplicateMessage += "&a" + itemType.name() + ": " + stacks + " stacks, ";
                    hasDuplicate = true;
                } else {
                    duplicateMessage += "&a" + itemType.name() + ": " + totalCount + ", ";
                    hasDuplicate = true;
                }
            }
        }

        if (hasDuplicate) {
            duplicateMessage = duplicateMessage.substring(0, duplicateMessage.length() - 2);
            logDuplicateItems(player.getName(), duplicateMessage);

            for (Player op : getOnlinePlayers()) {
                if (op.isOp()) {
                    String formattedDuplicateItemMessage = translateColorCodes(DUPLICATE_ITEM_MESSAGE, player);
                    op.sendMessage(formattedDuplicateItemMessage);
                }
            }
        }
    }

    private void logDuplicateItems(String playerName, String duplicateMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + LOG_FILE_NAME, true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);

            writer.write(timestamp + " - " + playerName + ": Found " + duplicateMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String translateColorCodes(String message, Player player) {
        return message.replace('&', ChatColor.COLOR_CHAR)
                .replace("%player%", player.getName());
    }

    private String translateColorCodes(String message, Player player, ItemStack itemStack) {
        return message.replace('&', ChatColor.COLOR_CHAR)
                .replace("%player%", player.getName())
                .replace("%item%", itemStack.getType().name())
                .replace("%amount%", String.valueOf(itemStack.getAmount()))
                .replace("%type%", itemStack.getType().name());

    }
}

