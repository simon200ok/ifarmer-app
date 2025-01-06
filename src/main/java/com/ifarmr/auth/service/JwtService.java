package com.ifarmr.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "MIHcAgEBBEIAi9oBwuqEzYFdh8ly8oWdsgFOsYjv+4PQUG29kXAHeFwhD4R1JGwzM2sIQ1TIV0CzvkLvKlz+ApmoklnmCst1teKgBwYFK4EEACOhgYkDgYYABABLnZ8o4KVkAfAHmOZ3AQStgqajw4kL3LoL1Njs2d2ACPIvJ157uOFj92K5QaPHJiSnesVqLscpm0yBFip75vW6tAHVSkwpuebXatT840+0WvFni0xnzONuP+gbgp0iXFEYlTcvk/+DvLebrBuwHGRCWj2Oasawz3NbGDFh+rYtArSP7A==";
    private final  Long jwtExpirationDate = 86400000L;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();


    // this key help us encrypt the token generated
    private Key key(){
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Authentication authentication, Long userId){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);


        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();

    }



    //this method help us get the name associated with the token
    public String getUserName(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return claims.getSubject();
    }

    public Long extractUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class); // Extract the userId from claims
    }


    // this method help us validate that the token belongs to the right person
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parse(token);

            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SecurityException | MalformedJwtException e) {
            throw new RuntimeException(e);
        }
    }



    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

}
