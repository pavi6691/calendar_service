package com.acme.calendar.service.model.User;

public class JwtResponse {

    private String token;
    private long expiresIn;
    private String refreshToken;
    private long refreshTokenExpiresIn;

    public JwtResponse(String token, long expiresIn, String refereshToken, long refreshTokenExpiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.refreshToken = refereshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(long refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
