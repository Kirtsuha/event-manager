package dev.sorokin.eventmanager.dto;

import lombok.Data;

@Data
public class JwtTokenResponse {
    private String jwt;

    public JwtTokenResponse(String token) {
        this.jwt = token;
    }
}
