package com.negi.ritika.setwallpaper.classData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.negi.ritika.setwallpaper.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    public All_Images model;

    public String image, no_download;

    private ViewHolder.clickListener mClickListener;

    ImageView mImage;
    TextView downloads;
    public ViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.rImageView);
        downloads = itemView.findViewById(R.id.noDown);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx, All_Images model) {
        //TextView mtitle=mView.findViewById(R.id.rTitleTv);
        //TextView mDiscription=mView.findViewById(R.id.rDisriptionTv);
        this.model = model;
        image = model.getUrl();
        no_download = model.getDownloads();
        //mtitle.setText(title);
        //mDiscription.setText(description);
        Picasso.with(ctx).load(image).placeholder(R.drawable.download).into(mImage);
        downloads.setText(no_download);

    }

    //interface to send cllbacks
    public interface clickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.clickListener clickListener) {
        mClickListener = clickListener;
    }
}
