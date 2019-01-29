package com.negi.ritika.setwallpaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.negi.ritika.setwallpaper.classData.All_Images;
import com.negi.ritika.setwallpaper.classData.ViewHolder;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context c;
    LayoutInflater li;
    List<All_Images> data;

    public PostListAdapter(Context c, List<All_Images> data) {
        this.c=c;
        li = LayoutInflater.from(c);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = li.inflate(R.layout.row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetails(c, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
