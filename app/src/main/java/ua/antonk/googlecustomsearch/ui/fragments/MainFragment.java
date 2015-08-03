package ua.antonk.googlecustomsearch.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.antonk.googlecustomsearch.R;
import ua.antonk.googlecustomsearch.ui.activities.MainActivity;
import ua.antonk.googlecustomsearch.ui.adapters.MainPagerAdapter;

public class MainFragment extends Fragment {

    private ViewPager mPager;

    public MainPagerAdapter getAdapter() {
        return mAdapter;
    }

    private MainPagerAdapter mAdapter;
    private TabLayout mTabs;

    boolean isMenuVisible = true;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mPager = (ViewPager) view.findViewById(R.id.pager);

        mAdapter = new MainPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mAdapter);

        mTabs = (TabLayout) view.findViewById(R.id.tab_layout);
        for (int i = 0; i < MainPagerAdapter.TABS.length; i++) {
            mTabs.addTab(mTabs.newTab().setText(MainPagerAdapter.TABS[i]));
        }
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                updateMenu(mAdapter.getRegisteredFragment(mPager.getCurrentItem())
                );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_save).setVisible(isMenuVisible);
        menu.findItem(R.id.action_search).setVisible(isMenuVisible);
    }


    public void updateMenu(Fragment currentFragment){
        isMenuVisible = currentFragment instanceof SearchFragment;
        getActivity().invalidateOptionsMenu();
    }
}
