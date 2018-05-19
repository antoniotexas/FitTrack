package edu.tamu.geoinnovation.fpx.Models;

import java.util.Date;
import java.util.Random;

public class Goal {
    public enum Type {Challenge, Goal}
    public enum Objective {Plan, Workout, Exercise}
    public enum Repetition {Daily, Weekly, BiWeekly, Monthly}

    public String name;
    public Date startDate;
    public Type type;
    public Objective objective;

    // Goal-Specific Aspects
    public Date endDate;
    public Repetition repetitionType;
    public int repetition;

    // Challenge-Specific Aspects
    public int duration, count;
    public String workoutType;


    // Goal Constructor
    public Goal(String name, Date startDate, Date endDate, Objective objective, Repetition repetitionType, int repetition) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;

        this.objective = objective;
        this.repetitionType = repetitionType;
        this.repetition = repetition;

        this.type = Type.Goal;
    }


    // Challenge Constructor
    public Goal(String name, Date startDate, int duration, Objective objective, String workoutType, int count) {
        this.name = name;
        this.startDate = startDate;
        this.objective = objective;
        this.duration = duration;
        this.workoutType = workoutType;
        this.count = count;

        this.type = Type.Challenge;
    }

    public static Objective randomObjective() {
        Random rand = new Random();
        int obj = rand.nextInt(3) + 1;
        switch(obj) {
            case 1:
                return Objective.Exercise;
            case 2:
                return Objective.Plan;
            case 3:
                return Objective.Workout;
        }
        return Objective.Workout;
    }

    public static Repetition randomRepetitionType() {
        Random rand = new Random();
        int obj = rand.nextInt(4) + 1;
        switch(obj) {
            case 1:
                return Repetition.Daily;
            case 2:
                return Repetition.Weekly;
            case 3:
                return Repetition.BiWeekly;
            case 4:
                return Repetition.Monthly;
        }
        return Repetition.Daily;
    }

}
