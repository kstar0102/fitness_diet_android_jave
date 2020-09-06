package com.diet.trinity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.diet.trinity.R;
import com.diet.trinity.model.Listmodel;

import org.json.JSONException;

import java.util.ArrayList;

public class CustomMealAdapter extends BaseAdapter  {
    ArrayList listItem = new ArrayList<>();
    public Context context;
    public ArrayList<Listmodel> FoodList;

    public CustomMealAdapter(Context context, ArrayList<Listmodel> FoodList) {
        super();
        this.context = context;
        this.FoodList = FoodList;
    }

    public class FoodHolder
    {
        TextView title;
        TextView gram_txt;
        TextView point_txt;
        ImageView id_btn;
    }

    @Override
    public int getCount() {
        return FoodList.size();
    }

    @Override
    public Object getItem(int position) {
        return FoodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_list_item, parent, false);
            holder = new FoodHolder();
            holder.title = (TextView) convertView.findViewById(R.id.listitem_title);
            holder.gram_txt = (TextView) convertView.findViewById(R.id.gram_txt);
            holder.point_txt = (TextView) convertView.findViewById(R.id.point_txt);
            holder.id_btn = (ImageView) convertView.findViewById(R.id.imgAddMeal);
            convertView.setTag(holder);
        } else {
            holder = (FoodHolder) convertView.getTag();
        }

        try {
            holder.title.setText(FoodList.get(position).getListTitle());
            holder.gram_txt.setText(FoodList.get(position).getListGram());
            holder.point_txt.setText(FoodList.get(position).getListPoint());
            holder.id_btn.setId(FoodList.get(position).getListId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
