package com.learning.ecommerce.dto;

public class JwtResponse {

    private UserDto userDto;
    private String jwtToken;

    public JwtResponse(UserDto userDto, String jwtToken) {
        this.userDto = userDto;
        this.jwtToken = jwtToken;
    }

    public UserDto getUser() {
        return userDto;
    }

    public void setUser(UserDto user) {
        this.userDto = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
