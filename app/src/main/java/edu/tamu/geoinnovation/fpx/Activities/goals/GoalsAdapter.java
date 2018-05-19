package edu.tamu.geoinnovation.fpx.activities.goals;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emredavarci.circleprogressbar.CircleProgressBar;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.tamu.geoinnovation.fpx.Models.Goal;
import edu.tamu.geoinnovation.fpx.R;

/**
 * Created by Abanoub Doss on 4/16/2018.
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {
    private List<Goal> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CircleProgressBar goalProgress;
        public TextView goalName, goalInfo, goalTime;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            goalProgress = v.findViewById(R.id.goalProgress);
            goalProgress.setMaxValue(100);
            goalName = v.findViewById(R.id.goalName);
            goalInfo = v.findViewById(R.id.goalInfo);
            goalTime = v.findViewById(R.id.goalTime);
            goalTime.setTypeface(null, Typeface.ITALIC);
        }
    }

    public void add(int position, Goal item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GoalsAdapter(List<Goal> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_goals_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Goal curGoal = values.get(position);
        Random rand = new Random();
        holder.goalProgress.setProgress(rand.nextInt(100) + 1);
        holder.goalName.setText(curGoal.name);
        holder.goalInfo.setText(curGoal.objective.toString());
        SimpleDateFormat simpleDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        String dateString = simpleDate.format(curGoal.startDate) + " to " + simpleDate.format(curGoal.endDate);
        holder.goalTime.setText(dateString);
        holder.goalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}
