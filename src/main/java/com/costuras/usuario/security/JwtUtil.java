package com.costuras.usuario.security;


import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.costuras.usuario.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {
      @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaims(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

   public User getUserFromToken(String token) {
    Claims claims = getAllClaims(token);
    Integer id = null;
    Object idClaim = claims.get("id");
    if (idClaim instanceof Integer) {
        id = (Integer) idClaim;
    } else if (idClaim instanceof Long) {
        id = ((Long) idClaim).intValue();
    }
    return User.builder()
            .id(id)
            .username(claims.getSubject())
            .role(claims.get("role", String.class))  // ← AGREGAR esto
            .build();
}
}
   

  

