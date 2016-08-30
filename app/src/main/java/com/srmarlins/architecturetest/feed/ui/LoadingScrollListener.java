package com.srmarlins.architecturetest.feed.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.srmarlins.architecturetest.feed.data.PhotoManager;

/**
 * Created by JaredFowler on 8/15/2016.
 */

public abstract class LoadingScrollListener extends RecyclerView.OnScrollListener {

    private int currentPage = 1;

    private LinearLayoutManager layoutManager;


    public LoadingScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount - PhotoManager.NUM_ITEMS_PER_PAGE / 2) {
                onLoadData(++currentPage);
            }
        }
    }

    public abstract void onLoadData(int pageNumber);
}
