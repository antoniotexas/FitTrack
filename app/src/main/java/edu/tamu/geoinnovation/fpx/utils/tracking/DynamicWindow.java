package edu.tamu.geoinnovation.fpx.utils.tracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicWindow {
	private static final double peakCountingAlpha = 0.25;
	private static final int EXERCISE_CONSTANT_AFTER = 5;
	private static final int REP_INCREASE_THRESHOLD = 5;


	private List<Double> x, y, z;
	private List<Double> tempX, tempY, tempZ;
	private EMAverage averageX, averageY, averageZ;
	private List<String> windowLabels;
	private HashMap<String, Integer> labelCounts;
	private double startTime, endTime;

	private int currentReps = 0;
	private String finalExercise = null;


	DynamicWindow(List<Window> windowList) {
		this.x = new ArrayList<>();
		this.y = new ArrayList<>();
		this.z = new ArrayList<>();

		this.tempX = new ArrayList<>();
		this.tempY = new ArrayList<>();
		this.tempZ = new ArrayList<>();

		this.averageX = new EMAverage(peakCountingAlpha);
		this.averageY = new EMAverage(peakCountingAlpha);
		this.averageZ = new EMAverage(peakCountingAlpha);

		this.windowLabels = new ArrayList<>();
		this.labelCounts = new HashMap<>();

		for (Window window : windowList) {
			boolean quarantine = (window.getLabel().equals("No Workout"));
			addAveragedCoords(window.getCoords('x'), 'x', quarantine);
			addAveragedCoords(window.getCoords('y'), 'y', quarantine);
			addAveragedCoords(window.getCoords('z'), 'z', quarantine);
			addLabel(window.getLabel());
		}

		this.startTime = windowList.get(0).getStartTime();
		this.endTime = windowList.get(windowList.size() - 1).getEndTime();
	}

	private void addAveragedCoords(List<Double> coords, char type, boolean quarantine) {
		List<Double> averagedCoords = new ArrayList<>();
		EMAverage currentAverage = null;
		switch(type) {
			case 'y':
				currentAverage = this.averageY;
				break;
			case 'z':
				currentAverage = this.averageZ;
				break;
			case 'x':
				currentAverage = this.averageX;
				break;
		}

		for(Double coord : coords) {
			averagedCoords.add(currentAverage.average(coord));
		}


		if(quarantine) {
			switch(type) {
				case 'x':
					this.tempX.addAll(averagedCoords);
					break;
				case 'y':
					this.tempY.addAll(averagedCoords);
					break;
				case 'z':
					this.tempZ.addAll(averagedCoords);
					break;
			}
		} else {
			switch(type) {
				case 'x':
					this.x.addAll(tempX);
					this.x.addAll(averagedCoords);
					this.tempX.clear();
					break;
				case 'y':
					this.y.addAll(tempY);
					this.y.addAll(averagedCoords);
					this.tempY.clear();
					break;
				case 'z':
					this.z.addAll(tempZ);
					this.z.addAll(averagedCoords);
					this.tempZ.clear();
					break;
			}
		}
	}

	private void addLabel(String label) {
		this.windowLabels.add(label);
		Integer count = labelCounts.get(label);
		if (count == null) labelCounts.put(label, 1);
		else labelCounts.put(label, count + 1);
	}

	void addWindow(Window newWindow) {
		boolean quarantine = (newWindow.getLabel().equals("No Workout") || ! WorkoutCorrelations.closeEnough(determineExercise(), newWindow.getLabel()));
		addAveragedCoords(newWindow.getCoords('x'), 'x', quarantine);
		addAveragedCoords(newWindow.getCoords('y'), 'y', quarantine);
		addAveragedCoords(newWindow.getCoords('z'), 'z', quarantine);
		addLabel(newWindow.getLabel());

		if(!newWindow.getLabel().equals("No Workout")) {
			this.endTime = newWindow.getEndTime();
		}
	}

	public String determineExercise() {
		if(finalExercise != null) return finalExercise;

		String estimatedExercise = "No Workout";
		int highestLabelCount = 0;

		for (Map.Entry<String, Integer> entry : labelCounts.entrySet()) {
			if (entry.getValue() > highestLabelCount) {
				estimatedExercise = entry.getKey();
				highestLabelCount = entry.getValue();
			}
		}
		if(windowLabels.size() <= EXERCISE_CONSTANT_AFTER) finalExercise = estimatedExercise;

		return estimatedExercise;
	}

	private double getDynamicWindowTime() {
		return endTime - startTime;
	}

	public int calculateReps() {
		String currentExercise = determineExercise();
		switch (currentExercise) {
			case "Running":
			case "Walking":
			case "Stairs":
				return (int) getDynamicWindowTime();
			case "Situps":
			case "Shoulder Press":
				return PeakCounter.countPeaks(x);
			case "Pulldowns":
			case "Flys":
				return PeakCounter.countPeaks(z);
			case "Jumping Jacks":
				return PeakCounter.countPeaks(y);
			case "Squats":
				return PeakCounter.countPeaks(x) / 2;
			default:
				double xVariance = PeakCounter.countPeaks(x);
				double yVariance = PeakCounter.getVariance(y);
				double zVariance = PeakCounter.getVariance(z);
				List<Double> bestVarianceCoords;

				if (xVariance >= yVariance && xVariance >= zVariance) bestVarianceCoords = x;
				else if (yVariance >= xVariance && yVariance >= zVariance) bestVarianceCoords = y;
				else bestVarianceCoords = y;

				return PeakCounter.countPeaks(bestVarianceCoords);
		}
	}

	public int getReps() {
		int calculatedReps = calculateReps();
		if(currentReps >= REP_INCREASE_THRESHOLD) {
			if(calculatedReps >= currentReps) {
				currentReps = calculatedReps;
				return calculatedReps;
			} else {
				return currentReps;
			}
		} else {
			currentReps = calculatedReps;
			return calculatedReps;
		}
	}

	boolean isNoLongerActive() {
		String preWinLabel = windowLabels.get(windowLabels.size() - 2);
		String curWinLabel = windowLabels.get(windowLabels.size() - 1);
		String overallLabel = determineExercise();

		if(! WorkoutCorrelations.closeEnough(overallLabel, curWinLabel)) {
			if(preWinLabel.equals("No Workout") || preWinLabel.equals(curWinLabel)) {
				// TODO: Drop last windows.
				return true;
			}
		}
		return false;
		// TODO: I don't think the above code quarantines bad data.
	}

	void trimEnd() {
		int labelCount = windowLabels.size();
		windowLabels.remove(labelCount - 1);
		windowLabels.remove(labelCount - 2);
	}
}