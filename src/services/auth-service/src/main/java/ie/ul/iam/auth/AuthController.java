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
        String url = "http://localhost:8081/users/by-email/" + request.getEmail();

        try {
            // For now, fetch all users
            ResponseEntity<UserResponce> response =
                    restTemplate.getForEntity(url, UserResponce.class);


            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                // Password check TODO
                String token = jwtService.generateToken(request.getEmail());
                return ResponseEntity.ok(token);
            }

            return ResponseEntity.status(401).body("Invalid credentials");

        } catch (Exception e) {
            // TODO tmp 404.. etc as 401
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
