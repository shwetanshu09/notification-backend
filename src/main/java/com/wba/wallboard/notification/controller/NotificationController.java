package com.wba.wallboard.notification.controller;

import com.wba.wallboard.notification.entity.DeviceToken;
import com.wba.wallboard.notification.repository.DeviceTokenRepository;
import com.wba.wallboard.notification.service.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private FCMService fcmService;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerToken(@RequestBody TokenRequest request) {
        DeviceToken deviceToken = deviceTokenRepository.findByToken(request.getToken())
                .orElse(new DeviceToken());
        
        deviceToken.setToken(request.getToken());
        deviceToken.setUserId(request.getUserId());
        deviceToken.setActive(true);
        deviceToken.setUpdatedAt(LocalDateTime.now());
        
        deviceTokenRepository.save(deviceToken);

        // Subscribe to a default topic for all users
        fcmService.subscribeToTopic(request.getToken(), "all-alerts");
        
        return ResponseEntity.ok(Map.of("message", "Token registered successfully"));
    }

    @PostMapping("/send-test")
    public ResponseEntity<?> sendTestNotification(@RequestBody TestNotificationRequest request) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "test");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        fcmService.sendNotificationToToken(
                request.getToken(),
                "Test Notification",
                "This is a test notification from your monitoring system",
                data
        );
        
        return ResponseEntity.ok(Map.of("message", "Notification sent"));
    }

    public static class TokenRequest {
        private String token;
        private String userId;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }

    public static class TestNotificationRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}