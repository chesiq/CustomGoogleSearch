package ua.antonk.googlecustomsearch.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ua.antonk.googlecustomsearch.Constants;
import ua.antonk.googlecustomsearch.R;
import ua.antonk.googlecustomsearch.database.DatabaseOpenHelper;
import ua.antonk.googlecustomsearch.database.entities.Item;
import ua.antonk.googlecustomsearch.database.entities.SearchResult;
import ua.antonk.googlecustomsearch.loaders.SearchResultLoader;
import ua.antonk.googlecustomsearch.ui.adapters.SearchResultsAdapter;
import ua.antonk.googlecustomsearch.ui.util.DividerItemDecoration;
import ua.antonk.googlecustomsearch.ui.util.EndlessRecyclerOnScrollListener;
import ua.antonk.googlecustomsearch.util.PicUtils;

public class SearchFragment extends Fragment  implements
        LoaderManager.LoaderCallbacks<SearchResult>,
        SearchResultsAdapter.Callbacks {

    public interface onDataChangedListener {
        void onDataChanged();
    }

    private onDataChangedListener mCallback;

    private static final int LOADER_ID = 1;

    private RecyclerView mSearchResults;
    private SearchResultsAdapter mAdapter;
    private ProgressBar mProgress;
    private LinearLayoutManager mLayoutManager;
    private TextView mEmptyView;
    private SearchView mTextSearch;

    List<Item> mDataToSave;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDataToSave = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mSearchResults = (RecyclerView) view.findViewById(R.id.list_search_results);
        mSearchResults.setLayoutManager(mLayoutManager);

        mAdapter = new SearchResultsAdapter(getActivity(), new SearchResult(), this);
        mProgress = (ProgressBar) view.findViewById(R.id.progress_search);

        mSearchResults.setAdapter(mAdapter);
        mSearchResults.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadResults(mTextSearch.getQuery().toString());
            }
        });
        mSearchResults.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));

        mEmptyView = (TextView) view.findViewById(R.id.empty_view);

        checkForEmptyList();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (onDataChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onDataChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DatabaseOpenHelper.getInstance(getActivity()).close();
        mCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mTextSearch = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mTextSearch.setIconifiedByDefault(false);
        mTextSearch.setQueryHint(getResources().getString(R.string.prompt_search));
        mTextSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (saveData())
                Toast.makeText(getActivity(), R.string.text_data_saved, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<SearchResult> onCreateLoader(int id, Bundle args) {
        int startIndex = args.getInt(Constants.BUNDLE_START_INDEX, 0);
        String request = args.getString(Constants.BUNDLE_SEARCH_REQUEST);
        return new SearchResultLoader(getActivity(), request, startIndex);
    }

    @Override
    public void onLoadFinished(Loader loader, SearchResult data) {
        if (data.getError() != null){
            Toast.makeText(getActivity(), data.getError(), Toast.LENGTH_LONG).show();
            return;
        }
        toggleProgressBar(false);
        loader.stopLoading();

        mAdapter.setData(data);
        checkForEmptyList();
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    private void loadResults(String request){
        if (request.isEmpty())
            return;

        toggleProgressBar(true);

        Bundle args = new Bundle();
        args.putInt(Constants.BUNDLE_START_INDEX, mAdapter.getItemCount() + 1);
        args.putString(Constants.BUNDLE_SEARCH_REQUEST, request);

        LoaderManager loader = getLoaderManager();

        if (loader.getLoader(LOADER_ID) == null)
            loader.initLoader(LOADER_ID, args, this);
        else
            loader.restartLoader(LOADER_ID, args, this);
    }

    private void checkForEmptyList(){
        if (mAdapter.getItemCount() > 0){
            mEmptyView.setVisibility(View.GONE);
            mSearchResults.setVisibility(View.VISIBLE);
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
            mSearchResults.setVisibility(View.GONE);
        }
    }

    private void toggleProgressBar(boolean isVisible){
        mProgress.setVisibility(
                isVisible ?
                View.VISIBLE :
                View.GONE
        );
    }

    @Override
    public void onItemCheckChanged(Item item, boolean isChecked) {
        if (isChecked)
            mDataToSave.add(item);
        else
            mDataToSave.remove(item);
    }

    @Override
    public void onImageClicked(String link) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(FullSizeImageFragment.class.getSimpleName())
                .replace(R.id.content, FullSizeImageFragment.newInstance(link))
                .commit();
    }

    private boolean saveData(){
        if (!mDataToSave.isEmpty()) {
            saveImages();
            DatabaseOpenHelper.getInstance(getActivity()).insertData(mDataToSave);
            mCallback.onDataChanged();
            return true;
        }
        return false;
    }

    private void saveImages(){
        for(Item item : mDataToSave){
            String imageFileName = item.getTitle().replaceAll("\\s+", "");
            String thumbFileName = imageFileName + "_thumb";
            String savedImage = PicUtils.saveImage(getActivity(), item.getImageLink(),
                    imageFileName, PicUtils.ImageType.IMAGE);
            String savedThumb = PicUtils.saveImage(getActivity(), item.getThumbnailLink(),
                    thumbFileName, PicUtils.ImageType.THUMBNAIL);
            item.setImagePath(savedImage);
            item.setThumbPath(savedThumb);
        }
    }
}
