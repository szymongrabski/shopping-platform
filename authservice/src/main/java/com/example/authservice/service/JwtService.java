package com.example.authservice.service;

import com.example.authservice.domain.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private PrivateKey getPrivateKey() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/keys/private.key");
            String key = new String(inputStream.readAllBytes())
                    .replaceAll("\\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error while loading private key", e);
        }
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        extraClaims.put("roles", user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList());

        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(10, ChronoUnit.HOURS)))
                .claims(extraClaims)
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }
}