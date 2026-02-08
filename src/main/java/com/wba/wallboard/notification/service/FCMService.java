package com.wba.wallboard.notification.service;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FCMService {

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);

    /**
     * Send notification to a specific device token
     */
    public void sendNotificationToToken(String token, String title, String body, Map<String, String> data) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data != null ? data : new HashMap<>())
                    .setWebpushConfig(WebpushConfig.builder()
                            .setNotification(WebpushNotification.builder()
                                    .setIcon("/assets/icon.png") // your app icon
                                    .setBadge("/assets/badge.png")
                                    .build())
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent message: {}", response);
        } catch (FirebaseMessagingException e) {
            logger.error("Error sending notification to token: {}", token, e);
            throw new RuntimeException("Failed to send notification", e);
        }
    }

    /**
     * Send notification to a topic (multiple subscribers)
     */
    public void sendNotificationToTopic(String topic, String title, String body, Map<String, String> data) {
        try {
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data != null ? data : new HashMap<>())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Successfully sent message to topic {}: {}", topic, response);
        } catch (FirebaseMessagingException e) {
            logger.error("Error sending notification to topic: {}", topic, e);
            throw new RuntimeException("Failed to send notification to topic", e);
        }
    }

    /**
     * Subscribe a token to a topic
     */
    public void subscribeToTopic(String token, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(java.util.Collections.singletonList(token), topic);
            logger.info("Successfully subscribed to topic {}: {}", topic, response.getSuccessCount());
        } catch (FirebaseMessagingException e) {
            logger.error("Error subscribing to topic: {}", topic, e);
            throw new RuntimeException("Failed to subscribe to topic", e);
        }
    }
}