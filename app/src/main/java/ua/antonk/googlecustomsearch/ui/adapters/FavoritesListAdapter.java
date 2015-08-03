package ua.antonk.googlecustomsearch.ui.adapters;

import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ua.antonk.googlecustomsearch.R;
import ua.antonk.googlecustomsearch.database.DatabaseOpenHelper;
import ua.antonk.googlecustomsearch.database.entities.Item;

/**
 * Created by Anton on 02.08.2015.
 */
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    public interface Callbacks{
        void onImageClick(String path);
    }

    private List<Item> mData;
    private Callbacks mCallbacks;

    public FavoritesListAdapter(List<Item> data, Callbacks callbacks){
        mData = data;
        mCallbacks = callbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData == null)
            return;

        Item item = mData.get(position);
        holder.mTitle.setText(item.getTitle());

        ImageView image = holder.mImage;
        if (item.getThumbPath() != null) {
            Uri thumbUri = Uri.fromFile(new File(item.getThumbPath()));
            Picasso.with(image.getContext()).load(thumbUri).into(image);

            final Uri imageUri = Uri.fromFile(new File(item.getImagePath()));
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.onImageClick(imageUri.toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImage;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.image);
            mTitle = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public void setData(Cursor data){
        List<Item> newData = new ArrayList<>();
        if (data != null){
            for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                String title = data.getString(data.getColumnIndex(DatabaseOpenHelper.COLUMN_TITLE));
                String link = data.getString(data.getColumnIndex(DatabaseOpenHelper.COLUMN_LINK));
                String image = data.getString(data.getColumnIndex(DatabaseOpenHelper.COLUMN_IMAGE));
                String thumb = data.getString(data.getColumnIndex(DatabaseOpenHelper.COLUMN_THUMBNAIL));
                newData.add(new Item(title, link, image, thumb));
            }
        }

        if (mData != null) {
            mData.addAll(newData);
            notifyItemRangeInserted(mData.size() + 1, newData.size());
        }
        else {
            mData = newData;
            notifyDataSetChanged();
        }
    }
}
