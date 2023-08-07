package com.github.kumo0621.achievement_bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Achievement_bot extends JavaPlugin implements Listener {

    private Gson gson;
    private Map<String, PlayerData> playerDataMap;

    @Override
    public void onEnable() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        playerDataMap = loadData();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAchievement(PlayerAdvancementDoneEvent e) {
        String player = e.getPlayer().getName();
        String advancementName = e.getAdvancement().getKey().getKey();

        Bukkit.broadcastMessage(ChatColor.GREEN + player + " さんが " + advancementName + " の実績を獲得しました。");

        // Update player's achievement data
        updatePlayerData(player, advancementName);
        String.valueOf(OpenAIAsync.sendTextAsync(player + " さんが " + advancementName + " の実績を獲得しました。"));
    }

    private void updatePlayerData(String playerName, String achievement) {
        playerDataMap.computeIfAbsent(playerName, k -> new PlayerData()).addAchievement(achievement);
        saveData();
    }

    private void saveData() {
        File dataFile = new File(getDataFolder(), "player_data.json");

        try (FileWriter writer = new FileWriter(dataFile)) {
            gson.toJson(playerDataMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, PlayerData> loadData() {
        File dataFile = new File(getDataFolder(), "player_data.json");

        if (!dataFile.exists()) {
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, PlayerData>>() {
            }.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}

class PlayerData {
    private Map<String, Integer> achievements;

    public PlayerData() {
        achievements = new HashMap<>();
    }

    public void addAchievement(String achievement) {
        achievements.put(achievement, achievements.getOrDefault(achievement, 0) + 1);
    }
}