package kh.edu.rupp.watchme.models; // Adjust based on your folder structure

import com.google.gson.annotations.SerializedName;

public class FeedbackRequest {

    @SerializedName("user_id") // Matches the column name in image_9f21d0.png
    private String userId;

    @SerializedName("feedback") // Matches the column name in image_9f21d0.png
    private String feedback;

    public FeedbackRequest(String userId, String feedback) {
        this.userId = userId;
        this.feedback = feedback;
    }
}