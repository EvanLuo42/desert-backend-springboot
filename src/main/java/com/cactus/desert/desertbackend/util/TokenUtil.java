package com.cactus.desert.desertbackend.util;

import com.cactus.desert.desertbackend.dto.TokenDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author EvanLuo42
 * @date 4/29/22 8:34 AM
 */
public class TokenUtil {

    @Value("${token.secret}")
    private static String secret;

    @Value("${token.expiration}")
    private static Long expiration;

    public static String generateToken(TokenDetail tokenDetail) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("playerName", tokenDetail.getPlayerName());
        claims.put("role", tokenDetail.isAdmin());
        claims.put("created", TokenUtil.generateCurrentDate());
        return TokenUtil.generateToken(claims);
    }

    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(TokenUtil.generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    private static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    private static Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static String getPlayerNameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get("playerName");
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public static String getRoleFromToken(String token) {
        String role;
        try {
            final Claims claims = getClaimsFromToken(token);
            role = (String) claims.get("role");
        } catch (Exception e) {
            role = null;
        }
        return role;
    }

    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
