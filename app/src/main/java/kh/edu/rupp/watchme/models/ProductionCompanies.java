package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

public class ProductionCompanies {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("logo_path")
    private String logoPath;

    @SerializedName("origin_country")
    private String originCountry;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLogoPath() { return logoPath; }
    public String getOriginCountry() { return originCountry; }
}
