package ua.antonk.googlecustomsearch.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import ua.antonk.googlecustomsearch.ui.fragments.FavoritesFragment;
import ua.antonk.googlecustomsearch.ui.fragments.SearchFragment;

/**
 * Created by Anton on 01.08.2015.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public static final String[] TABS = {"Search", "Favorites"};

    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SearchFragment.newInstance();
            case 1:
                return FavoritesFragment.newInstance();
            default:
                return SearchFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
