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

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);


        return Jwts.builder()
                .setSubject(username)
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

//    public String getSecretKey() {
//        return SECRET_KEY;
//    }
//
//
//    //Extract all claims
//    private Claims extractAllClaims(String token){
//        return Jwts
//                .parser()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    //Extract Single Claims
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
//        final Claims claims = extractAllClaims(token);
//
//        return  claimsResolver.apply(claims);
//    }
//
//    //Extract Username
//    public  String extractUsername(String token){
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    //Generate token for claims and user details
//    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){
//        return Jwts
//                .builder()
//                .setClaims(extractClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *24))
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String generateToken(UserDetails userDetails){
//        return (generateToken(new HashMap<>(), userDetails));
//    }
//
//    //check if token is valid
//    public  Boolean isTokenValid(String token, UserDetails userDetails){
//        final  String userName = extractUsername(token);
//
//        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public void blacklistToken(String token) {
//        blacklistedTokens.add(token);
//    }
//
//    public boolean isBlacklisted(String token) {
//        return blacklistedTokens.contains(token);
//    }

}
