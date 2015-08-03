package ua.antonk.googlecustomsearch.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import ua.antonk.googlecustomsearch.database.DatabaseOpenHelper;

/**
 * Created by Anton on 02.08.2015.
 */
public class DatabaseLoader extends AsyncTaskLoader<Cursor> {

    private ForceLoadContentObserver mObserver;

    private DatabaseOpenHelper mDatabaseManager;
    private int mStartIndex;
    private int mCount;

    private Cursor mCursor;

    public DatabaseLoader(Context context, DatabaseOpenHelper manager, int startIndex, int count) {
        super(context);
        mDatabaseManager = manager;
        mStartIndex = startIndex;
        mCount = count;

        mObserver = new ForceLoadContentObserver();
    }

    @Override
    public Cursor loadInBackground() {

        Cursor cursor = mDatabaseManager.getData(mStartIndex, mCount);
        if (cursor != null) {
            cursor.getCount();
            cursor.registerContentObserver(mObserver);
        }
        return cursor;
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Cursor data) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (data != null) {
                data.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldCursor != null && oldCursor != data && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        // Retry a refresh the next time the loader is started
        onContentChanged();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }
}
