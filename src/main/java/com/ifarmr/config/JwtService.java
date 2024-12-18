package com.ifarmr.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY = "MIHcAgEBBEIAi9oBwuqEzYFdh8ly8oWdsgFOsYjv+4PQUG29kXAHeFwhD4R1JGwzM2sIQ1TIV0CzvkLvKlz+ApmoklnmCst1teKgBwYFK4EEACOhgYkDgYYABABLnZ8o4KVkAfAHmOZ3AQStgqajw4kL3LoL1Njs2d2ACPIvJ157uOFj92K5QaPHJiSnesVqLscpm0yBFip75vW6tAHVSkwpuebXatT840+0WvFni0xnzONuP+gbgp0iXFEYlTcvk/+DvLebrBuwHGRCWj2Oasawz3NbGDFh+rYtArSP7A==";

//    public JwtService(@Value("${SECRET_KEY}") String SECRET_KEY) {
//        this.SECRET_KEY = SECRET_KEY;
//    }

    public String getSecretKey() {
        return SECRET_KEY;
    }


    //Extract all claims
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Extract Single Claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);

        return  claimsResolver.apply(claims);
    }

    //Extract Username
    public  String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Generate token for claims and user details
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return (generateToken(new HashMap<>(), userDetails));
    }

    //check if token is valid
    public  Boolean isTokenValid(String token, UserDetails userDetails){
        final  String userName = extractUsername(token);

        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
