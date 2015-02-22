package com.citymaps.citymapsacc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.ViewHolder> {
    private final ArrayList<CitymapsPhoto> photos;

    public PhotosListAdapter(ArrayList<CitymapsPhoto> photos) {
        this.photos = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.photo_view, parent, false);
        CitymapsImageView imageView = (CitymapsImageView) rootView.findViewById(R.id.iv_photo);
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return new ViewHolder(rootView, imageView, progressBar);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String uriString = photos.get(position).getImageUrl();
        holder.iv_photo.setImageUrl(uriString, holder.progressBar, position);
    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    public void addAll(ArrayList<CitymapsPhoto> newPhotos) {
        int startSize = photos.size();
        photos.addAll(newPhotos);
        this.notifyItemRangeInserted(startSize, newPhotos.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CitymapsImageView iv_photo;
        public final ProgressBar progressBar;

        public ViewHolder(View rootView, CitymapsImageView imageView, ProgressBar progressBar) {
            super(rootView);
            this.iv_photo = imageView;
            this.progressBar = progressBar;
        }
    }


}
