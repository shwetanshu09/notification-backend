package com.wba.wallboard.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class NotificationApplication {

    private static final Logger logger = LoggerFactory.getLogger(NotificationApplication.class);

    public static void main(String[] args) {
        initializeFirebase();
        
        SpringApplication.run(NotificationApplication.class, args);
    }

    private static void initializeFirebase() {
        try {
            logger.info("=== INITIALIZING FIREBASE ===");
            
            if (!FirebaseApp.getApps().isEmpty()) {
                logger.info("FirebaseApp already initialized");
                return;
            }
            
            GoogleCredentials credentials;
            
            String firebaseCredentialsJson = System.getenv("FIREBASE_CREDENTIALS");
            
            if (firebaseCredentialsJson != null && !firebaseCredentialsJson.isEmpty()) {
                logger.info("Loading Firebase credentials from environment variable");
                credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8))
                );
            } else {
                logger.info("Loading Firebase credentials from file");
                credentials = GoogleCredentials.fromStream(
                    new FileInputStream("src/main/resources/firebase-service-account.json")
                );
            }
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);
            logger.info("=== FIREBASE INITIALIZED SUCCESSFULLY ===");
            
        } catch (IOException e) {
            logger.error("Failed to initialize Firebase", e);
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }
}