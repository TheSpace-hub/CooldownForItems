package diachkov.sergey.cooldownForItems.listeners;

import diachkov.sergey.cooldownForItems.CooldownForItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
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
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Material material = event.getItem().getType();
        if (CooldownForItems.checkCooldown(plugin, player, material)) {
            event.setCancelled(true);
        }
        CooldownForItems.addCooldown(plugin, player, material);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (CooldownForItems.checkCooldown(plugin, player, Material.FISHING_ROD)) {
            event.setCancelled(true);
        }
        CooldownForItems.addCooldown(plugin, player, Material.FISHING_ROD);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player))
            return;

        Material material = player.getInventory().getItemInMainHand().getType();
        if (CooldownForItems.checkCooldown(plugin, player, material)) {
            event.setCancelled(true);
        }
        CooldownForItems.addCooldown(plugin, player, material);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = player.getInventory().getItemInMainHand().getType();
        if (CooldownForItems.checkCooldown(plugin, player, material)) {
            event.setCancelled(true);
        }
        CooldownForItems.addCooldown(plugin, player, material);
    }

}
