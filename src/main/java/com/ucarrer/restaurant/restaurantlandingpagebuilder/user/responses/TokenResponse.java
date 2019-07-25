package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.responses;

public class TokenResponse {
    private String token;
    private String type = "Bearer";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
