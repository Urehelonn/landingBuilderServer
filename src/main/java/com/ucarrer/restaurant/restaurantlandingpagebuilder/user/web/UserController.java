package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
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
    @CrossOrigin("http:/.localhost:4200")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello world");
    }

    //localhsot:8080/api/register
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> register(@RequestBody User user) {

        //call user service to insert user
        User savedUser = userService.register(user);
        CoreResponseBody res;
        if (savedUser == null) {
            res = new CoreResponseBody(savedUser, "User already exist.", new Exception("User Already Exist."));
        } else {
            res = new CoreResponseBody(savedUser, "get user msg", null);
        }
        return ResponseEntity.ok(res);
    }

    //write login api, return token
    @PostMapping("/login")
    //give peremission for port 4200 to access this port
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> login(@RequestBody User user) {
        //get token use method in service
        String loginToken = userService.login(user);
        CoreResponseBody res;

        if (loginToken == null) {
            res = new CoreResponseBody(null, "Username or password does not match with the record.", new Exception("Wrong password or username combination."));
//            return ResponseEntity.notFound();
        } else {
            res = new CoreResponseBody(loginToken, "get user msg", null);
        }
        return ResponseEntity.ok(res);
    }

}
