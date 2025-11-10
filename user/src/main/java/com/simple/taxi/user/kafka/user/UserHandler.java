package com.simple.taxi.user.kafka.user;

import com.simple.taxi.user.kafka.core.AbstractEntityChangeHandler;
import com.simple.taxi.user.mapper.UserProfileMapper;
import com.simple.taxi.user.mapper.UserSettingsMapper;
import com.simple.taxi.user.mapper.UserStatusMapper;
import com.simple.taxi.user.model.dto.UserEvent;
import com.simple.taxi.user.model.entities.UserProfile;
import com.simple.taxi.user.model.entities.UserSettings;
import com.simple.taxi.user.model.entities.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserHandler extends AbstractEntityChangeHandler {

    public static final String AUTH_PUBLIC_USERS = "auth.public.users";
    public static final String USER_ID = "id";
    private final UserProfileMapper mapper;
    private final UserSettingsMapper userSettingsMapper;
    private final UserStatusMapper userStatusMapper;

    public UserHandler(R2dbcEntityTemplate template, UserProfileMapper mapper, UserSettingsMapper userSettingsMapper, UserStatusMapper userStatusMapper) {
        super(template);
        this.mapper = mapper;
        this.userSettingsMapper = userSettingsMapper;
        this.userStatusMapper = userStatusMapper;
    }

    @Override
    public String getTopic() {
        return AUTH_PUBLIC_USERS;
    }

    @Override
    public void onCreate(Object after) {
        var user = (UserEvent.User) after;

        var profile = mapper.toEntity(user);
        var settings = userSettingsMapper.toEntity(user);
        var status = userStatusMapper.toEntity(user);

        Mono.when(
                        template.insert(UserProfile.class).using(profile),
                        template.insert(UserSettings.class).using(settings),
                        template.insert(UserStatus.class).using(status)
                )
                .doOnSuccess(v -> log.info("✅ Created profile + settings + status for user {}", user.getId()))
                .doOnError(e -> log.error("❌ Failed to insert user {}: {}", user.getId(), e.getMessage(), e))
                .subscribe();

    }

    @Override
    public void onUpdate(Object after, Object before) {
        var userAfter = (UserEvent.User) after;
        var userBefore = (UserEvent.User) before;
        applyPartialUpdate(UserProfile.class, USER_ID, userAfter.getId(), userAfter, userBefore);
    }

    @Override
    public void onDelete(Object before) {
        var user = (UserEvent.User) before;
        deleteById(user.getId(), UserProfile.class, UserSettings.class);
    }
}