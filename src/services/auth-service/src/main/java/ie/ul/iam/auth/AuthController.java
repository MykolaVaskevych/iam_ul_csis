package ie.ul.iam.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
            ResponseEntity<UserResponce> response = restTemplate.getForEntity(url, UserResponce.class);

            if(!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
                return ResponseEntity.status(401).body("ERROR response is null or code is not 200 check AuthController");
            }

            UserResponce user = response.getBody();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // check pass
            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("ERROR passowrd does not much, try another LOL");
            }

            // if pass ok get for user jwt token
            String token = jwtService.generateToken(user.getEmail());

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
