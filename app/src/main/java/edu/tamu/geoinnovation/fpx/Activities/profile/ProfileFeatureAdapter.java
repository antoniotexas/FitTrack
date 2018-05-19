package edu.tamu.geoinnovation.fpx.activities.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.tamu.geoinnovation.fpx.Models.Profile;
import edu.tamu.geoinnovation.fpx.R;

public class ProfileFeatureAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Profile> exList;


    public ProfileFeatureAdapter(Context c, ArrayList<Profile> list) {
        this.mContext = c;
        this.exList = list;
    }

    public int getCount() {
        return exList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.profile_grid_single_item, null);

            ImageView imageView = grid.findViewById(R.id.grid_image);
            TextView exerciseName = grid.findViewById(R.id.exercise_name);
            TextView typeCount = grid.findViewById(R.id.count);

            imageView.setImageResource(fetchExerciseIconID(exList.get(position).name));
            exerciseName.setText(exList.get(position).name);
            typeCount.setText(exList.get(position).count);

        } else {
            grid = (View) convertView;
        }

        return grid;
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
        }

        return R.drawable.walking_icon;
    }

}