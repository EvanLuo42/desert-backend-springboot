package com.cactus.desert.desertbackend.util;

import com.cactus.desert.desertbackend.dto.TokenDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author EvanLuo42
 * @date 4/29/22 8:34 AM
 */
@Component
public class TokenUtil {

    private final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private Long expiration;

    public String generateToken(TokenDetail tokenDetail) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("playerName", tokenDetail.getPlayerName());
        claims.put("role", tokenDetail.isAdmin());
        claims.put("created", this.generateCurrentDate());
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        logger.debug("Generated token for {}", claims.get("playerName"));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(this.generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public Optional<String> getPlayerNameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get("playerName");
            logger.debug("Get playerName successfully.");
        } catch (Exception e) {
            username = null;
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(username);
    }

    public String getRoleFromToken(String token) {
        String role;
        try {
            final Claims claims = getClaimsFromToken(token);
            role = (String) claims.get("role");
        } catch (Exception e) {
            role = null;
        }
        return role;
    }

    private Claims getClaimsFromToken(String token) {
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
