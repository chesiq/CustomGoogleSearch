package ua.antonk.googlecustomsearch.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ua.antonk.googlecustomsearch.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullSizeImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullSizeImageFragment extends Fragment {

    private static final String ARG_IMAGE_LINK = "image_link";

    private String mImageLink;

    public static FullSizeImageFragment newInstance(String imageLink) {
        FullSizeImageFragment fragment = new FullSizeImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_LINK, imageLink);
        fragment.setArguments(args);
        return fragment;
    }

    public FullSizeImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mImageLink = getArguments().getString(ARG_IMAGE_LINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_size_image, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_full);
        Picasso.with(getActivity()).load(mImageLink).into(imageView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
    }
}
