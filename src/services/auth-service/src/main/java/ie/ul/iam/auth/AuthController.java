package ie.ul.iam.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JWT_Service jwtService;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String url = "http://localhost:8081/users/by-email/" + request.getEmail();

        try {
            ResponseEntity<ApiResponse<UserResponse>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<ApiResponse<UserResponse>>() {}
                    );

            // --- Check that user-service responded properly
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.status(401).body(
                        new ApiError(
                                "ERROR response is null or code is not 200 check AuthController",
                                Map.of("email", request.getEmail())
                        )
                );
            }

            ApiResponse<UserResponse> apiResponse = response.getBody();
            UserResponse user = apiResponse.getData();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // --- Password check
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body(
                        new ApiError(
                                "ERROR passowrd does not much, try another LOL",
                                Map.of("email", request.getEmail())
                        )
                );
            }

            // --- Generate JWT token
            String token = jwtService.generateToken(user.getEmail());

            // Return token wrapped inside JSON response
            return ResponseEntity.ok(
                    ApiResponse.success(
                            new AuthResponse(token, "Login successful")
                    )
            );

        } catch (Exception e) {

            // --- Something else went wrong (network, service down, etc.)
            return ResponseEntity.status(401).body(
                    new ApiError(
                            e.getMessage(),
                            Map.of("hint", "Probably user-service can't find the badass you're looking for")
                    )
            );
        }
    }
}
