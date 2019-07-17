package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

public class LoginResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}
