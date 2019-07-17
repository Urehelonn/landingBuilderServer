package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Long id = userService.register(user);
        return ResponseEntity.ok(id.toString());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = null;
        token = userService.auth(request.getUsername(), request.getPassword());
        if ( token != null ) {
            return ResponseEntity.ok(token);
        }
        else{
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/forgot-password/:email")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) {
        return ResponseEntity.ok(email);
    }

    @GetMapping("/confirm-email/:token")
    public ResponseEntity<?> confirmEmail(@PathVariable String token) {
        User user = userService.confirmEmail(token);
        return ResponseEntity.ok(user.getId());
    }







}