package ua.antonk.googlecustomsearch.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import ua.antonk.googlecustomsearch.database.entities.SearchResult;
import ua.antonk.googlecustomsearch.interfaces.ApiMethods;

/**
 * Created by Anton on 02.08.2015.
 */
public class SearchResultLoader extends AsyncTaskLoader<SearchResult> {

    public static final String API_URL = "https://www.googleapis.com/customsearch/v1";
    public static final String PARAMETER_KEY = "AIzaSyAKvgEvVGXPBjNWak7OdH2MCjq-o0o_pHE";
    public static final String PARAMETER_CX = "003258582942626771348:10e2o-qsd-c";
    public static final String PARAMETER_FIELDS = "items(title,link,pagemap/cse_image,pagemap/cse_thumbnail)";

    private RestAdapter mRestAdapter;
    private SearchResult mResult;
    private String mRequest;
    private int mStartIndex;

    public SearchResultLoader(Context context, String request, int startIndex) {
        super(context);
        mRequest = request;
        mStartIndex = startIndex;
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

    }

    @Override
    public SearchResult loadInBackground() {
        ApiMethods methods = mRestAdapter.create(ApiMethods.class);
        SearchResult result = new SearchResult();
        try {
            result = methods.getSearchResults(
                    PARAMETER_KEY,
                    PARAMETER_CX,
                    mRequest,
                    mStartIndex,
                    PARAMETER_FIELDS);
        }catch (RetrofitError e){
            e.printStackTrace();
            result.setError(e.getMessage());
        }

        return result;
    }

    @Override
    public void deliverResult(SearchResult data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        SearchResult oldData = mResult;
        mResult = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
        }

        if (takeContentChanged() || mResult == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private void releaseResources(SearchResult data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }
}
