package edu.tamu.geoinnovation.fpx.activities.goals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.tamu.geoinnovation.fpx.Models.Goal;
import edu.tamu.geoinnovation.fpx.R;

import static edu.tamu.geoinnovation.fpx.Models.Goal.randomObjective;
import static edu.tamu.geoinnovation.fpx.Models.Goal.randomRepetitionType;

public class GoalsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goals);
        Toolbar navbar = findViewById(R.id.navigation_bar);
        setTitle("Goals");
        setSupportActionBar(navbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadGoals();

        final View add_goal_action = findViewById(R.id.add_goal_action);
        add_goal_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addGoalFormActivity = new Intent(getApplicationContext(), AddGoalsFormActivity.class);
                startActivity(addGoalFormActivity);
            }
        });
    }

    public void loadGoals() {
        recyclerView = findViewById(R.id.current_goals_container);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Goal> input = new ArrayList<>();
        for (int i = 0; i < rand.nextInt(5) + 5; i++) {
            Goal curGoal = generateGoal(i);
            input.add(curGoal);
        }

        mAdapter = new GoalsAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }

    private Goal generateGoal(int i) {
        Date startDate = generateRandomDate(1, 105, Calendar.getInstance().get(Calendar.YEAR));
        Date endDate = generateRandomDate(110, 225, Calendar.getInstance().get(Calendar.YEAR));

        return new Goal("Goal " + i, startDate, endDate, randomObjective(), randomRepetitionType(), 1);
    }

    private Date generateRandomDate(int minDay, int maxDay, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, rand.nextInt(maxDay - minDay) + minDay);
        return calendar.getTime();
    }

    /*


    public void calender(){

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
            }

        };



        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(PlanActivityForm.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });


    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }
     */
}
