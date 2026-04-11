package kh.edu.rupp.watchme.models;

public class SignUpRequest {
    private String userName;
    private String email;
    private String password;

    public SignUpRequest(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName(){
        return userName;
    }


}
