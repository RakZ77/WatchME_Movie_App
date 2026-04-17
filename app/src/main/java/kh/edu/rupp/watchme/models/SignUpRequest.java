package kh.edu.rupp.watchme.models;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest {
    private Map<String, String> data;
    private String email;
    private String password;

    public SignUpRequest(String userName, String email, String password) {
        this.email = email;
        this.password = password;

        data = new HashMap<>();
        data.put("username", userName);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, String> getData() { return data; }


}
