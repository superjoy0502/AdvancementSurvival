package com.github.superjoy0502.advsurvival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class AdvancementSurvival extends JavaPlugin implements Listener {

    public boolean pluginEnabled = false;

    @Override
    public void onEnable() {
//        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getLogger().info("Enabling AdvancementSurvival...");

        getServer().getPluginManager().registerEvents(this, this);

        pluginEnabled = getConfig().get("enablePlugin").equals(true);

//        onlineplayers = (List<Player>) Bukkit.getOnlinePlayers();
//
//        for (int i = 0; i < onlineplayers.size(); i++){
//            getLogger().info("for loop: " + i);
//            if (!config.getStringList("players").contains(String.valueOf(onlineplayers.get(i).getUniqueId()))){
//                getLogger().warning("The config file does not contain the uuid of the "+i+ " player!");
//
//                config.addDefault("players." + onlineplayers.get(i).getUniqueId(), new ArrayList<String>());
//            } else {
//                getLogger().warning("Creating a new list for player " + i + "!");
//                Iterator<Advancement> advancementIterator = getServer().advancementIterator();
//                ArrayList<Advancement> advancementArrayList = new ArrayList<Advancement>();
//
//                while (advancementIterator.hasNext()){
//                    AdvancementProgress advancementProgress = onlineplayers.get(i).getAdvancementProgress(advancementIterator.next());
//                    if (advancementProgress.isDone()){
//                        advancementArrayList.add(advancementProgress.getAdvancement());
//                    }
//                }
//                config.set("players." + onlineplayers.get(i).getUniqueId(), advancementArrayList);
//                getLogger().warning("Config set.");
//            }
//        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling AdvancementSurvival...");
    }

    // Detect what advancements player has achieved and reassign them on a new list

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!pluginEnabled) {
            return;
        }
        getLogger().info("A player joined.");
        getLogger().info("players:\n" + getConfig().getConfigurationSection("players"));
        if (!getConfig().getConfigurationSection("players").contains(String.valueOf(event.getPlayer().getUniqueId()))) {
            getLogger().warning("A config for " + event.getPlayer().getName() + " does not exist! Creating one...");

            getConfig().addDefault("players." + event.getPlayer().getUniqueId(), new ArrayList<String>());
            saveConfig();
            getLogger().info("Config for " + event.getPlayer().getName() + " successfully created.");
            return;
        }

//        getLogger().warning("Creating a new list for " + event.getPlayer().getName() + "!");
//
//        Iterator<Advancement> advancementIterator = getServer().advancementIterator();
//        ArrayList<Advancement> advancementArrayList = new ArrayList<>();
//
//        while (advancementIterator.hasNext()){
//            AdvancementProgress advancementProgress = event.getPlayer().getAdvancementProgress(advancementIterator.next());
//            if (advancementProgress.isDone()){
//                advancementArrayList.add(advancementProgress.getAdvancement());
//            }
//        }
//
//        getConfig().set("players." + event.getPlayer().getUniqueId(), advancementArrayList);
//        getLogger().info("List for " + event.getPlayer().getName() + " successfully created.");
    }

    // Add advancement to list when achieved

    @EventHandler
    public void onAdvancementAchieved(PlayerAdvancementDoneEvent event) {
        if (!pluginEnabled) {
            return;
        }
        UUID uuid = event.getPlayer().getUniqueId();
        String advancementKey = String.valueOf(event.getAdvancement().getKey());

        if (!advancementKey.contains("minecraft:recipes/")) {
            if (Bukkit.getOfflinePlayer(uuid) != null && Bukkit.getOfflinePlayer(uuid).isOnline()) {
                getLogger().info(Bukkit.getPlayer(uuid).getName() + " has completed the advancement: " + advancementKey + "! Editing the config file...");
            }
            ArrayList<String> advancementList = (ArrayList<String>) getConfig().getStringList("players." + uuid);
            advancementList.add(advancementKey);
            getConfig().set("players." + uuid, advancementList);
            saveConfig();
            getLogger().info("Config changes successfully saved.");
        }
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {

        if (event.getRecipe().getResult().isSimilar(new ItemStack(Material.IRON_AXE))) {

        }
    }
}
