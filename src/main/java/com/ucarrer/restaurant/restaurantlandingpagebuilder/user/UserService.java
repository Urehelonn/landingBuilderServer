package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {


    UserRepository userRepository;

    public User register(User user){

        return userRepository.save(user);
    }
}
