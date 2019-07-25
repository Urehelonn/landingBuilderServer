package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.enums.UserStatus;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.utils.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import com.ucarrer.restaurant.restaurantlandingpagebuilder.utils.*;

@Service("UserService")
public class UserService {

    BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepository;


    @Value("${ucareer.jwt.expire-in-hour}")
    private Long expireHours;

    @Value("${ucareer.jwt.token}")
    private String plainSecret;

    @Value("${ucareer.jwt.forgot}")
    private String plainForgotPasswordSecret;

    @Value("${ucareer.jwt.forgot-expire-in-hour}")
    private Long forgotPasswordTokenExpireHour;

    private String encodedSecret;

    public User register(User user){
        User foundOne = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(foundOne == null){
            // create new user
            User newUser = new User();
            newUser.setPassword(encoder.encode(user.getPassword()));
            newUser.setUsername(user.getUsername());
            newUser.setStatus(UserStatus.Inactive);
            User savedUser = userRepository.save(newUser);
            return savedUser;
        }else{
            return null;
        }

    }

    public String login(User user){
        User loginUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(loginUser!=null){
            boolean matched = encoder.matches(user.getPassword(),loginUser.getPassword());
            if(matched){
                return createToken(loginUser);
            }else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    public User updateProfile(User user, User currentUser){
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setAddress(user.getAddress());


        if(user.getPassword() != null){
            currentUser.setPassword(encoder.encode(user.getPassword()));
        }
        userRepository.save(currentUser);
        return currentUser;
    }



    public String createToken(User user) {
        JwtTokenHelper jwtHelper = new JwtTokenHelper(plainSecret, expireHours);
        return jwtHelper.creatToken(user);
    }

    public String createForgotPasswordToken(User user) {
        JwtTokenHelper jwtHelper = new JwtTokenHelper(plainForgotPasswordSecret, forgotPasswordTokenExpireHour);
        return jwtHelper.creatToken(user);
    }

    public User getUserByToken(String token) {
        JwtTokenHelper jwtHelper = new JwtTokenHelper(plainSecret, expireHours);
        return getUserByToken(jwtHelper, token);
    }

    public User verifyForgotPasswordToken(String token) {
        JwtTokenHelper jwtHelper = new JwtTokenHelper(plainForgotPasswordSecret, forgotPasswordTokenExpireHour);
        return getUserByToken(jwtHelper, token);
    }

    private User getUserByToken(JwtTokenHelper helper, String token){
        String username = helper.verifyToken(token);
        if(username == null){
            return null;
        }
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            return null;
        }
        return user;
    }

    public User resetPassword(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
}
