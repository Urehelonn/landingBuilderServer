package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    PasswordEncoder encoder;

    UserRepository userRepository;

    public Boolean auth(String username, String password){
        User user = userRepository.findByUsername(username).orElse(null);
        if(user != null){
            if(user.getPassword() == password){
                return true;
            }
            return false;
        }
        else{
            return false;
        }
    }

    public Long register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    public User confirmEmail(String token){
        User user = userRepository.findUserByForgotPasswordToken(token).orElse(null);
        if(user != null){
            user.setStatus(UserStatus.Active);
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
