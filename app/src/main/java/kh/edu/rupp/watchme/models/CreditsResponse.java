package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsResponse {
    @SerializedName("cast")
    private List<CrewMember> cast;

    @SerializedName("crew")
    private List<CrewMember> crew;

    public List<CrewMember> getCrew() { return crew; }
    public List<CrewMember> getCast() { return cast; }
}
