package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.mail.MailService;
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

    @Autowired
    MailService mailService;

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
            String token = userService.createToken(savedUser);
            String body = String.format("please use this link to confirm your username, %s/api/user/confirm/%s", "http://localhost:8080", token);

            try{
                mailService.sendSimpleMessage(user.getUsername(), "Confirm your email", body);
                response = new CoreResponseBody(savedUser, "please confirm your email", null);
                return ResponseEntity.ok(response);
            }
            catch (Exception e){
                response = new CoreResponseBody(null, "please confirm your email", e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        }
    }




    @GetMapping("/user/confirm/{token}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> confirmEmail(@PathVariable String token){
        CoreResponseBody response;
        if(token.trim() == ""){
            response = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User foundOne = userService.confirmByToken(token);
        if(foundOne == null){
            response = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        response = new CoreResponseBody(token, "", null);
        return ResponseEntity.ok(response);
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

    @GetMapping("/me")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> me(@RequestHeader("Authorization") String authHeader){
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody response;
        if(token == ""){
            response = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        User user = userService.getUserByToken(token);
        if(user == null){
            response = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
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
    ){
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody response;
        if(token == ""){
            response = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User currentUser = userService.getUserByToken(token);
        if(currentUser == null){
            response = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User updatedUser = userService.updateProfile(user, currentUser);
        response = new CoreResponseBody(updatedUser, "profile update completed", null);
        return ResponseEntity.ok(response);
    }

    private String getJwtTokenFromHeader(String authHeader){
        return authHeader.replace("Bearer ","").trim();
    }


}
