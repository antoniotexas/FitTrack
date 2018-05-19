package edu.tamu.geoinnovation.fpx.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import edu.tamu.geoinnovation.fpx.Models.Exercise;
import edu.tamu.geoinnovation.fpx.Models.Plan;
import edu.tamu.geoinnovation.fpx.activities.goals.GoalsActivity;
import edu.tamu.geoinnovation.fpx.activities.history.HistoryActivity;
import edu.tamu.geoinnovation.fpx.activities.profile.ProfileActivity;
import edu.tamu.geoinnovation.fpx.activities.tracking.TrackWorkoutActivity;
import edu.tamu.geoinnovation.fpx.activities.plans.PlanActivity;

import edu.tamu.geoinnovation.fpx.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final int PLAN_ACTIVITY_RESPONSE = 1;
    ArrayList<Plan> plans = new ArrayList<>();

    String planName;
    boolean returnPlanName = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load navigation bar.
        Toolbar navbar = findViewById(R.id.home_navigation_bar);
        setSupportActionBar(navbar);

        // Load user goals.
        loadGoals();

        // Load plans from database (Used if the user presses play / start workout button)
        loadPlans();

        // Set button functionality.
        findViewById(R.id.goals_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);
        findViewById(R.id.plan_button).setOnClickListener(this);
        findViewById(R.id.history_button).setOnClickListener(this);
        findViewById(R.id.workout_button).setOnClickListener(this);
    }

    public void loadPlans() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("plans");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            plans.add(child.getValue(Plan.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("MainActivity", "The read failed: " + databaseError.getCode());
                        //handle databaseError
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_actionbar, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int buttonID = view.getId();

        if (buttonID == R.id.goals_button) {
            Intent intent = new Intent(this, GoalsActivity.class);
            startActivity(intent);
        } else if (buttonID == R.id.profile_button) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (buttonID == R.id.plan_button) {
            Intent intent = new Intent(this, PlanActivity.class);
            startActivityForResult(intent, PLAN_ACTIVITY_RESPONSE);
        } else if (buttonID == R.id.history_button) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (buttonID == R.id.workout_button) {
            openBeginTrackingWorkoutDialog();
        }
    }

    public void loadGoals() {
        // TODO: Replace randomGoals() with a real get goals function.
        Map<String, Integer> goals = randomGoals();

        // If the number of goals we have is more than 3, we'll use two vertical sets of horizontal bars.
        if (goals.size() <= 3) {
            LinearLayout goals_container = findViewById(R.id.goals_preview_layout);
            goals_container.removeViewAt(1);
            goals_container.removeViewAt(0);
            goals_container.setWeightSum(goals.size());

            final float scale = this.getResources().getDisplayMetrics().density;
            int heightInPixels = (int) (200 * scale + 0.5f);
            int progressBarSizeInPixels = (int) (250 * scale + 0.5f);

            for (Map.Entry<String, Integer> goal : goals.entrySet()) {
                LinearLayout singleGoalContainer = new LinearLayout(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, heightInPixels);
                lp.weight = 1;
                singleGoalContainer.setLayoutParams(lp);
                singleGoalContainer.setOrientation(LinearLayout.VERTICAL);
                singleGoalContainer.setGravity(Gravity.CENTER);


                CircleProgressBar progressBar = new CircleProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleLarge);
                progressBar.setMinimumHeight(progressBarSizeInPixels / goals.size());
                progressBar.setMinimumWidth(progressBarSizeInPixels / goals.size());
                progressBar.setProgress(goal.getValue());
                progressBar.setMaxValue(100);
                progressBar.setStrokeWidth(4);
                progressBar.setBackgroundWidth(4);
                progressBar.setProgressColor(ContextCompat.getColor(this, R.color.colorPrimary));
                progressBar.setText(goal.getKey());    // set progress text
                progressBar.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));        // set text color
                /*
                progressBar.setSuffix("%"); 			// set suffix
                progressBar.setPrefix(""); 			// set prefix

                String goalName = goal.getKey();
                TextView goalLabel = new TextView(this);
                goalLabel.setGravity(Gravity.CENTER);
                goalLabel.setText(goalName);

                singleGoalContainer.addView(goalLabel);*/
                singleGoalContainer.addView(progressBar);
                goals_container.addView(singleGoalContainer);
            }
        } else {

            int i = 0;
            for (Map.Entry<String, Integer> goal : goals.entrySet()) {
                ProgressBar progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
                progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutparams.setMargins(0, 0, 0, 0);
                progressBar.setLayoutParams(layoutparams);
                progressBar.setProgress(goal.getValue());

                String goalName = goal.getKey();
                TextView goalLabel = new TextView(this);
                goalLabel.setGravity(Gravity.CENTER);
                goalLabel.setText(goalName);

                LinearLayout goals_container;
                if (i % 2 == 0) goals_container = findViewById(R.id.sub_goal_preview_left_layout);
                else goals_container = findViewById(R.id.sub_goal_preview_right_layout);
                goals_container.addView(goalLabel);
                goals_container.addView(progressBar);

                i++;
            }
        }
    }

    // NOTE: This is only a temporary function to generate mock data.
    public Map<String, Integer> randomGoals() {
        int numGoals = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        Map<String, Integer> goals = new HashMap<>();
        for (int i = 0; i < numGoals; i++) {
            goals.put("Goal #" + (i + 1), ThreadLocalRandom.current().nextInt(0, 100 + 1));
        }
        return goals;
    }

    public void openBeginTrackingWorkoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        final View beginWorkoutDialog = inflater.inflate(R.layout.activity_begin_tracking_workout_dialog, null);

        final ArrayList<String> planNames = new ArrayList<>();

        planNames.add("No Plan");
        //TODO when return from PlanAdapter the plans array is empty. Should not be empty
        for (Plan plan : plans) {
            planNames.add(plan.name);
        }

        ArrayAdapter<String> choosePlanAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, planNames);

        choosePlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner choosePlanSpinner = beginWorkoutDialog.findViewById(R.id.plan_spinner);
        choosePlanSpinner.setAdapter(choosePlanAdapter);

        choosePlanSpinner.setSelection(planNames.indexOf(planName));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(beginWorkoutDialog)
                // Add action buttons
                .setPositiveButton("Begin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String planSelected = choosePlanSpinner.getSelectedItem().toString();

                        CheckBox voiceGuidanceCheckBox = beginWorkoutDialog.findViewById(R.id.voice_guidance_option);
                        boolean voiceGuidance = voiceGuidanceCheckBox.isChecked();

                        Intent intent = new Intent(getApplicationContext(), TrackWorkoutActivity.class);
                        intent.putExtra("PLAN_NAME", planSelected);
                        intent.putExtra("VOICE_GUIDANCE", voiceGuidance);

                        if (!planSelected.equals("No Plan")) {
                            for (Plan plan : plans) {
                                if (plan.name.equals(planSelected)) {
                                    ArrayList<String> names = new ArrayList<>();
                                    ArrayList<String> counts = new ArrayList<>();
                                    for (Exercise exercise : plan.exerciseList) {
                                        names.add(exercise.name);
                                        counts.add(exercise.count);
                                    }
                                    intent.putExtra("PLAN_EXERCISES", names);
                                    intent.putExtra("PLAN_COUNTS", counts);
                                }
                            }
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: Maybe do something here?
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the PlanAdapter with an OK result
        if (requestCode == PLAN_ACTIVITY_RESPONSE) {
            if (resultCode == RESULT_OK) {
                planName = data.getStringExtra("startPlan");
                returnPlanName = true;
                openBeginTrackingWorkoutDialog();
            }
        }
    }
}
