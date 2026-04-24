package kh.edu.rupp.watchme.models;

public class UpdateProfileRequest {
    private String username;
    private String avatar_url;
    private String birthday;
    private String gender;
    private String location;

    public UpdateProfileRequest(String username, String avatar_url,
                                String birthday, String gender, String location) {
        this.username = username;
        this.avatar_url = avatar_url;
        this.birthday = birthday;
        this.gender = gender;
        this.location = location;
    }
}

