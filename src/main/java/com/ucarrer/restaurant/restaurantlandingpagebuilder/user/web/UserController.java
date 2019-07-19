package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.register(user);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = null;
        token = userService.auth(request.getUsername(), request.getPassword());

        if (token != null) {
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/me")
    public ResponseEntity<User> me(@RequestBody SelfGetRequest request) {
        String token = request.getToken();
        User user = userService.verifyToken(token);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        return ResponseEntity.ok(email);
    }

   /* @GetMapping("/confirm-email/{token}")
    public ResponseEntity<User> confirmEmail(@PathVariable String token) throws URISyntaxException {
        User user = userService.confirmEmail(token);
        return ResponseEntity.ok(user);
    }*/


}