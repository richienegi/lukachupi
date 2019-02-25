package com.negi.wallpapers.setwallpaper;

import android.view.View;

public interface ClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}
