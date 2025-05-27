package diachkov.sergey.cooldownForItems;

import diachkov.sergey.cooldownForItems.listeners.MainHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public final class CooldownForItems extends JavaPlugin implements Runnable {

    private static Map<Player, Map<Material, Integer>> cooldowns = new HashMap<>();

    private void printItemsWithCooldown() {
        getLogger().info("Список вещей с ограничениями:");
        ConfigurationSection items = getConfig().getConfigurationSection("items");
        assert items != null;

        for (String item : items.getKeys(false)) {
            getLogger().info("  " + item + ": " + items.getInt(item));
        }
    }

    public static void addPlayerToCooldownsList(Player player) {
        cooldowns.put(player, new HashMap<>());
    }

    public static void removePlayerFromCooldownList(Player player) {
        cooldowns.remove(player);
    }

    public static void addCooldown(Plugin plugin, Player player, Material material) {
        if (checkCooldown(plugin, player, material))
            return;

        String minecraftId = material.getKey().asString();
        if (!plugin.getConfig().getConfigurationSection("items").isSet(minecraftId))
            return;

        int cooldown = plugin.getConfig().getConfigurationSection("items").getInt(minecraftId);

        cooldowns.get(player).put(material, cooldown);
    }

    public static boolean checkCooldown(Plugin plugin, Player player, Material material) {
        String minecraftId = material.getKey().asString();
        if (!plugin.getConfig().getConfigurationSection("items").isSet(minecraftId))
            return false;

        return cooldowns.get(player).containsKey(material);
    }

    public static void everyTick(Plugin plugin) {
        plugin.getLogger().info("Задержки:");
        for (Player player : cooldowns.keySet()) {
            Map<Material, Integer> playerCooldowns = cooldowns.get(player);
            if (playerCooldowns == null) continue;

            Iterator<Map.Entry<Material, Integer>> iterator = playerCooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Material, Integer> entry = iterator.next();
                Material material = entry.getKey();
                int time = entry.getValue();

                plugin.getLogger().info("  " + material.name() + ": " + time);

                if (time <= 0) {
                    iterator.remove();
                } else {
                    entry.setValue(time - 1);
                }
            }
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        printItemsWithCooldown();

        getServer().getPluginManager().registerEvents(new MainHandler(this), this);

        Bukkit.getScheduler().runTaskTimer(this, this, 0L, 1L);
    }

    @Override
    public void run() {
        everyTick(this);
    }
}
