package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api")
//localhost:8080/api/hello
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> register(@RequestBody User user) {
        User registerUser = userService.register(user);
        CoreResponseBody response;
        // if registerUser return null, means fail to register
        if (registerUser == null) {
            response = new CoreResponseBody(null, "Sorry, user already exists", new Exception("Sorry, user already exists"));
            //response = new CoreResponseBody(null, "Sorry, user already exists", null);

            return ResponseEntity.ok(response);

        } else {
            response = new CoreResponseBody(registerUser, "Registered successfully", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> login(@RequestBody User user) {
        User currentUser = userService.login(user);
        CoreResponseBody response;
        if (currentUser == null) {
            response = new CoreResponseBody(null, "Invalid Login. The username/email and password you entered did not match our records.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            String token = userService.creatToken(currentUser);
            response = new CoreResponseBody(token, "Login successfully", null);
            return ResponseEntity.ok(response);

        }

    }

    @GetMapping("/me")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> me(@RequestHeader("Authorization") String authHeader) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody response;
        if (token == "") {
            response = new CoreResponseBody(null, "Invalid token", new Exception("Invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userService.getUserByToken(token);
        if (user == null) {
            response = new CoreResponseBody(null, "Invalid token", new Exception("Invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response = new CoreResponseBody(user, "", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> me(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User user
    ) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody response;
        if (token == "") {
            response = new CoreResponseBody(null, "Invalid token", new Exception("Invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User currentUser = userService.getUserByToken(token);
        if (currentUser == null) {
            response = new CoreResponseBody(null, "Invalid token", new Exception("Invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User updateUser = userService.updateProfile(user, currentUser);
        response = new CoreResponseBody(updateUser, "Profile updated.", null);
        return ResponseEntity.ok(response);

    }

    private String getJwtTokenFromHeader(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }

}
