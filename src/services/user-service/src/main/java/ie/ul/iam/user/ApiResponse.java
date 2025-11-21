package ie.ul.iam.user;

public class ApiResponse<T> {
    private String status;
    private T data;

    public ApiResponse() {}

    public ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data);
    }

    public String getStatus() { return status; }
    public T getData() { return data; }
}
