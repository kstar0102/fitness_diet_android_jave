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

public class CustomAdapter extends BaseAdapter  {
    ArrayList listItem = new ArrayList<>();
    public Context context;
    public ArrayList<Listmodel> FoodList;

    public CustomAdapter(Context context, ArrayList<Listmodel> FoodList) {
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
        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.mainlist_item, parent, false);
            holder=new FoodHolder();
            holder.title=(TextView) convertView.findViewById(R.id.listitem_title);
            holder.gram_txt=(TextView) convertView.findViewById(R.id.gram_txt);
            holder.point_txt=(TextView) convertView.findViewById(R.id.point_txt);
            holder.id_btn=(ImageView) convertView.findViewById(R.id.imgAddMeal);
            convertView.setTag(holder);
        }
        else
        {
            holder=(FoodHolder) convertView.getTag();
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

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                final FilterResults oReturn = new FilterResults();
//                final ArrayList<Listmodel> results = new ArrayList<Listmodel>();
//                if (orig == null)
//                    orig = employeeArrayList;
//                if (constraint != null) {
//                    if (orig != null && orig.size() > 0) {
//                        for (final Listmodel g : orig) {
//                            if (g.getListText().toLowerCase()
//                                    .contains(constraint.toString()))
//                                results.add(g);
//                        }
//                    }
//                    oReturn.values = results;
//                }
//                return oReturn;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint,
//                                          FilterResults results) {
//                employeeArrayList = (ArrayList<Listmodel>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }
}
