package com.github.superjoy0502.advsurvival;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AdvancementSurvival extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();
    List<Player> onlineplayers;

    @Override
    public void onEnable() {
//        saveDefaultConfig();
        config.options().copyDefaults(true);
        getLogger().info("onEnable");

        // Enable our class to check for new players using onPlayerJoin()
        getServer().getPluginManager().registerEvents(this, this);

        onlineplayers = (List<Player>) Bukkit.getOnlinePlayers();
        for (int i = 0; i < onlineplayers.size(); i++){
            getLogger().info("for loop: " + i);
            if (!config.getStringList("players.").contains(String.valueOf(onlineplayers.get(i).getUniqueId()))){
                getLogger().warning("The config file does not contain the uuid of the "+i+ " player!");
                config.addDefault("players." + onlineplayers.get(i).getUniqueId(), new ArrayList<String>());
            } else {
                getLogger().warning("Creating a new list for player " + i + "!");
                Iterator<Advancement> advancementIterator = getServer().advancementIterator();
                ArrayList<Advancement> advancementArrayList = new ArrayList<Advancement>();

                while (advancementIterator.hasNext()){
                    AdvancementProgress advancementProgress = onlineplayers.get(i).getAdvancementProgress(advancementIterator.next());
                    if (advancementProgress.isDone()){
                        advancementArrayList.add(advancementProgress.getAdvancement());
                    }
                }
                config.set("players." + onlineplayers.get(i).getUniqueId(), advancementArrayList);
                getLogger().warning("Config set.");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        getLogger().info("A player joined.");
        if (!config.getStringList("players.").contains(String.valueOf(event.getPlayer().getUniqueId()))){
            getLogger().warning("A config for " + event.getPlayer().getName() + " does not exist! Creating one...");
            config.addDefault("players." + event.getPlayer().getUniqueId(), new ArrayList<String>());
            saveConfig();
            getLogger().info("Config for " + event.getPlayer().getName() + " successfully created.");
            return;
        }

        getLogger().warning("Creating a new list for " + event.getPlayer().getName() + "!");

        Iterator<Advancement> advancementIterator = getServer().advancementIterator();
        ArrayList<Advancement> advancementArrayList = new ArrayList<Advancement>();

        while (advancementIterator.hasNext()){
            AdvancementProgress advancementProgress = event.getPlayer().getAdvancementProgress(advancementIterator.next());
            if (advancementProgress.isDone()){
                advancementArrayList.add(advancementProgress.getAdvancement());
            }
        }

        config.set("players." + event.getPlayer().getUniqueId(), advancementArrayList);
        getLogger().info("Config for " + event.getPlayer().getName() + " successfully created.");
    }

    @EventHandler
    public void onAdvancementAchieved(PlayerAdvancementDoneEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        String advancementKey = String.valueOf(event.getAdvancement().getKey());

        if (!advancementKey.contains("minecraft:recipes/")){
            if (Bukkit.getOfflinePlayer(uuid) != null && Bukkit.getOfflinePlayer(uuid).isOnline()) {
                getLogger().info(Bukkit.getPlayer(uuid).getName() + " has completed the advancement: " + advancementKey + "!");
            }
            ArrayList<String> advancementList = (ArrayList<String>) config.getStringList("players." + uuid);
            advancementList.add(advancementKey);
            config.set("players." + uuid, advancementList);
            saveConfig();
            getLogger().info("Config for " + event.getPlayer().getName() + " successfully created.");
        }
    }
}
