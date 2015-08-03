package ua.antonk.googlecustomsearch.ui.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import ua.antonk.googlecustomsearch.R;
import ua.antonk.googlecustomsearch.database.entities.Item;
import ua.antonk.googlecustomsearch.database.entities.SearchResult;

/**
 * Created by Anton on 01.08.2015.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    public interface Callbacks{
        void onItemCheckChanged(Item item, boolean isChecked);
        void onImageClicked(String imageLink);
    }

    private Context mContext;
    private SearchResult mResult;
    private Callbacks mCallbacks;

    public SearchResultsAdapter(Context context, SearchResult result, Callbacks callbacks){
        mContext = context;
        mResult = result;
        mCallbacks = callbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_result, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final int position = i;
        final Item item = mResult.getItems().get(position);

        viewHolder.name.setText(item.getTitle());
        viewHolder.addToFav.setChecked(item.isChecked());
        viewHolder.addToFav.setTag(item);

        viewHolder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Item item = (Item) cb.getTag();
                item.setIsChecked(cb.isChecked());
                mResult.getItems().get(position).setIsChecked(cb.isChecked());

                mCallbacks.onItemCheckChanged(item, cb.isChecked());
            }
        });

        if (item.getPagemap() != null) {

            Picasso.with(mContext).load(item.getThumbnailLink()).into(viewHolder.image);
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.onImageClicked(item.getImageLink());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mResult.getItems() == null)
            return 0;

        return mResult.getItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected ImageView image;
        protected TextView name;
        protected CheckBox addToFav;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            addToFav = (CheckBox) itemView.findViewById(R.id.check_add_to_fav);
        }
    }

    public void setData(SearchResult result){
        List<Item> newData = result.getItems();

        if (mResult.getItems() != null) {
            mResult.getItems().addAll(newData);
            notifyItemRangeInserted(mResult.getItems().size() + 1, newData.size());
        }
        else {
            mResult.setItems(newData);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        if (mResult.getItems() == null)
            return;

        mResult.getItems().clear();
        notifyDataSetChanged();
    }
}
