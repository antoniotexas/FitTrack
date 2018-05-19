package edu.tamu.geoinnovation.fpx.utils.tracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abanoub Doss on 4/23/2018.
 */

public class DynamicWindowManager {
    private static final double DW_ACTIVITY_THRESHOLD = 0.60;
    private static final double alpha = 0.25;
    private static final int windowSize = 2;

    private EMAverage averageX = new EMAverage(alpha);
    private EMAverage averageY = new EMAverage(alpha);
    private EMAverage averageZ = new EMAverage(alpha);

    private DynamicWindow curDW = null;
    private List<Window> potentialDW = new ArrayList<>();
    private List<Window> allWindows = new ArrayList<>();
    public List<DynamicWindow> allDWs = new ArrayList<>();

    public void addData(double time, double xValue, double yValue, double zValue) {
        double normalizedX = averageX.average(xValue);
        double normalizedY = averageY.average(yValue);
        double normalizedZ = averageZ.average(zValue);

        if (allWindows.isEmpty()) {
            Window newWindow = new Window(time, normalizedX, normalizedY, normalizedZ, "");
            allWindows.add(newWindow);
        } else {
            Window lastWindow = allWindows.get(allWindows.size() - 1);
            if (time - lastWindow.getStartTime() > windowSize) {
                lastWindow.setLastTimeAsEndTime();
                Tree25RF activityTree = lastWindow.calculateActivity();
                lastWindow.setLabel(activityTree.getActivity());

                updateDW();

                Window newWindow = new Window(time, normalizedX, normalizedY, normalizedZ, "");
                allWindows.add(newWindow);
            } else {
                lastWindow.addValues(time, normalizedX, normalizedY, normalizedZ);
                lastWindow.addRawValues(xValue, yValue, zValue);
            }
        }
    }

    private boolean isPotentialDWValid() {
        // Rule 1: Can't start with No Workout
        if (potentialDW.get(0).getLabel().equals("No Workout")) {
            return false;
        }

        // Rule 2: Max 2 consecutive inactive
        String lastWorkoutLabel = "";
        double noActivityWorkouts = 0.0;
        for (Window curWindow : potentialDW) {
            if (curWindow.getLabel().equals("No Workout")) {
                noActivityWorkouts += 1.0;
                if (curWindow.getLabel().equals(lastWorkoutLabel)) {
                    return false;
                }
            }
        }

        double noActivityPercentage = noActivityWorkouts / ((double) potentialDW.size());

        // Rule 3: % of windows with "No Activity" >= (1.0 - DW_ACTIVITY_THRESHOLD)
        return !(noActivityWorkouts >= 1.0 && noActivityPercentage >= (1.0 - DW_ACTIVITY_THRESHOLD));
    }

    private void updateDW() {
        if (curDW == null) {
            Window lastWindow = allWindows.get(allWindows.size() - 1);

            if (!potentialDW.isEmpty() || (potentialDW.isEmpty() && !lastWindow.getLabel().equals("No Workout"))) {
                potentialDW.add(lastWindow);

                if (potentialDW.size() >= 5) {
                    if (isPotentialDWValid()) {
                        curDW = new DynamicWindow(potentialDW);
                        potentialDW.clear();
                        allDWs.add(curDW);
                    } else {
                        potentialDW.remove(0);
                    }
                }
            }
        } else {
            curDW.addWindow(allWindows.get(allWindows.size() - 1));
            if (curDW.isNoLongerActive()) {
                curDW.trimEnd();
                if(curDW.determineExercise().equals("No Workout")) {
                    allDWs.remove(allDWs.size() - 1);
                }
                curDW = null;
            }
        }
    }

    public boolean isUserActive() {
        return (curDW != null);
    }

    public String currentExercise() {
        return (curDW == null) ? "No Workout" : curDW.determineExercise();
    }

    public String currentReps() {
        if(curDW == null) {
            return "0 reps";
        } else {
            if(currentExercise().equals("Running") || currentExercise().equals("Stairs")) {
                return curDW.getReps() + " seconds";
            } else {
                return curDW.getReps() + " reps";
            }
        }
    }

    public int repsProgress() {
        return (curDW == null) ? 0 : curDW.getReps() % 10;
    }
}
