package ua.antonk.googlecustomsearch.ui.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.antonk.googlecustomsearch.Constants;
import ua.antonk.googlecustomsearch.R;
import ua.antonk.googlecustomsearch.database.DatabaseOpenHelper;
import ua.antonk.googlecustomsearch.loaders.DatabaseLoader;
import ua.antonk.googlecustomsearch.ui.adapters.FavoritesListAdapter;
import ua.antonk.googlecustomsearch.ui.util.DividerItemDecoration;
import ua.antonk.googlecustomsearch.ui.util.EndlessRecyclerOnScrollListener;

public class FavoritesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FavoritesListAdapter.Callbacks {

    private static final int FAVORITES_THRESHOLD_COUNT = 10;
    private static final int LOADER_ID = 2;

    private RecyclerView mFavorites;
    private LinearLayoutManager mLayoutManager;
    private FavoritesListAdapter mAdapter;
    private TextView mEmptyView;


    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();

        return fragment;
    }

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mFavorites = (RecyclerView) view.findViewById(R.id.list_favorites);
        mFavorites.setLayoutManager(mLayoutManager);
        mFavorites.setAdapter(mAdapter);
        mFavorites.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadData();
            }
        });
        mFavorites.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));

        mEmptyView = (TextView) view.findViewById(R.id.empty_view);


        checkForEmptyList();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new FavoritesListAdapter(null, this);
        loadData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int startIndex = args.getInt(Constants.BUNDLE_START_INDEX);
        int count = args.getInt(Constants.BUNDLE_COUNT);

        return new DatabaseLoader(
                getActivity(),
                DatabaseOpenHelper.getInstance(getActivity()),
                startIndex,
                count);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null){
            updateAdapter(data);
        }
        loader.stopLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void updateAdapter(Cursor cursor){
        mAdapter.setData(cursor);
        checkForEmptyList();
    }

    public void loadData(){
        Bundle args = new Bundle();
        args.putInt(Constants.BUNDLE_START_INDEX, mAdapter.getItemCount());
        args.putInt(Constants.BUNDLE_COUNT, FAVORITES_THRESHOLD_COUNT);

        getLoaderManager().destroyLoader(LOADER_ID);
        getLoaderManager().initLoader(LOADER_ID, args, this);
    }

    private void checkForEmptyList(){
        if (mAdapter.getItemCount() > 0){
            mEmptyView.setVisibility(View.GONE);
            mFavorites.setVisibility(View.VISIBLE);
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
            mFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageClick(String path) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(FullSizeImageFragment.class.getSimpleName())
                .replace(R.id.content, FullSizeImageFragment.newInstance(path))
                .commit();
    }
}
