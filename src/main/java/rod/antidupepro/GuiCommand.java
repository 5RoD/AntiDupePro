package rod.antidupepro;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class GuiCommand implements Listener {

    // Method to open the Flagged Players GUI
    public static void openFlaggedPlayersGUI(Player opener) {
        Inventory gui = Bukkit.createInventory(opener, 27, "Flagged Players");

        // Load flagged players from the YAML file
        List<String> flaggedPlayers = FileManager.loadFlaggedPlayers();

        // Display flagged players in the GUI
        for (int i = 0; i < flaggedPlayers.size(); i++) {
            String playerName = flaggedPlayers.get(i);
            ItemStack playerHead = createPlayerHead(playerName);
            gui.addItem(playerHead);
        }

        // Add navigation buttons (you may customize these buttons)
        ItemStack nextButton = createNavigationButton("Next");
        ItemStack backButton = createNavigationButton("Back");

        gui.setItem(24, backButton);
        gui.setItem(26, nextButton);

        opener.openInventory(gui);
    }

    // Method to create a player head ItemStack
    private static ItemStack createPlayerHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(playerName);
        head.setItemMeta(meta);
        return head;
    }

    // Method to create navigation buttons
    private static ItemStack createNavigationButton(String buttonText) {
        ItemStack button = new ItemStack(Material.ARROW);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(buttonText);
        button.setItemMeta(meta);
        return button;
    }

    // Handle inventory click events
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
    }
}
