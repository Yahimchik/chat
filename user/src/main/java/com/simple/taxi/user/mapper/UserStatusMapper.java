package com.simple.taxi.user.mapper;

import com.simple.taxi.user.model.dto.UserEvent;
import com.simple.taxi.user.model.entities.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

    @Mapping(target = "userId", source = "id")
    UserStatus toEntity(UserEvent.User user);
}

