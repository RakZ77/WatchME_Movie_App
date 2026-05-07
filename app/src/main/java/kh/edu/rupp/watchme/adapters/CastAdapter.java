package kh.edu.rupp.watchme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Cast;
import kh.edu.rupp.watchme.models.Watchlist;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private List<Cast> castList;;

    public CastAdapter(List<Cast> castList){
        this.castList = castList;
    }

    public void setData(List<Cast> castList) {
        this.castList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cast_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = castList.get(position);

        holder.castName.setText(cast.getName());
        holder.character.setText(cast.getCharacter());

        if (cast.getProfilePath() != null) {
            String url = "https://image.tmdb.org/t/p/w500" + cast.getProfilePath();
            Picasso.get().load(url).into(holder.castProfile);
        } else {
            holder.castProfile.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public int getItemCount() {
        return castList == null ? 0 : castList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView castProfile;
        TextView castName, character;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           castName = itemView.findViewById(R.id.tvCastName);
           character = itemView.findViewById(R.id.tvCharacter);
           castProfile = itemView.findViewById(R.id.castProfile);
        }
    }
}