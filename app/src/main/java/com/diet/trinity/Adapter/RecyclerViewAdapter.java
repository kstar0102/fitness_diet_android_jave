package com.diet.trinity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diet.trinity.R;
import com.diet.trinity.activity.RecipieSingleActivity;
import com.diet.trinity.model.Recipe;

import java.util.ArrayList;


/**
 * Created by User on 2/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<Recipe> mLists = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Recipe> list) {
        mLists = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder._photo.setImageResource(mLists.get(position).getPhotoResId());

        holder._name.setText(mLists.get(position).getName());

        holder._point.setText(String.valueOf(mLists.get(position).getPoints()) + " points");

        holder._card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(mContext, RecipieSingleActivity.class);
            mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView _photo;
        TextView _name;
        TextView _point;
        CardView _card;

        public ViewHolder(View itemView) {
            super(itemView);
            _photo = itemView.findViewById(R.id.imgPhoto);
            _name = itemView.findViewById(R.id.name);
            _point = itemView.findViewById(R.id.point);
            _card = itemView.findViewById(R.id.card);
        }
    }
}
