package edu.tamu.geoinnovation.fpx.activities.plans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import edu.tamu.geoinnovation.fpx.Models.Plan;
import edu.tamu.geoinnovation.fpx.R;
import edu.tamu.geoinnovation.fpx.activities.MainActivity;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static edu.tamu.geoinnovation.fpx.activities.plans.PlanActivityForm.CREATE_NEW_PLAN_REQUEST;
import static java.security.AccessController.getContext;

/**
 * Created by Abanoub Doss on 4/20/2018.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List<Plan> planList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView planName;
        TextView planSummary;
        Button playBtn;
        Button editBtn;
        Button deleteBtn;

        public ViewHolder(View rowView) {
            super(rowView);
            planName = rowView.findViewById(R.id.plan_name_text_view);
            planSummary = rowView.findViewById(R.id.plan_summary_text_view);
            playBtn = rowView.findViewById(R.id.play_button);
            editBtn = rowView.findViewById(R.id.edit_button);
            deleteBtn = rowView.findViewById(R.id.delete_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlanAdapter(List<Plan> planList) {
        this.planList = planList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.activity_plan_row, parent, false);
        PlanAdapter.ViewHolder viewHolder = new PlanAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.planName.setText(planList.get(position).name);
        String planSummary = Integer.toString(planList.get(position).getSize())    ;
        holder.planSummary.setText( "exercises : " +planSummary);

        holder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("startPlan", planList.get(position).name);
                ((Activity)v.getContext()).setResult(RESULT_OK,returnIntent);
                ((Activity)v.getContext()).finish();
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("plans").child(planList.get(position).name);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(v.getContext(), PlanActivityForm.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("edit", planList.get(position).name);
                        intent.putExtras(bundle);
                        //intent, CREATE_NEW_PLAN_REQUEST
                        startActivityForResult((Activity) v.getContext(), intent, CREATE_NEW_PLAN_REQUEST, bundle);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };

                ref.addListenerForSingleValueEvent(valueEventListener);
                notifyDataSetChanged();
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("plans").child(planList.get(position).name);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                          dataSnapshot.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };

                ref.addListenerForSingleValueEvent(valueEventListener);

                planList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return planList.size();
    }

}
