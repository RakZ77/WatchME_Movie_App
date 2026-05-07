package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsResponse {
    @SerializedName("cast")
    private List<Cast> cast;

    @SerializedName("crew")
    private List<CrewMember> crew;

    public List<CrewMember> getCrew() { return crew; }
    public List<Cast> getCast() { return cast; }
}
