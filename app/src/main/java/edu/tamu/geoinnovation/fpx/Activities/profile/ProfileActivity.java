package edu.tamu.geoinnovation.fpx.activities.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.tamu.geoinnovation.fpx.Models.Plan;
import edu.tamu.geoinnovation.fpx.Models.Profile;
import edu.tamu.geoinnovation.fpx.R;

public class ProfileActivity extends AppCompatActivity {

    GridView gridView;
    DatabaseReference ref;
    ArrayList<Profile> exercisesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar navbar = findViewById(R.id.navigation_bar);
        setTitle("Profile");
        setSupportActionBar(navbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        exercisesList = new ArrayList<>();

        loadExercises();

    }

    public void loadExercises() {

        ref = FirebaseDatabase.getInstance().getReference().child("profile");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            exercisesList.add(child.getValue(Profile.class));

                        }
                        upDateScreen();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    public void upDateScreen() {
        ProfileFeatureAdapter adapter = new ProfileFeatureAdapter(ProfileActivity.this, exercisesList);
        gridView = findViewById(R.id.grid_view);
        gridView.setAdapter(adapter);

    }




























    /*
    public void loadWeightGraph(){
        GraphView graph = findViewById(R.id.weight_graph);

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
            new DataPoint(d1, 1), new DataPoint(d2, 5), new DataPoint(d3, 3)
        });

        graph.addSeries(series);
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);

        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);

        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);

        // activate vertical scrolling
        graph.getViewport().setScrollableY(true);

        // styling series
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(4);
    }*/
}
