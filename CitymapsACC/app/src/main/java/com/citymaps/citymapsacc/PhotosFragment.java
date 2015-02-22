package com.citymaps.citymapsacc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PhotosFragment extends Fragment implements GetPhotosAsync.OnPhotosReceivedListener {

    private static final String ARG_NUM_COLUMNS = "numColumns";
    private int numColumns = 2; //default to two columns

    private PhotosListAdapter photosListAdapter;
    private int index = 0;
    private Context context;

    public PhotosFragment() {
    }

    /**
     * In this case I could have made numColumns a constant
     * Instead I chose to show an example of how I would pass arguments to a fragment.
     */
    public static PhotosFragment getInstance(int numColumns) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_NUM_COLUMNS, numColumns);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            numColumns = arguments.getInt(ARG_NUM_COLUMNS);
        }

        photosListAdapter = new PhotosListAdapter(new ArrayList<CitymapsPhoto>());
        RecyclerView photosRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
        photosRecyclerView.setLayoutManager(layoutManager);
        photosRecyclerView.setItemAnimator(new DefaultItemAnimator());
        photosRecyclerView.setAdapter(photosListAdapter);
        photosRecyclerView.setOnScrollListener(new OnPhotosScrolledListener(layoutManager));

        loadMorePhotos();

        return rootView;
    }

    void loadMorePhotos() {
        new GetPhotosAsync(this, context).execute(index);
        index += Constants.IMAGE_LOAD_LIMIT;
    }

    @Override
    public void onPhotosReceived(ArrayList<CitymapsPhoto> photos) {
        photosListAdapter.addAll(photos);
    }

    public class OnPhotosScrolledListener extends RecyclerView.OnScrollListener {
        int totalItemCount;
        final StaggeredGridLayoutManager layoutManager;

        public OnPhotosScrolledListener(StaggeredGridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            totalItemCount = layoutManager.getItemCount();
            int[] lastVisiblePositions = layoutManager.findLastCompletelyVisibleItemPositions(null);
            for (int position : lastVisiblePositions) {
                if (position >= totalItemCount - layoutManager.getSpanCount()) {
                    onScrolledToBottom();
                }
            }
        }

        public void onScrolledToBottom() {
            loadMorePhotos();
        }

    }
}