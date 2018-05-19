package edu.tamu.geoinnovation.fpx.activities.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import edu.tamu.geoinnovation.fpx.R;

/**
 * Created by matt1.01 on 3/26/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> items;

    public HistoryAdapter(ArrayList<ArrayList<String>> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<String> item = items.get(position);

        holder.userNameText.setText(item.get(0));
        holder.planeNameText.setText(item.get(1));
        holder.repsNameText.setText(item.get(2));
        holder.dateText.setText(item.get(3));

        //holder.image.setImageBitmap(null);
        //Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        //Picasso.with(holder.image.getContext()).load(item.image).into(holder.image);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameText;
        TextView planeNameText;
        TextView repsNameText;
        TextView dateText;


        ViewHolder(View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.username);
            planeNameText = itemView.findViewById(R.id.exername);
            repsNameText = itemView.findViewById(R.id.repsname);
            dateText = itemView.findViewById(R.id.datename);
        }
    }
}
