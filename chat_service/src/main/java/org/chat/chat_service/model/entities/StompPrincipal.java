package org.chat.chat_service.model.entities;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.List;

@Getter
@Setter
public class StompPrincipal implements Principal {
    private final String name;
    private final List<String> roles;

    public StompPrincipal(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }
}

