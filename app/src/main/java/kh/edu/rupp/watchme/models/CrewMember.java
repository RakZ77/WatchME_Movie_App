package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

public class CrewMember {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("job")
    private String job;

    @SerializedName("department")
    private String department;

    public String getName() { return name; }
    public String getJob() { return job; }
}
