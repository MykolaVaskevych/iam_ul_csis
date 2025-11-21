package ie.ul.iam.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JWT_Service jwtService;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Call User-Service to verify the user exists
        String url = "http://localhost:8081/users"; // TODO:  replace with service discovery

        try {
            // For now, fetch all users
            ResponseEntity<Object[]> response =
                    restTemplate.getForEntity(url, Object[].class);

            boolean found = false;
//            assert response.getBody() != null;
            for (Object obj : response.getBody()) {
                String value = obj.toString();
                if (value.contains(request.getEmail())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            // No password check yet — TODO: add proper BCrypt
            String token = jwtService.generateToken(request.getEmail());

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Auth service error");
        }
    }
}
