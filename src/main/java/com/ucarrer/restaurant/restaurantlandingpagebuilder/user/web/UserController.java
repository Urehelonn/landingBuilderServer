package com.ucarrer.restaurant.restaurantlandingpagebuilder.user.web;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody.CoreResponseBody;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.mail.MailService;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.PasswordCombination;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.User;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.UserService;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;

    //localhsot:8080/api/builderid
    @GetMapping("/builderid")
    @CrossOrigin("http://localhost:4200")
    public ResponseEntity<CoreResponseBody> getBuilderId(
            @RequestHeader("Authorization") String authHeader) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res;
        if (token == "") {
            res = new CoreResponseBody(null, "invalid token", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User currUser = userService.getUserByToken(token);
        if (currUser == null) {
            res = new CoreResponseBody(null, "Invalid token found.", new Exception("invalid token"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        Long builderId = currUser.getBuilder().getId();
        res = new CoreResponseBody(builderId, "Get builder Id succeed", null);
        return ResponseEntity.ok(res);
    }

    //localhsot:8080/api/register
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> register(@RequestBody User user) {
        //call user service to insert user
        User savedUser = userService.register(user);
        CoreResponseBody res;
        if (savedUser == null) {
            res = new CoreResponseBody(savedUser, "Sorry, user with email is already exist.", new Exception("User Already Exist."));
        } else {
            try {
                String token = userService.createToken(savedUser);
                String body = String.format(
                        "please click following link to confirm your username. <a href=\"%s/api/user/confirm/%s\">Email Confirmed</a>",
                        "http://localhost:8080", token);
//                String to, String subject, String text
                this.mailService.sendSimpleMessage(savedUser.getUsername(), "Please confirm your email!", body);
            } catch (MailException e) {
                res = new CoreResponseBody(null, "email send failed", e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            res = new CoreResponseBody(savedUser, "register succeed with state of inactive", null);
        }
        return ResponseEntity.ok(res);
    }

    //localhsot:8080/api/confirmation
    @PostMapping("/confirmation")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> resendConfirmationEmail(@RequestBody String username) {
        CoreResponseBody res;
        User savedUser = this.userService.getUserByUsername(username);
        if (savedUser != null && savedUser.getStatus() != UserStatus.Active) {
            try {
                String token = userService.createToken(savedUser);
                String body = String.format(
                        "please click following link to confirm your username. <a href=\"%s/api/user/confirm/%s\">Email Confirmed</a>",
                        "http://localhost:8080", token);
//                String to, String subject, String text
                this.mailService.sendSimpleMessage(savedUser.getUsername(), "Please confirm your email!", body);
            } catch (MailException e) {
                res = new CoreResponseBody(null, "Email send failed.", e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
            return ResponseEntity.ok(new CoreResponseBody(savedUser,
                    "Confirmation email sent again!", null));
        } else if (savedUser.getStatus() == UserStatus.Active) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CoreResponseBody(null, "User already set active.",
                    new Exception("Already actived.")));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new CoreResponseBody(null, "Cannot match this email address with existed user.",
                        new Exception("Not found")));
    }


    @GetMapping("/user/confirm/{token}")
    @CrossOrigin(origins = "http://localhsot:4200")
    public ResponseEntity<CoreResponseBody> confirmMail(
            @PathVariable String token,
            HttpServletResponse httpServletResponse)
            throws IOException {
        // validate token to see if it matches with a user who's currently inactive
        User user = this.userService.getUserByToken(token);
        // after validate succeed change user active state and stores in db (call service)
        if (user != null) {
            if (this.userService.setUserActive(user)) {
                CoreResponseBody res = new CoreResponseBody(user, "User set status to active.", null);
                httpServletResponse.sendRedirect("http://localhost:4200/login");
                return ResponseEntity.ok(res);
            }
        }
        httpServletResponse.sendRedirect("http://localhost:4200/register");
        CoreResponseBody res = new CoreResponseBody(null, "Token invalid.", new Exception("Invalid Token"));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    //write login api, return token
    @PostMapping("/login")
    //give peremission for port 4200 to access this port
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> login(@RequestBody User user) {
        CoreResponseBody res;

        //get token use method in service
        String loginToken = userService.login(user);
        if (loginToken == null) {
            res = new CoreResponseBody(null, "Username or password does not match with the record.", new Exception("Wrong password or username combination."));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        } else {
            User luser = userService.getUserByToken(loginToken);
            // ensure that user is active, if not return false
            if (luser.getStatus() == UserStatus.Active) {
                res = new CoreResponseBody(loginToken, "Login succeed.", null);
                return ResponseEntity.ok(res);
            }
            res = new CoreResponseBody(null, "Please active your account by confirming with your email.", new Exception("not active"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    // go to profile page
    @GetMapping("/me")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> me(@RequestHeader("Authorization") String authHeader) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res = new CoreResponseBody(null, "Need login to access current page.", new Exception("invalid token"));
        if (token == "") {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User user = userService.getUserByToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        res = new CoreResponseBody(user, "Login succeed and get user profile.", null);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // update user profile
    @PostMapping("/me")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> me(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User user
    ) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res = new CoreResponseBody(null, "Need login to access current page.", new Exception("invalid token"));

        if (token == "") {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User currUser = userService.getUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        User savedUser = userService.updateUser(currUser, user);
        if (savedUser != null) {
            res = new CoreResponseBody(savedUser, "User profile updated.", null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        res = new CoreResponseBody(null, "Invalid user information provided, please check the form again.", new Exception("invalid user information"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @PostMapping("/passchange")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<CoreResponseBody> passchange(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PasswordCombination password) {
        String token = this.getJwtTokenFromHeader(authHeader);
        CoreResponseBody res = new CoreResponseBody(null, "Login needed to access current page.", new Exception("invalid token"));
        if (token == "") {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
        User user = userService.getUserByToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }

        if (password == null) {
            res = new CoreResponseBody(null, "Eempty password", new Exception("No pasword given."));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        User savedUser = userService.updateUserPassword(user, password.getCurrentPassword(), password.getNewPassword());
        if (savedUser != null) {
            res = new CoreResponseBody(savedUser, "Password changed", null);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        res = new CoreResponseBody(null, "Current password does not match with the record, password change failed.", new Exception("Current password is invalid, password change failed."));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    private String getJwtTokenFromHeader(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }


}
