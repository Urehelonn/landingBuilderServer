package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<User> register(@RequestBody User user) {
        //call user service to insert user
        User savedUser = userService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    //write login api, return token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        //get token use method in service
        String loginToken = userService.login(user);
        //TODO add 404 option instead of null
        if(loginToken==null){
           // return ResponseEntity.notFound();
        }
        return ResponseEntity.ok(loginToken);
    }

}
