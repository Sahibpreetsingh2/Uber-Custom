package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.entities.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${Secret.key}")
    private String secretKey;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Users users){

        return Jwts.builder()
                .setSubject(users.getId().toString())
                .claim("email", users.getUsername())
                .claim("roles", users.getRoles().toString())
                .signWith(getSecretKey() , SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // 1 min
                .compact();
    }

    public Long generateUserIdFromToken(String token){

        String userId = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(userId);

    }


    public String generateAccessToken(Users users){

        return Jwts.builder()
                .setSubject(users.getId().toString())
                .claim("email", users.getUsername())
                .claim("roles", users.getRoles().toString())
                .signWith(getSecretKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 1 min
                .compact();
    }

    public String generateRefreshToken(Users users){

        return Jwts.builder()
                .setSubject(users.getId().toString())
                .signWith(getSecretKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 2 min
                .compact();
    }
//    public long getRemainingExpiration(String token) {
//
//        Date expiration = extractExpiration(token);
//
//        return expiration.getTime() - System.currentTimeMillis();
//    }ml'
}
