package com.diet.trinity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.diet.trinity.Adapter.SampleCollapsedViewHolder;
import com.diet.trinity.Adapter.SampleExpandedViewHolder;
import com.diet.trinity.R;
import com.diet.trinity.model.SampleItem;
import com.sysdata.widget.accordion.ExpandableItemHolder;
import com.sysdata.widget.accordion.FancyAccordionView;
import com.sysdata.widget.accordion.Item;
import com.sysdata.widget.accordion.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class PolicyActivity extends AppCompatActivity {
    private static final String KEY_EXPANDED_ID = "expandedId";

    public static final int VIEW_TYPE_1 = 1;
    public static final int VIEW_TYPE_2 = 2;

    private Toast mToast;
    private FancyAccordionView mRecyclerView;
    private ItemAdapter.OnItemClickedListener mListener = new ItemAdapter.OnItemClickedListener() {
        @Override
        public void onItemClicked(ItemAdapter.ItemViewHolder<?> viewHolder, int id) {
            ItemAdapter.ItemHolder itemHolder = viewHolder.getItemHolder();
            SampleItem item = ((SampleItem) itemHolder.item);

            switch (id) {
                case ItemAdapter.OnItemClickedListener.ACTION_ID_COLLAPSED_VIEW:
                    //showToast(String.format("Collapsed %s clicked!", item.getTitle()));
                    break;
                case ItemAdapter.OnItemClickedListener.ACTION_ID_EXPANDED_VIEW:
                    //showToast(String.format("Expanded %s clicked!", item.getTitle()));
                    break;
                default:
                    // do nothing
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        mRecyclerView = (FancyAccordionView) findViewById(R.id.alarms_recycler_view);

        // bind the factory to create view holder for item collapsed of type 1
        mRecyclerView.setCollapsedViewHolderFactory(
                SampleCollapsedViewHolder.Factory.create(R.layout.sample_layout_collapsed),
                mListener,
                VIEW_TYPE_1
        );
        // bind the factory to create view holder for item collapsed of type 2
        mRecyclerView.setCollapsedViewHolderFactory(
                SampleCollapsedViewHolder.Factory.create(R.layout.sample_layout_collapsed),
                mListener,
                VIEW_TYPE_2
        );

        // bind the factory to create view holder for item expanded of type 1
        mRecyclerView.setExpandedViewHolderFactory(
                SampleExpandedViewHolder.Factory.create(R.layout.sample_layout_expanded),
                mListener,
                VIEW_TYPE_1
        );
        // bind the factory to create view holder for item expanded of type 2
        mRecyclerView.setExpandedViewHolderFactory(
                SampleExpandedViewHolder.Factory.create(R.layout.sample_layout_expanded),
                mListener,
                VIEW_TYPE_2
        );

        // restore the expanded item from state
        if (savedInstanceState != null) {
            mRecyclerView.setExpandedItemId(savedInstanceState.getLong(KEY_EXPANDED_ID, Item.INVALID_ID));
        }

        // populate RecyclerView with mock data
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_EXPANDED_ID, mRecyclerView.getExpandedItemId());
    }

    private void loadData() {
        final int dataCount = 7;
        int index = 0;

        final List<ExpandableItemHolder> itemHolders = new ArrayList<>(7);
        Item itemModel;
        ExpandableItemHolder itemHolder;

        String[] title = getResources().getStringArray(R.array.listTitle);
        String[] content = getResources().getStringArray(R.array.listContent);

        for (; index < dataCount; index++) {
            String full_content = content[index];
            if(index == 5)
                full_content = full_content + getResources().getString(R.string.string6);
            itemModel = SampleItem.create(title[index], full_content);
            itemHolder = new ExpandableItemHolder(itemModel, VIEW_TYPE_1);
            itemHolders.add(itemHolder);
        }

        mRecyclerView.setAdapterItems(itemHolders);
    }

}