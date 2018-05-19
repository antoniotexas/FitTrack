package edu.tamu.geoinnovation.fpx.activities.plans;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.tamu.geoinnovation.fpx.Models.Exercise;
import edu.tamu.geoinnovation.fpx.R;

/**
 * Created by Abanoub Doss on 4/20/2018.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<Exercise> exerciseList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView exName;
        TextView count;
        Button deleteBtn;
        Button decrementBtn;
        Button increaseBtn;

        public ViewHolder(View rowView) {
            super(rowView);
            icon = rowView.findViewById(R.id.rowImageView);
            exName = rowView.findViewById(R.id.row_ex_name_text_view);
            count = rowView.findViewById(R.id.count_text_view);
            deleteBtn = rowView.findViewById(R.id.delete_button);
            decrementBtn = rowView.findViewById(R.id.decrement_button);
            increaseBtn = rowView.findViewById(R.id.increment_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExerciseAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.activity_plan_form_exercise_row, parent, false);
        ExerciseAdapter.ViewHolder viewHolder = new ExerciseAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.icon.setImageResource(fetchExerciseIconID(exerciseList.get(position).name));
        holder.exName.setText(exerciseList.get(position).name);
        String count = exerciseList.get(position).count + " " + exerciseList.get(position).countType;
        holder.count.setText(count);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(exerciseList.get(position).count) > 0) {
                    exerciseList.get(position).decrementCount();
                    notifyDataSetChanged();
                }
            }
        });
        holder.increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseList.get(position).incrementCount();
                notifyDataSetChanged();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    private int fetchExerciseIconID(String exercise) {
        switch (exercise) {
            case "Bench Press":
                return R.drawable.bench_press_icon;
            case "Shoulder Press":
                return R.drawable.shoulder_press_icon;
            case "Squats":
                return R.drawable.deadlifts_icon;
            case "Flys":
                // TODO: Find flys icon
                return R.drawable.dumbbell_icon;
            case "Pulldowns":
                return R.drawable.pulldowns_icon;
            case "Pushups":
                return R.drawable.push_ups_icon;
            case "Curls":
                return R.drawable.curls_icon;
            case "Situps":
                return R.drawable.situps_icon;
            case "Jumping Jacks":
                return R.drawable.jumping_jacks_icon;
            case "Jump Rope":
                return R.drawable.jumping_rope_icon;
            case "Running":
                return R.drawable.running_icon;
            case "Walking":
                return R.drawable.walking_icon;
            case "Step ups":
                return R.drawable.step_ups_icon;
            case "Stairs":
                return R.drawable.stairs_icon;
        }
        // Default??
        return R.drawable.walking_icon;
    }
}
