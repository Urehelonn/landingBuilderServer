package com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.web;


import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.Builder;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.builder.BuilderService;
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

    @Autowired
    BuilderService builderService;

    @GetMapping("/me/build")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> mineBuilder(@RequestHeader("Authorization") String authHeader) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res;
        if (token == "") {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User user = userService.getUserByToken(token);
        if (user == null) {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        System.out.println("user found: "+user.getUsername());
        Builder builder = builderService.getBuilderByUser(user);
        // re-structure on builder data

        if(builder==null){
            res = new CoreResponseBody(null, "corresponding builder not found", new Exception("Not Found"));
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        res = new CoreResponseBody(builder, "return builder data.", null);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/me/build")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> editBuilder(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Builder builder
    ) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res;
        if (token == "") {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User user = userService.getUserByToken(token);
        if (user == null) {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        Builder builderRes = builderService.save(builder, user);
        if (builderRes != null) {
            System.out.println(builderRes);
            res = new CoreResponseBody(builderRes, "Builder updated.", null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        res = new CoreResponseBody(null, "Builder input invalid", new Exception("Invalid Builder Input"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


    @GetMapping("/build/{id}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> getBuilderById(@PathVariable Long id) {
        CoreResponseBody res;
        Builder builder = builderService.getBuilderById(id);
        if (builder == null) {
            res = new CoreResponseBody(null, "No corresponding builder found with this id.", new Exception("No Builder Found"));
            System.out.println("No builder with id " + id + " found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        res = new CoreResponseBody(builder, "Get builder data.", null);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    private String getJwtTokenFromHeader(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }


}
