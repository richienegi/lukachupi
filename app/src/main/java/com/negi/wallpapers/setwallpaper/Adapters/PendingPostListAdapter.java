package com.negi.wallpapers.setwallpaper.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.negi.wallpapers.setwallpaper.GlideImageLoader;
import com.negi.wallpapers.setwallpaper.Models.User_Images;
import com.negi.wallpapers.setwallpaper.R;

import java.util.List;

public class PendingPostListAdapter extends RecyclerView.Adapter<PendingPostListAdapter.ViewHolder> {

    Context c;
    LayoutInflater li;
    List<User_Images> data;
    FragmentManager fm;

    PendingClickListener listener;

    public PendingPostListAdapter(Context c, List<User_Images> data, FragmentManager fm) {
        this.c = c;
        li = LayoutInflater.from(c);
        this.data = data;
        this.fm = fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = li.inflate(R.layout.pending, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User_Images info = data.get(position);
        holder.setData(position, info);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .priority(Priority.HIGH);

        new GlideImageLoader(holder.mImage, holder.pb).load(info.getThumb(),options);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public User_Images model;
        int position;
        ImageView mImage;
        ProgressBar pb;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.rImageView);
            pb = itemView.findViewById(R.id.progress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPendingItemClick(v, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onPendingItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }

        public void setData(int position, User_Images model) {
            this.position = position;
            this.model = model;
        }
    }

    public void setListener(PendingClickListener listener) {
        this.listener = listener;
    }

    public interface PendingClickListener {
        void onPendingItemClick(View v, int position);

        void onPendingItemLongClick(View v, int position);
    }

}
