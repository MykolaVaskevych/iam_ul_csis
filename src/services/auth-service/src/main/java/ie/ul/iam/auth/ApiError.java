package ie.ul.iam.auth;

import java.util.Map;

public class ApiError {

    private String status = "error";
    private String message;
    private Map<String, Object> details;

    public ApiError() {}

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(String message, Map<String, Object> details) {
        this.message = message;
        this.details = details;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Map<String, Object> getDetails() { return details; }
}
