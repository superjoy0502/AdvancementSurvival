package com.github.superjoy0502.advsurvival;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdvancementSurvival extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
//        saveDefaultConfig();
        config.options().copyDefaults(true);

        // Enable our class to check for new players using onPlayerJoin()
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        config.addDefault("player." + String.valueOf(event.getPlayer().getUniqueId()), new ArrayList<String>());
        saveConfig();
    }

    @EventHandler
    public void onAdvancementAchieved(PlayerAdvancementDoneEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        Advancement advancement = event.getAdvancement();
        String advancementKey = String.valueOf(advancement.getKey());

        if (!advancementKey.contains("minecraft:recipes/")){
            if (Bukkit.getOfflinePlayer(uuid) != null && Bukkit.getOfflinePlayer(uuid).isOnline()) {
                getLogger().info(Bukkit.getPlayer(uuid).getName() + " has completed the advancement: " + advancementKey + "!");
            }
            ArrayList<String> advancementList = (ArrayList<String>) config.getStringList(String.valueOf(uuid));
            advancementList.add(advancementKey);
            config.set("player." + String.valueOf(uuid), advancementList);
            saveConfig();
        }
    }
}
