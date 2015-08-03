package ua.antonk.googlecustomsearch.ui.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Anton on 02.08.2015.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0;
    private boolean loading = true;
    int totalItemCount, lastVisibleItem;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = mLinearLayoutManager.getItemCount();
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (lastVisibleItem == totalItemCount - 1)) {

            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}