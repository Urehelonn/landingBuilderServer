package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.enums.UserStatus;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("UserService")
public class UserService {

    @Autowired
    UserRepository repository;

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
        User newUser = new User();

        newUser.setPassword(user.getPassword());
        newUser.setUsername(user.getUsername());
        newUser.setStatus(UserStatus.Inactive);

        User savedUser = repository.save(newUser);
        return savedUser;
    }

    public String login(User user){
        User loginUser = (User)repository.findByUsernameAndPassword(user.getUsername(),
                user.getPassword()).orElse(null);

        if(loginUser!=null){
            return createToken(loginUser);
        }
        else{
            return null;
        }
    }

    public String createToken(User user){
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
