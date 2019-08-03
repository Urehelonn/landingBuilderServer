package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.web;


import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BuilderController {

    @Autowired
    UserService userService;

    @GetMapping("/me/builder")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> mineBuilder(@RequestHeader("Authorization") String authHeader) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res;
        if (token == "") {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User user = userService.getUserByToken(token);
        //todo: way 1: builderService => getBuilderByUser
        //    todo: create builderRepository => findBuilderByUser Builder obj

        // todo: modified
        if (user == null) {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        res = new CoreResponseBody(user, "get user by username", null);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    private String getJwtTokenFromHeader(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }


}
