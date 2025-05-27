package diachkov.sergey.cooldownForItems.listeners;

import diachkov.sergey.cooldownForItems.CooldownForItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;


public class MainHandler implements Listener {

    private final Plugin plugin;

    public MainHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        CooldownForItems.addPlayerToCooldownsList(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CooldownForItems.removePlayerFromCooldownList(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null)
            return;

        CooldownForItems.addCooldown(plugin, player, event.getItem().getType());
    }

}
