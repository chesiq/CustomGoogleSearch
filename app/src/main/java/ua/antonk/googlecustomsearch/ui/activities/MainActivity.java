package ua.antonk.googlecustomsearch.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import ua.antonk.googlecustomsearch.R;
import ua.antonk.googlecustomsearch.ui.fragments.FavoritesFragment;
import ua.antonk.googlecustomsearch.ui.fragments.MainFragment;
import ua.antonk.googlecustomsearch.ui.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements
        SearchFragment.onDataChangedListener {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.content) == null) {
            manager.beginTransaction()
                    .addToBackStack(MainFragment.class.getSimpleName())
                    .add(R.id.content, MainFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDataChanged() {
        MainFragment main = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.content);

        FavoritesFragment favorites = (FavoritesFragment) main.getAdapter().getRegisteredFragment(1);
        favorites.loadData();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else
            super.onBackPressed();
    }
}
