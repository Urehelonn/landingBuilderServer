package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    //localhsot:8080/api/hello
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello world");
    }

    //localhsot:8080/api/register
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> register(@RequestBody User user) {
        //call user service to insert user
        User savedUser = userService.register(user);
        CoreResponseBody response;
        if (savedUser == null) {
            response = new CoreResponseBody(null, "user already exist", new Exception("user already exist"));
            return ResponseEntity.ok(response);

        } else {
            response = new CoreResponseBody(savedUser, "registered successfully", null);
            return ResponseEntity.ok(response);
        }
    }


    //write login api, return token
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> login(@RequestBody User user) {
        //get token use method in service
        String loginToken = userService.login(user);
        CoreResponseBody response;
        if (loginToken == null) {
            response = new CoreResponseBody(null, "user and password", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }else{
            response = new CoreResponseBody(loginToken, "", null);
            return ResponseEntity.ok(response);
        }
    }

}
