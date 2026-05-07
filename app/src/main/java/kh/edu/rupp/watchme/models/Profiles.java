package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

public class Profiles {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("avatar_url")
    private String avatar_url;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("gender")
    private String gender;
    @SerializedName("location")
    private String location;


    public Profiles(String username, String avatar_url) {
        this.username = username;
        this.avatar_url = avatar_url;
    }

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
