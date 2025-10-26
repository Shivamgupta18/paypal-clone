package com.paypal.api_gateWay.config;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET = "secret123secret123secret123secret123secret123secret123secret123secret123";

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }


   public static Claims validateToken(String token) {
      return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
   }


}
