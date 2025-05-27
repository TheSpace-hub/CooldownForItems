package diachkov.sergey.cooldownForItems;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class CooldownForItems extends JavaPlugin {

    private void printItemsWithCooldown() {
        getLogger().info("Список вещей с ограничениями:");
        ConfigurationSection items = getConfig().getConfigurationSection("items");
        assert items != null;

        for (String item : items.getKeys(false)) {
            getLogger().info("  " + item + ": " + items.getInt(item));
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        printItemsWithCooldown();
    }
}
