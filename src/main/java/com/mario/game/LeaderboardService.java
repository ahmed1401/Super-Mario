package com.mario.game;

import com.mario.utils.GameLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service for managing and persisting leaderboard data.
 */
public class LeaderboardService {

    private static final String LEADERBOARD_FILE = "data/leaderboard.json";
    private static final int MAX_ENTRIES = 100;

    private static LeaderboardService instance;
    private List<LeaderboardEntry> entries;
    private Gson gson;

    public static class LeaderboardEntry {
        private String playerName;
        private int score;
        private int level;
        private String date;

        public LeaderboardEntry(String playerName, int score, int level) {
            this.playerName = playerName;
            this.score = score;
            this.level = level;
            this.date = java.time.LocalDate.now().toString();
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public int getLevel() {
            return level;
        }

        public String getDate() {
            return date;
        }
    }

    private LeaderboardService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.entries = new ArrayList<>();
        loadLeaderboard();
    }

    public static LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardService();
        }
        return instance;
    }

    /**
     * Add a score to the leaderboard
     */
    public boolean addScore(String playerName, int score, int level) {
        LeaderboardEntry newEntry = new LeaderboardEntry(playerName, score, level);
        entries.add(newEntry);

        // Sort by score descending
        entries.sort(Comparator.comparingInt(LeaderboardEntry::getScore).reversed());

        // Keep only top entries
        while (entries.size() > MAX_ENTRIES) {
            entries.remove(entries.size() - 1);
        }

        saveLeaderboard();

        // Check if this score made it to the leaderboard
        boolean madeIt = entries.contains(newEntry);
        if (madeIt) {
            GameLogger.logObserverNotification("New leaderboard entry", playerName + ": " + score);
        }

        return madeIt;
    }

    /**
     * Get all leaderboard entries
     */
    public List<LeaderboardEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Get top N entries
     */
    public List<LeaderboardEntry> getTopEntries(int n) {
        return entries.subList(0, Math.min(n, entries.size()));
    }

    /**
     * Check if a score qualifies for leaderboard
     */
    public boolean qualifiesForLeaderboard(int score) {
        if (entries.size() < MAX_ENTRIES) {
            return true;
        }
        return score > entries.get(entries.size() - 1).getScore();
    }

    /**
     * Get player's best score
     */
    public int getPlayerBestScore(String playerName) {
        return entries.stream()
                .filter(e -> e.getPlayerName().equalsIgnoreCase(playerName))
                .mapToInt(LeaderboardEntry::getScore)
                .max()
                .orElse(0);
    }

    /**
     * Get player's rank
     */
    public int getPlayerRank(String playerName) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getPlayerName().equalsIgnoreCase(playerName)) {
                return i + 1;
            }
        }
        return -1; // Not on leaderboard
    }

    private void loadLeaderboard() {
        try {
            Path path = Paths.get(LEADERBOARD_FILE);
            if (Files.exists(path)) {
                String json = Files.readString(path);
                Type type = new TypeToken<List<LeaderboardEntry>>() {
                }.getType();
                entries = gson.fromJson(json, type);
                if (entries == null) {
                    entries = new ArrayList<>();
                }
                GameLogger.info("Loaded " + entries.size() + " leaderboard entries");
            }
        } catch (IOException e) {
            GameLogger.logError("Failed to load leaderboard", e);
            entries = new ArrayList<>();
        }
    }

    private void saveLeaderboard() {
        try {
            Path path = Paths.get(LEADERBOARD_FILE);
            Files.createDirectories(path.getParent());
            String json = gson.toJson(entries);
            Files.writeString(path, json);
        } catch (IOException e) {
            GameLogger.logError("Failed to save leaderboard", e);
        }
    }
}
