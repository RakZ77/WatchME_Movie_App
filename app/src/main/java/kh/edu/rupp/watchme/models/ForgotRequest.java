package kh.edu.rupp.watchme.models;

public class ForgotRequest {

    private String email;

    public ForgotRequest(String email) {
        this.email = email;
    }

    // Getter
    public String getEmail() {
        return email;
    }

    // Setter
    public void setEmail(String email) {
        this.email = email;
    }
}
