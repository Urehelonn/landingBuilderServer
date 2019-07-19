package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    BCryptPasswordEncoder encoder;
    //todo: no autowried?

    final
    UserRepository userRepository;

    @Value("${ucareer.jwt.expire-in-hour}")
    private Long expireHours;

    @Value("${ucareer.jwt.token}")
    private String plainSecret;

    private String encodedSecret;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

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

    public String auth(String username, String password){

        User user = userRepository.findByUsername(username).orElse(null);
        if(user != null){

            if(encoder.matches(password, user.getPassword())){
                return creatToken(user);
            }
            return null;
        }
        else{
            return null;
        }
    }

    public User register(User user){
        user.setStatus(UserStatus.Active);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    /*public User confirmEmail(String token){
        User user = userRepository.findUserByForgotPasswordToken(token).orElse(null);
        if(user != null){
            user.setStatus(UserStatus.Active);
            userRepository.save(user);
            return user;
        }
        return null;
    }*/

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

    public User verifyToken(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(plainSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            return userRepository.findByUsername(username).orElse(null);
        }
        catch (Exception e){
            return null;
        }

    }
}
