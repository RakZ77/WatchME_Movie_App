package kh.edu.rupp.watchme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private List<Review> reviews = new ArrayList<>();
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews != null ? reviews : new ArrayList<>();
        notifyDataSetChanged();
    }
    // Prepend new review so it appears at the top instantly
    public void addReview(Review review) {
        reviews.add(0, review);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvComment, tvDate;
        ImageView profileImage;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername  = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvDate    = itemView.findViewById(R.id.tvDate);
            profileImage = itemView.findViewById(R.id.ivAvatar);
        }

        void bind(Review review) {
            tvUsername.setText(review.getDisplayName());
            tvComment.setText(review.getComment());
            tvDate.setText(formatDate(review.getCreatedAt()));

            String avatarUrl = review.getAvatarUrl();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Picasso.get()
                        .load(avatarUrl)  // already a full URL, no prefix needed
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.profile);
            }

        }

        private String formatDate(String raw) {
            if (raw == null) return "Just now";
            try {
                SimpleDateFormat in  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat out = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = in.parse(raw);
                return out.format(date);
            } catch (ParseException e) {
                return "Just now";
            }
        }
    }

}
