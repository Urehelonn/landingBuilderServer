package com.ucarrer.restaurant.restaurantlandingpagebuilder.user;

import com.ucarrer.restaurant.restaurantlandingpagebuilder.user.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("UserService")
public class UserService {

    BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder();

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
        User foundOne = repository.findByUsername(user.getUsername()).orElse(null);
        if(foundOne == null){
            // create new user
            User newUser = new User();
            newUser.setPassword(encoder.encode(user.getPassword()));
            newUser.setUsername(user.getUsername());
            newUser.setStatus(UserStatus.Inactive);
            User savedUser = repository.save(newUser);
            return savedUser;
        }else{
            return null;
        }

    }

    public String login(User user){
        User loginUser = (User)repository.findByUsername(user.getUsername()).orElse(null);
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
        if(user.getFirstName() != null){
            currentUser.setFirstName(user.getFirstName());
        }
        if(user.getLastName() != null){
            currentUser.setLastName(user.getLastName());
        }
        if(user.getPhone() != null){
            currentUser.setPhone(user.getPhone());
        }
        if(user.getDescription() != null){
            currentUser.setDescription(user.getDescription());
        }
        if(user.getAddress() != null){
            currentUser.setAddress(user.getAddress());
        }
        if(user.getPassword() != null && user.getPassword().trim() != ""){
            currentUser.setPassword(encoder.encode(user.getPassword()));
        }
        User savedUser = repository.save(currentUser);
        return savedUser;
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

    public User getUserByToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(encodedSecret)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            User user = repository.findByUsername(username).orElse(null);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public User confirmByToken(String token){
        try {
            User foundUser = this.getUserByToken(token);
            if(foundUser == null){
                return null;
            }
            else{
                foundUser.setStatus(UserStatus.Active);
                repository.save(foundUser);
                return foundUser;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
