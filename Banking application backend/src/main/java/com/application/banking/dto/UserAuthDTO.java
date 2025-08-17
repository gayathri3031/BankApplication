package com.application.banking.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthDTO {

    private String username;
    private Collection<String> roles;

    public UserAuthDTO(String username, Collection<? extends GrantedAuthority> collection) {
        this.username = username;
        this.roles = collection.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());
    }
    

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
} 