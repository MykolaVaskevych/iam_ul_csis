package ie.ul.iam.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class JWT_Service {

    // Temporary hardcoded secret key for dev (we replace this later)
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); <-- deprecated

    // Development key — exactly 32 bytes for HS256
    private final Key key = Keys.hmacShaKeyFor(
            "super-secret-development-key-123456".getBytes()
    );


    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        long expiry = 1000 * 60 * 60; // 1 hour

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiry))
                .signWith(key)
                .compact();
    }

//    public static void main(String[] args) {
//        JWT_Service jwtService = new JWT_Service();
//        System.out.println(key.toString());
//        String token = jwtService.generateToken("admin");
//        System.out.println(token);
//    }
}


