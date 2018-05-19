package edu.tamu.geoinnovation.fpx.activities.tracking;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.getpebble.android.kit.PebbleKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.tamu.geoinnovation.fpx.Models.Exercise;
import edu.tamu.geoinnovation.fpx.activities.plans.ExerciseAdapter;
import edu.tamu.geoinnovation.fpx.activities.postworkout.ReviewWorkoutActivity;
import edu.tamu.geoinnovation.fpx.R;
import edu.tamu.geoinnovation.fpx.activities.wearables.pebble.PebbleCommunication;
import edu.tamu.geoinnovation.fpx.activities.wearables.pebble.PebbleInitializationActivity;
import edu.tamu.geoinnovation.fpx.utils.tracking.DynamicWindow;
import edu.tamu.geoinnovation.fpx.utils.tracking.DynamicWindowManager;

import static edu.tamu.geoinnovation.fpx.activities.wearables.pebble.PebbleInitializationActivity.PEBBLE_APP_UUID;

public class TrackWorkoutActivity extends AppCompatActivity {
    private static final int INITIALIZE_WEARABLE_SERVICE = 1;  // The request code

    // If you plan to create a new platform, then search for USED_WEARBLE_PLATFORM to find the
    // functions that must be implemented for that wearable. Base example: Pebble.
    enum Wearable_Platform {
        Pebble
    } // All supported wearable platforms.

    public static final Wearable_Platform USED_WEARBLE_PLATFORM = Wearable_Platform.Pebble;

    private RecyclerView workoutReviewContainer;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Exercise> workoutExercises = new ArrayList<>();
    List<Exercise> actualExercises = new ArrayList<>();

    //private NotificationManagerCompat notificationManager;
    //private NotificationCompat.Builder mBuilder;
    private String workoutHistory = "No Workout\n";

    DynamicWindowManager dwManager = new DynamicWindowManager();

    CircleProgressBar workoutProgressBar;
    TextToSpeech textToSpeech;
    String toSpeech;

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "wearable-communication" is broadcasted.
    private BroadcastReceiver wearableCommReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double time = intent.getDoubleExtra("time", 0.0);
            Double xValue = intent.getDoubleExtra("x-value", 0.0);
            Double yValue = intent.getDoubleExtra("y-value", 0.0);
            Double zValue = intent.getDoubleExtra("z-value", 0.0);
            dwManager.addData(time, xValue, yValue, zValue);
            updateWorkoutNotification();
            updateScreen();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_workout);


        // Initialize whichever wearable is in use.
        switch (USED_WEARBLE_PLATFORM) {
            case Pebble:
                Intent initializePebbleIntent = new Intent(this, PebbleInitializationActivity.class);
                startActivityForResult(initializePebbleIntent, INITIALIZE_WEARABLE_SERVICE);
                break;
            default:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INITIALIZE_WEARABLE_SERVICE) {
            if (resultCode == RESULT_CANCELED) finish();
            else {

                PebbleKit.startAppOnPebble(this, PEBBLE_APP_UUID);

                // Fetch the plan and whether voice guidance is enabled.
                Intent intent = getIntent();
                String planName = intent.getStringExtra("PLAN_NAME");
                boolean voiceGuidance = intent.getBooleanExtra("VOICE_GUIDANCE", false);

                ArrayList<String> names = intent.getStringArrayListExtra("PLAN_EXERCISES");
                ArrayList<String> counts = intent.getStringArrayListExtra("PLAN_COUNTS");
                if (!planName.equals("No Plan") && names != null && counts != null) {
                    for (int i = 0; i < names.size(); i++) {
                        workoutExercises.add(new Exercise(names.get(i), "reps", counts.get(i)));
                    }
                }

                // Initialize text2speech for voice guidance.
                if (voiceGuidance) {
                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(Locale.US);
                            }
                        }
                    });
                }

        /*
        TODO: Implement speaking where necessary.
                addExercise(exercise, type, 0);
                String toSpeak = "Your next exercie is" + exercise;
                textToSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null,null);
         */

                // Initialize the recycler view that will show users the exercises they add to their plan.
                workoutReviewContainer = findViewById(R.id.workout_review_container);
                workoutReviewContainer.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(this);
                workoutReviewContainer.setLayoutManager(layoutManager);

                mAdapter = new ExerciseAdapter(workoutExercises);
                workoutReviewContainer.setAdapter(mAdapter);


                // Begin listening to the wearable device.
                LocalBroadcastManager.getInstance(this).registerReceiver(wearableCommReceiver,
                        new IntentFilter("wearable-communication"));


                // Initialize the service for whichever wearable is in use.
                switch (USED_WEARBLE_PLATFORM) {
                    case Pebble:
                        startService(new Intent(this, PebbleCommunication.class));
                        break;
                    default:
                        finish();
                }

                // Initialize progress bar.
                workoutProgressBar = findViewById(R.id.progressBar);
                workoutProgressBar.setMaxValue(9);
                workoutProgressBar.setProgress(0);


                // TODO: Replace this with a function that simply modifies this view instead of moving users to a different view.
                // Create an Intent for the activity you want to start
                Intent resultIntent = new Intent(this, ReviewWorkoutActivity.class);

                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);

                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        /*
        // TODO: Build a notification channel.
        mBuilder = new NotificationCompat.Builder(this, "AW")
                .setSmallIcon(R.drawable.shoulder_press_icon)
                .setContentTitle("FitnessRPG - Active Workout")
                .setContentText("You're working out! Keep it up!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_close_black_24dp, "End Workout", resultPendingIntent)
                .setOngoing(true);

        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build()); */
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Workout")
                .setMessage("Are you sure you want to cancel this workout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    // TODO: Check if we need to implement anything for onResume() or onPause()
    @Override
    protected void onDestroy() {
        PebbleKit.closeAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
        //notificationManager.cancel(0);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(wearableCommReceiver);
        super.onDestroy();
    }

    public void endWorkout(View view) {
        Intent reviewWorkoutIntent = new Intent(this, ReviewWorkoutActivity.class);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> counts = new ArrayList<>();
        for (Exercise exercise : workoutExercises) {
            names.add(exercise.name);
            counts.add(exercise.count);
        }

        reviewWorkoutIntent.putStringArrayListExtra("WORKOUT_NAMES", names);
        reviewWorkoutIntent.putStringArrayListExtra("WORKOUT_COUNTS", counts);
        startActivity(reviewWorkoutIntent);

        finish();
    }

    private void updateScreen() {
        TextView cw_label = findViewById(R.id.current_workout_label);
        cw_label.setText(dwManager.currentExercise());
        workoutProgressBar.setText(dwManager.currentReps());
        workoutProgressBar.setProgress(dwManager.repsProgress());

        workoutExercises.clear();
        for (DynamicWindow curDW : dwManager.allDWs) {
            if (curDW != null) {
                String curWL = curDW.determineExercise();
                int reps = curDW.getReps();

                if (curWL.equals("Running") || curWL.equals("Stairs") || curWL.equals("Stairs")) {
                    workoutExercises.add(new Exercise(curWL, "secs", Integer.toString(reps)));
                } else {
                    workoutExercises.add(new Exercise(curWL, "reps", Integer.toString(reps)));
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateWorkoutNotification() {
        /*
        if (dwManager.isUserActive()) {
            mBuilder.setContentText("Currently doing " + dwManager.currentExercise() + "! Keep it up!");
        } else {
            mBuilder.setContentText("You're working out! Keep it up!");
        }

        notificationManager.notify(0, mBuilder.build());
        */
    }
}
