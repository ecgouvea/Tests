package tests.email.hotmail;

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GenerateJwtToken {
    public static void main(String[] args) {
        String clientId = "<clientId>";
        String clientSecret = "<clientSecret>";
        String tenantId = "<tenantId>";

        String accessToken = generateAccessToken(clientId, clientSecret, tenantId);

        System.out.println("Generated Access Token: " + accessToken);
    }

    private static String generateAccessToken(String clientId, String clientSecret, String tenantId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", clientId);
        claims.put("sub", clientId);
        claims.put("aud", "https://graph.microsoft.com");
        claims.put("exp", new Date(nowMillis + 3600000)); // 1 hour validity
        claims.put("iat", now);

        return null;
//                Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, clientSecret)
//                .compact();
    }
}


/*
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class GenerateJwtToken {
    public static void main(String[] args) {
        String clientId = "<clientId>";
        String clientSecret = "<clientSecret>";

        String token = Jwts.builder()
                .setSubject(clientId)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour validity
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        System.out.println("Generated JWT token: " + token);
    }
}
*/