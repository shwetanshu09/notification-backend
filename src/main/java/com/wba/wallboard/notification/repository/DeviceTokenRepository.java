package com.wba.wallboard.notification.repository;

import com.wba.wallboard.notification.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    Optional<DeviceToken> findByToken(String token);
    List<DeviceToken> findByUserIdAndActiveTrue(String userId);
    List<DeviceToken> findByActiveTrue();
}