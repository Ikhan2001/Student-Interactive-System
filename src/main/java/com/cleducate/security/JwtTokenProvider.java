package com.cleducate.security;

import com.cleducate.entity.User;
import com.cleducate.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.header.name}")
    private String header;
    @Value("${jwt.auth.token.expire.milliseconds}")
    private long authTokenValidityInMilliseconds;

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtTokenProvider(UserRepository userRepository,
                            UserDetailsServiceImpl userDetailsService){
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateAuthToken(User user){

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userId", user.getId());
        claims.put("userName", user.getFirstName()+" "+user.getLastName());

        Date currentDate = new Date();
        Date validity = new Date(currentDate.getTime() + authTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generatePasswordResetToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userId", user.getId());
        claims.put("userName", user.getFirstName()+" "+user.getLastName());
        Date currentDate = new Date();
        Date validity = new Date(currentDate.getTime() + 1800000); // 30 minutes expiration (30 * 60 * 1000)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userId").toString();
    }

    public String getUserName(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token){
        Jws<Claims> claim = resolveClaims(token);
        return (!claim.getBody().getExpiration().before(new Date()) &&
                validateUser(claim.getBody().get("userId").toString()));
    }

    public boolean validateResetToken(String token){
        Jws<Claims> claim =  resolveClaims(token);
        return (!claim.getBody().getExpiration().before(new Date()) && validateUser(claim.getBody().get("userId").toString()));
    }

    public boolean validateUser(String userId){
        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        if(!user.isPresent()){
            throw new UsernameNotFoundException("User Not found with userId: " + userId);
        }
        return user.get().isActive() && !user.get().isDeleted();
    }

    public Jws<Claims> resolveClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public String getEmailFromToken(String token){
        return resolveClaims(token).getBody().getSubject();
    }

}
