package com.example.SecurityAuthentication.dto;

public class JwtResponseDto {

    private String validateToken;

    public JwtResponseDto() {
        // Default constructor
    }

    public String getValidateToken() {
        return validateToken;
    }

    public void setValidateToken(String validateToken) {
        this.validateToken = validateToken;
    }

    @Override
    public String toString() {
        return "JwtResponseDto{" +
                "validateToken='" + validateToken + '\'' +
                '}';
    }
}
