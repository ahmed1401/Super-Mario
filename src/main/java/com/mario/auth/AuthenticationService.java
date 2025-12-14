package com.mario.auth;

import com.mario.utils.GameLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication service for user login/registration.
 * Stores user data in a JSON file.
 */
public class AuthenticationService {

    private static final String USERS_FILE = "data/users.json";
    private static AuthenticationService instance;

    private Map<String, User> users;
    private User currentUser;
    private Gson gson;

    private AuthenticationService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new HashMap<>();
        loadUsers();
    }

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Register a new user
     */
    public boolean register(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        if (users.containsKey(username.toLowerCase())) {
            GameLogger.logAuth("Registration failed - user exists", username);
            return false;
        }

        User newUser = new User(username, hashPassword(password));
        users.put(username.toLowerCase(), newUser);
        saveUsers();

        GameLogger.logAuth("Registered", username);
        return true;
    }

    /**
     * Login a user
     */
    public boolean login(String username, String password) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        User user = users.get(username.toLowerCase());
        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            currentUser = user;
            GameLogger.logAuth("Login successful", username);
            return true;
        }

        GameLogger.logAuth("Login failed", username);
        return false;
    }

    /**
     * Login as guest
     */
    public void loginAsGuest() {
        currentUser = new User("Guest", "");
        currentUser.setGuest(true);
        GameLogger.logAuth("Guest login", "Guest");
    }

    /**
     * Logout current user
     */
    public void logout() {
        if (currentUser != null) {
            GameLogger.logAuth("Logout", currentUser.getUsername());
            currentUser = null;
        }
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Get current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Update user's high score
     */
    public void updateHighScore(int score) {
        if (currentUser != null && !currentUser.isGuest()) {
            if (score > currentUser.getHighScore()) {
                currentUser.setHighScore(score);
                saveUsers();
                GameLogger.logAuth("New high score", currentUser.getUsername() + ": " + score);
            }
        }
    }

    /**
     * Save user progress
     */
    public void saveProgress(int level, int lives, int score) {
        if (currentUser != null && !currentUser.isGuest()) {
            currentUser.setLastLevel(level);
            currentUser.setSavedLives(lives);
            currentUser.setSavedScore(score);
            saveUsers();
            GameLogger.logAuth("Progress saved", currentUser.getUsername());
        }
    }

    private void loadUsers() {
        try {
            Path path = Paths.get(USERS_FILE);
            if (Files.exists(path)) {
                String json = Files.readString(path);
                Type type = new TypeToken<Map<String, User>>() {
                }.getType();
                users = gson.fromJson(json, type);
                if (users == null) {
                    users = new HashMap<>();
                }
                GameLogger.info("Loaded " + users.size() + " users");
            }
        } catch (IOException e) {
            GameLogger.logError("Failed to load users", e);
            users = new HashMap<>();
        }
    }

    private void saveUsers() {
        try {
            Path path = Paths.get(USERS_FILE);
            Files.createDirectories(path.getParent());
            String json = gson.toJson(users);
            Files.writeString(path, json);
        } catch (IOException e) {
            GameLogger.logError("Failed to save users", e);
        }
    }

    private String hashPassword(String password) {
        // Simple hash for demo - in production use proper hashing
        return Integer.toHexString(password.hashCode());
    }
}
