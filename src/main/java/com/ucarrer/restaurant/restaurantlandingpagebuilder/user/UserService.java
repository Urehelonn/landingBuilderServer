package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;
import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Service
public class UserService {


    PasswordEncoder encoder = new
            BCryptPasswordEncoder();

    UserRepository userRepository;

    @Value("${ucareer.jwt.expire-in-hour}")
    private Long expireHours;

    @Value("${ucareer.jwt.token}")
    private String plainSecret;

    private String encodedSecret;

    @PostConstruct
    protected void init() {
        this.encodedSecret = generateEncodedSecret(this.plainSecret);
    }

    private String generateEncodedSecret(String plainSecret) {
        if (StringUtils.isEmpty(plainSecret))
        {
            throw new IllegalArgumentException("JWT secret cannot be null or empty.");
        }
        return Base64
                .getEncoder()
                .encodeToString(this.plainSecret.getBytes());
    }



    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }


    public String creatToken(User user){
        Date now = new Date();
        Long expireInMilis = TimeUnit.HOURS.toMillis(expireHours);
        Date expiredAt = new Date(expireInMilis + now.getTime());

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS512, encodedSecret)
                .compact();
    }
}