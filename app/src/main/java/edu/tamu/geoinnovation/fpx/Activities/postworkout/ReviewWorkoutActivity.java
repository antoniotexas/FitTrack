package edu.tamu.geoinnovation.fpx.activities.postworkout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.tamu.geoinnovation.fpx.Models.Exercise;
import edu.tamu.geoinnovation.fpx.R;
import edu.tamu.geoinnovation.fpx.activities.plans.ExerciseAdapter;

public class ReviewWorkoutActivity extends AppCompatActivity {
    private RecyclerView workoutReviewContainer;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Exercise> workoutExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_workout);

        Intent trackWorkoutIntent = getIntent();
        ArrayList<String> names = trackWorkoutIntent.getStringArrayListExtra("WORKOUT_NAMES");
        ArrayList<String> counts = trackWorkoutIntent.getStringArrayListExtra("WORKOUT_COUNTS");

        for (int i = 0; i < names.size(); i++) {
            Exercise newExercise = new Exercise(names.get(i), "reps", counts.get(i));
            workoutExercises.add(newExercise);
        }

        // Initialize the recycler view that will show users the exercises they add to their plan.
        workoutReviewContainer = findViewById(R.id.workout_review_container);
        workoutReviewContainer.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        workoutReviewContainer.setLayoutManager(layoutManager);

        workoutExercises = new ArrayList<>();
        mAdapter = new ExerciseAdapter(workoutExercises);
        workoutReviewContainer.setAdapter(mAdapter);
    }

    public void endWorkoutReview(View view) {
        finish();
    }
}
