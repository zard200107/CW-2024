package com.example.demo.controller;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ResourceManager {
    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final String IMAGE_PATH = "/com/example/demo/images/";

    public static void preloadResources() {
        String[] imageNames = {
                "background1.jpg",
                "background2.jpg",
                "bossplane.png",
                "enemyplane.png",
                "userplane.png",
                "fireball.png",
                "enemyFire.png",
                "userfire.png",
                "heart.png",
                "shield.png",
                "youwin.png",
                "gameover.png"
        };

        for (String imageName : imageNames) {
            try {
                loadImage(imageName);
            } catch (Exception e) {
                showError("Failed to load image: " + imageName, e);
            }
        }
    }

    public static Image getImage(String imageName) {
        return imageCache.computeIfAbsent(imageName, name -> {
            try {
                return loadImage(name);
            } catch (Exception e) {
                showError("Failed to load image: " + name, e);
                return null;
            }
        });
    }

    private static Image loadImage(String imageName) {
        try {
            String path = IMAGE_PATH + imageName;
            return new Image(ResourceManager.class.getResource(path).toExternalForm());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + imageName, e);
        }
    }

    private static void showError(String message, Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Resource Loading Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.show();
    }
}