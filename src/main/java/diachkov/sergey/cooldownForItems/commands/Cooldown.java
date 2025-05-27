package diachkov.sergey.cooldownForItems.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Cooldown implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    public Cooldown(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1 || !strings[0].equals("reload")) {
            if (commandSender instanceof Player player) {
                Component errorMessage = Component.text("Неизвестная команда")
                        .color(TextColor.color(0xff0000));
                player.sendMessage(errorMessage);
                return false;
            }
            plugin.getLogger().severe("Неизвестная команда");
            return false;
        }

        if (commandSender instanceof Player player && !player.hasPermission("cooldownforitems.reload")) {
            Component errorMessage = Component.text("У вас недостаточно прав для этого")
                    .color(TextColor.color(0xff0000));
            player.sendMessage(errorMessage);
            return false;
        }

        plugin.reloadConfig();
        if (commandSender instanceof Player player) {
            Component errorMessage = Component.text("Конфиг перезагружен")
                    .color(TextColor.color(0x00ff00));
            player.sendMessage(errorMessage);
            return false;
        }
        plugin.getLogger().info("Конфиг перезагружен");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player && !player.hasPermission("cooldownforitems.reload"))
            return List.of();

        if (strings.length == 1)
            return List.of("reload");
        return List.of();
    }
}
