package com.omkokate.colorcodetask;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.omkokate.colorcodetask.BannerItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<BannerItem> bannerItemList;

    public BannerAdapter(List<BannerItem> bannerItemList) {
        this.bannerItemList = bannerItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BannerItem bannerItem = bannerItemList.get(position);

        // Load image using Picasso or Glide
        Log.d("tag2",bannerItem.getPicture());
        Picasso.get().load(bannerItem.getPicture()).into(holder.imageView);

        // You can handle item click events here if needed
    }

    @Override
    public int getItemCount() {
        return bannerItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bannerImageView);
        }
    }
}
