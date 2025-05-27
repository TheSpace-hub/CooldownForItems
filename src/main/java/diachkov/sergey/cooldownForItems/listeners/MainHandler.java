package diachkov.sergey.cooldownForItems.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class MainHandler implements Listener {

    private Plugin plugin;

    public MainHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        plugin.getLogger().info("Событие!");
    }

}
