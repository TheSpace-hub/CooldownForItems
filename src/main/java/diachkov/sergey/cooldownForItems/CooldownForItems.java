package diachkov.sergey.cooldownForItems;

import diachkov.sergey.cooldownForItems.commands.Cooldown;
import diachkov.sergey.cooldownForItems.listeners.MainHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
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

        String minecraftId = material.getKey().asString().replace("minecraft:", "");
        if (!plugin.getConfig().getConfigurationSection("items").isSet(minecraftId))
            return;

        int cooldown = plugin.getConfig().getConfigurationSection("items").getInt(minecraftId);

        cooldowns.get(player).put(material, cooldown * 20);
    }

    public static boolean checkCooldown(Plugin plugin, Player player, Material material) {
        String minecraftId = material.getKey().asString().replace("minecraft:", "");
        if (!plugin.getConfig().getConfigurationSection("items").isSet(minecraftId))
            return false;

        return cooldowns.get(player).containsKey(material);
    }

    public static void everyTick() {
        for (Player player : cooldowns.keySet()) {
            Component messageForActionBar = Component.empty();

            Map<Material, Integer> playerCooldowns = cooldowns.get(player);
            if (playerCooldowns == null) continue;

            Iterator<Map.Entry<Material, Integer>> iterator = playerCooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Material, Integer> entry = iterator.next();
                Material material = entry.getKey();
                int time = entry.getValue();

                messageForActionBar = messageForActionBar.append(
                        Component.text("/ " + material.name().toLowerCase().replace("_", " ")
                                + ": " + String.format("%.1f", (double) time / 20) + " /")
                );

                if (time <= 0) {
                    iterator.remove();
                } else {
                    entry.setValue(time - 1);
                }
            }

            player.sendActionBar(messageForActionBar);
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        printItemsWithCooldown();

        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayerToCooldownsList(player);
        }

        getServer().getPluginManager().registerEvents(new MainHandler(this), this);
        getCommand("cooldown").setExecutor(new Cooldown(this));
        getCommand("cooldown").setTabCompleter(new Cooldown(this));

        Bukkit.getScheduler().runTaskTimer(this, this, 0L, 1L);
    }

    @Override
    public void run() {
        everyTick();
    }
}
