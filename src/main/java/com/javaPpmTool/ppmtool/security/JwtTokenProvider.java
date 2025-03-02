package com.javaPpmTool.ppmtool.security;

import com.javaPpmTool.ppmtool.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.javaPpmTool.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.javaPpmTool.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

//    // Use secret key from application.properties
//    @Value("${app.jwt-secret}")
//    private String jwtSecret;

    // Generate the token when we have a valid username and password
    public String generateToken(Authentication authentication){
        // The identity of the principal being authenticated.
        // In the case of an authentication request with username and password, this would be the username.
        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        String token = Jwts.builder()
                .setSubject(userId) // Sets the "sub" (subject) claim in a JWT token
                .setClaims(claims) // Sets custom claims (payload)
                .setIssuedAt(now) // Token issue time
                .setExpiration(expiryDate)
                .signWith(key())
                .compact(); // Generates the final token as a String

        return token;
    }

    // decode the key and will return the key
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(SECRET)
        );
    }

    // Validate the token
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(key())
                    .parseClaimsJws(token);

            return true;
        }catch (SignatureException ex){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            System.out.println("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT claims string is empty");
        }

        return false;
    }


    // Get user Id from token
    // We want to extract the user whose userId is in the claims we put claims.put("id", (Long.toString(user.getId())));
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .parseClaimsJws(token)
                .getBody();

        String id = (String)claims.get("id");

        // We need long to getUserId from repository
        return Long.parseLong(id);
    }
}
