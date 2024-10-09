package org.acme.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.acme.demo.entity.Role;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class JwtUtil {
    private static final String PRIVATE_KEY_PATH = "private_key.pem"; // Path to your private key
    private static final String PUBLIC_KEY_PATH = "public_key.pem"; // Path to your public key

    private PublicKey getPublicSigningKey() {
        try {
            String publicKeyPEM = new String(Files.readAllBytes(Paths.get(PUBLIC_KEY_PATH)))
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read public key file", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    private PrivateKey getPrivateSigningKey() {
        try {
            String privateKeyPEM = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_PATH)))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read private key file", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }



    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getPublicSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); // Returns claims inside jwt token
    }

    public String extractUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    //Check expiration
    public boolean isTokenExpired(String token){
        Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    //Validate expiration
    public boolean validateToken(String token, String username){
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    public String generateToken(String username, Set<Role> roles, long expirationMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // Custom expiration time
                .signWith(getPrivateSigningKey())
                .compact();
    }
}
