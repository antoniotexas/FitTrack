package edu.tamu.geoinnovation.fpx.activities.plans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tamu.geoinnovation.fpx.Models.Exercise;
import edu.tamu.geoinnovation.fpx.Models.Plan;
import edu.tamu.geoinnovation.fpx.R;

public class PlanActivity extends AppCompatActivity {
    static final int CREATE_NEW_PLAN_REQUEST = 1;  // The request code
    private FloatingActionButton addWorkoutPlanBtn;
    private RecyclerView plansContainer;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Plan> plans;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout file and load the navigation bar.
        setContentView(R.layout.activity_plan);
        Toolbar navbar = findViewById(R.id.navigation_bar);
        setTitle("Plans");
        setSupportActionBar(navbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addWorkoutPlanBtn = findViewById(R.id.addPlanFAB);
        addWorkoutPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PlanActivityForm.class);
                startActivityForResult(intent, CREATE_NEW_PLAN_REQUEST);
            }
        });

        /* It was causing a NULL POINTER when ever I read from DB
        List<Exercise> exercises = new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Plan newPlan = new Plan("FirstEntry", exercises);
        mDatabase.child("plans").child("FirstEntry").setValue(newPlan);
        */

        // Initialize the recycler view that will show users the plans they currently have.
        plansContainer = findViewById(R.id.plans_container);
        plansContainer.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        plansContainer.setLayoutManager(layoutManager);

        plans = new ArrayList<>();
        mAdapter = new PlanAdapter(plans);
        plansContainer.setAdapter(mAdapter);


        ref = FirebaseDatabase.getInstance().getReference().child("plans");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //loadPlans((Map<String, Object>) dataSnapshot.getValue());
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            plans.add(child.getValue(Plan.class));
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


    }

    /*
    private void loadPlans(Map<String, Object> planList) {
        for (Map.Entry<String, Object> entry : planList.entrySet()) {
            List<Exercise> exerciseList = new ArrayList<>();
            plans.add(new Plan(entry.getKey(), exerciseList));
            mAdapter.notifyDataSetChanged();
        }
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_NEW_PLAN_REQUEST) {
            if (resultCode == RESULT_OK) {
                //TODO: Check if we need to do something with the database reference.
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
