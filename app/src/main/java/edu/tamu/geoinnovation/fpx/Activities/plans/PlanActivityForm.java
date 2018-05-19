package edu.tamu.geoinnovation.fpx.activities.plans;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.tamu.geoinnovation.fpx.Models.Exercise;
import edu.tamu.geoinnovation.fpx.Models.Plan;
import edu.tamu.geoinnovation.fpx.R;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static edu.tamu.geoinnovation.fpx.activities.plans.PlanActivity.CREATE_NEW_PLAN_REQUEST;

public class PlanActivityForm extends AppCompatActivity {
    static final int CREATE_NEW_PLAN_REQUEST = 1;  // The request code
    private RecyclerView planPreviewContainer;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Exercise> exercises;

    DatabaseReference mDataBase;

    EditText planNameInput;

    String planName;
    String planNameFromIntent;
    String isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout file and load the navigation bar.
        setContentView(R.layout.activity_plan_form);
        Toolbar navbar = findViewById(R.id.navigation_bar);
        setSupportActionBar(navbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        planNameInput = findViewById(R.id.workout_plan_name);

        // Initialize the recycler view that will show users the exercises they add to their plan.
        planPreviewContainer = findViewById(R.id.plan_preview_container);
        planPreviewContainer.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        planPreviewContainer.setLayoutManager(layoutManager);
        String title = "Create Plan";
        exercises = new ArrayList<>();
        Intent intent = getIntent();
        isEdit = "No";
        mAdapter = new ExerciseAdapter(exercises);
        if(intent.hasExtra("edit")){
            title = "Edit Plan";
            Bundle bd = intent.getExtras();
            if(!bd.getString("edit").equals(null)){
                String value = bd.getString("edit");
                isEdit = value;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("plans").child(value);
                planNameInput.setText(value);
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Plan newPost = dataSnapshot.getValue(Plan.class);
                            exercises.addAll(newPost.exerciseList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                ref.addListenerForSingleValueEvent(valueEventListener);
            }
        }
        setTitle(title);
        planPreviewContainer.setAdapter(mAdapter);

    }


    public void openAddExerciseDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View dialogAddExerciseView = inflater.inflate(R.layout.activity_plan_add_exercise_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogAddExerciseView)
                // Add action buttons
                .setPositiveButton("Add Exercise", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(exercises.size() == 0) {
                           findViewById(R.id.empty_recycler_view_notice).setVisibility(View.GONE);
                        }

                        EditText countEditText = dialogAddExerciseView.findViewById(R.id.exercise_count_input);
                        String exerciseCount = countEditText.getText().toString();

                        Spinner exerciseSpinner = dialogAddExerciseView.findViewById(R.id.exercise_spinner);
                        String exerciseName = exerciseSpinner.getSelectedItem().toString();

                        Spinner countTypeSpinner = dialogAddExerciseView.findViewById(R.id.exercise_count_type_spinner);
                        String countType = countTypeSpinner.getSelectedItem().toString();

                        Exercise newExercise = new Exercise(exerciseName, countType, exerciseCount);
                        exercises.add(newExercise);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: Maybe do something here?
                    }
                });
        builder.create().show();

        final EditText planNameInput = findViewById(R.id.workout_plan_name);
        planNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    public void addPlan(View view) {

        if (!validatePlanName()) {
            return;
        }

        planName = planNameInput.getText().toString();

        mDataBase = FirebaseDatabase.getInstance().getReference().child("plans").child(planName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists() || isEdit != "No") {

                    if(exercises.size() >= 1){
                        mDataBase = FirebaseDatabase.getInstance().getReference();
                        Plan newPlan = new Plan(planName, exercises);
                        mDataBase.child("plans").child(isEdit).removeValue();
                        mDataBase.child("plans").child(planName).setValue(newPlan);
                        finish();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivityForm.this);
                        builder.setMessage("You should pick at least 1 exercise")
                                .setTitle("Add More Exercises")
                                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) { }
                                })
                                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.create().show();
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivityForm.this);
                    builder.setMessage("This plan name already exists! Please use a different name or go back to the plans menu.")
                            .setTitle("Plan Name Taken")
                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) { }
                            })
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    builder.create().show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDataBase.addListenerForSingleValueEvent(valueEventListener);
    }

    private boolean validatePlanName() {

        boolean valid = true;

        String name = planNameInput.getText().toString();

        if (name.length() < 3) {
            planNameInput.setError("Minimum 3 characters");
            valid = false;
        } else {
            planNameInput.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_NEW_PLAN_REQUEST) {
            if (resultCode == RESULT_OK) {

                planNameFromIntent = getIntent().getStringExtra("planName");

            }
        }
    }


}
