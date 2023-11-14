package rod.antidupepro;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FILE_NAME = "antidupe_messages.yml";

    // Save flagged players to the YAML file
    public static void saveFlaggedPlayers(List<String> flaggedPlayers) {
        File file = new File(AntiDupePro.getPlugin(AntiDupePro.class).getDataFolder(), FILE_NAME);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("flaggedPlayers", flaggedPlayers);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load flagged players from the YAML file
    public static List<String> loadFlaggedPlayers() {
        List<String> flaggedPlayers = new ArrayList<>();

        FileConfiguration config = AntiDupePro.getPlugin(AntiDupePro.class).getConfig();
        List<String> playerList = config.getStringList("flaggedPlayers");

        for (String entry : playerList) {
            String playerName = entry.split(":")[1].trim();
            flaggedPlayers.add(playerName);
        }

        return flaggedPlayers;
    }

    // Add a player to the flagged list
    public static void addFlaggedPlayer(String playerName) {
        List<String> flaggedPlayers = loadFlaggedPlayers();
        flaggedPlayers.add(playerName);
        saveFlaggedPlayers(flaggedPlayers);
    }

    // Remove a player from the flagged list
    public static void removeFlaggedPlayer(String playerName) {
        List<String> flaggedPlayers = loadFlaggedPlayers();
        flaggedPlayers.remove(playerName);
        saveFlaggedPlayers(flaggedPlayers);
    }
}
