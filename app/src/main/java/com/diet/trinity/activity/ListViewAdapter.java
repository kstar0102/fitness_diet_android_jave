package com.diet.trinity.activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.diet.trinity.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    private List<FoodItems> FoodNamesList = null;
    private ArrayList<FoodItems> arraylist;

    public ListViewAdapter(Context context, List<FoodItems> FoodNamesList) {
        mContext = context;
        this.FoodNamesList = FoodNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<FoodItems>();
        this.arraylist.addAll(FoodNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return FoodNamesList.size();
    }

    @Override
    public FoodItems getItem(int position) {
        return FoodNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(FoodNamesList.get(position).getFoodName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        FoodNamesList.clear();
        if (charText.length() == 0) {
            FoodNamesList.addAll(arraylist);
        } else {
            for (FoodItems wp : arraylist) {
                if (wp.getFoodName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    FoodNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
