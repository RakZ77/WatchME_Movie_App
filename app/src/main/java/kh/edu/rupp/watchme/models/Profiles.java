package kh.edu.rupp.watchme.models;

public class Profiles {
    private String id;
    private String username;
    private String avatar_url;
    private String birthday;
    private String gender;
    private String location;

    // Getter methods for the fields

    public String getUsername() {
        return username;
    }

    public String getAvatar_url(){
        return avatar_url;
    }

    public String getBirthday(){
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation(){
        return location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    // Add more getter and setter methods for other fields as needed)
}
