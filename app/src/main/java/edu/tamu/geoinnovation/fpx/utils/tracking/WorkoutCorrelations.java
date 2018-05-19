package edu.tamu.geoinnovation.fpx.utils.tracking;

import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class WorkoutCorrelations {

	static List<Double> a = new ArrayList<Double>(Arrays.asList(0.455, 0.921, 0.948, 0.993, 1.000, 0.998, 1.000, 1.000, 1.000, 0.996, 1.000, 0.954, 1.000, 1.000, 0.894));
	static List<Double> b = new ArrayList<Double>(Arrays.asList(0.848, 0.455, 0.941, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 0.983, 1.000, 1.000, 0.911));
	static List<Double> c = new ArrayList<Double>(Arrays.asList(0.910, 0.967, 0.455, 0.956, 1.000, 0.992, 0.992, 1.000, 1.000, 1.000, 1.000, 0.985, 1.000, 1.000, 0.871));
	static List<Double> d = new ArrayList<Double>(Arrays.asList(0.993, 1.000, 0.962, 0.455, 0.987, 0.990, 0.993, 1.000, 1.000, 1.000, 0.993, 1.000, 0.997, 1.000, 0.912));
	static List<Double> e = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 1.000, 0.978, 0.455, 0.949, 1.000, 0.991, 0.996, 1.000, 1.000, 0.996, 1.000, 1.000, 0.854));
	static List<Double> f = new ArrayList<Double>(Arrays.asList(0.960, 1.000, 0.979, 0.941, 0.798, 0.455, 0.841, 0.960, 1.000, 1.000, 1.000, 0.941, 1.000, 0.960, 0.613));
	static List<Double> g = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 0.997, 0.997, 1.000, 0.976, 0.455, 0.992, 1.000, 1.000, 0.995, 1.000, 0.992, 1.000, 0.956));
	static List<Double> h = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 1.000, 1.000, 0.997, 0.990, 0.992, 0.455, 1.000, 1.000, 1.000, 1.000, 0.995, 0.995, 0.803));
	static List<Double> i = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 1.000, 1.000, 0.985, 1.000, 1.000, 1.000, 0.455, 1.000, 1.000, 1.000, 1.000, 1.000, 0.768));
	static List<Double> j = new ArrayList<Double>(Arrays.asList(0.988, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 1.000, 0.455, 1.000, 0.922, 1.000, 1.000, 0.789));
	static List<Double> k = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 1.000, 0.985, 1.000, 1.000, 0.942, 1.000, 1.000, 1.000, 0.455, 1.000, 1.000, 1.000, 0.878));
	static List<Double> l = new ArrayList<Double>(Arrays.asList(0.917, 0.985, 0.993, 1.000, 0.993, 0.989, 1.000, 1.000, 1.000, 0.971, 1.000, 0.455, 1.000, 0.993, 0.820));
	static List<Double> m = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 1.000, 0.862, 1.000, 1.000, 0.806, 0.926, 1.000, 1.000, 1.000, 1.000, 0.455, 1.000, 0.862));
	static List<Double> n = new ArrayList<Double>(Arrays.asList(1.000, 1.000, 1.000, 1.000, 1.000, 0.981, 1.000, 0.981, 1.000, 1.000, 1.000, 0.981, 1.000, 0.455, 0.882));
	static List<Double> o = new ArrayList<Double>(Arrays.asList(0.979, 0.992, 0.990, 0.991, 0.990, 0.989, 0.993, 0.969, 0.988, 0.993, 0.997, 0.981, 1.000, 0.998, 0.455));
	static List<List<Double>> corr = new ArrayList<List<Double>>();
	static double closeEnoughThreshhold = 1.0;
	static ArrayList<String> workouts = new ArrayList<String>(Arrays.asList("Bench Press", "Shoulder Press", "Flys", "Curls", "Pulldowns", "Stairs", "Running", "Walking", "Situps", "Pushups", "Jumping Jacks", "Squats", "Jump Rope", "Step ups", "No Workout"));


	/*
	[  0.455  0.921  0.948  0.993  1.000  0.998  1.000  1.000  1.000  0.996  1.000  0.954  1.000  1.000  0.894  ] |    a =  Bench Press
	[  0.848  0.455  0.941  1.000  1.000  1.000  1.000  1.000  1.000  1.000  1.000  0.983  1.000  1.000  0.911  ] |    b =  Shoulder Press
	[  0.910  0.967  0.455  0.956  1.000  0.992  0.992  1.000  1.000  1.000  1.000  0.985  1.000  1.000  0.871  ] |    c =  Flys
	[  0.993  1.000  0.962  0.455  0.987  0.990  0.993  1.000  1.000  1.000  0.993  1.000  0.997  1.000  0.912  ] |    d =  Curls
	[  1.000  1.000  1.000  0.978  0.455  0.949  1.000  0.991  0.996  1.000  1.000  0.996  1.000  1.000  0.854  ] |    e =  Pulldowns
	[  0.960  1.000  0.979  0.941  0.798  0.455  0.841  0.960  1.000  1.000  1.000  0.941  1.000  0.960  0.613  ] |    f =  Stairs
	[  1.000  1.000  0.997  0.997  1.000  0.976  0.455  0.992  1.000  1.000  0.995  1.000  0.992  1.000  0.956  ] |    g =  Running
	[  1.000  1.000  1.000  1.000  0.997  0.990  0.992  0.455  1.000  1.000  1.000  1.000  0.995  0.995  0.803  ] |    h =  Walking
	[  1.000  1.000  1.000  1.000  0.985  1.000  1.000  1.000  0.455  1.000  1.000  1.000  1.000  1.000  0.768  ] |    i =  Situps
	[  0.988  1.000  1.000  1.000  1.000  1.000  1.000  1.000  1.000  0.455  1.000  0.922  1.000  1.000  0.789  ] |    j =  Pushups
	[  1.000  1.000  1.000  0.985  1.000  1.000  0.942  1.000  1.000  1.000  0.455  1.000  1.000  1.000  0.878  ] |    k =  Jumping Jacks
	[  0.917  0.985  0.993  1.000  0.993  0.989  1.000  1.000  1.000  0.971  1.000  0.455  1.000  0.993  0.820  ] |    l =  Deadlifts
	[  1.000  1.000  1.000  0.862  1.000  1.000  0.806  0.926  1.000  1.000  1.000  1.000  0.455  1.000  0.862  ] |    m =  Jump Rope
	[  1.000  1.000  1.000  1.000  1.000  0.981  1.000  0.981  1.000  1.000  1.000  0.981  1.000  0.455  0.882  ] |    n =  Step ups
	[  0.979  0.992  0.990  0.991  0.990  0.989  0.993  0.969  0.988  0.993  0.997  0.981  1.000  0.998  0.455  ] |    o =  No Workout
	*/

	private static void populateCorr() {
		corr.add(a);
		corr.add(b);
		corr.add(c);
		corr.add(d);
		corr.add(f);
		corr.add(f);
		corr.add(g);
		corr.add(h);
		corr.add(i);
		corr.add(j);
		corr.add(k);
		corr.add(l);
		corr.add(m);
		corr.add(n);
		corr.add(o);
	}

	public static boolean closeEnough(String expected, String actual) {
		if (actual.equals("No Workout")) return false;
		if (corr.isEmpty() || corr.get(0) == null || corr.get(0).get(0) == null) populateCorr();
		int toldyou = workouts.indexOf(expected);
		int whatdid = workouts.indexOf(actual);
		double test = corr.get(toldyou).get(whatdid);
		if (test > 0.921) return false;
		return true;
	}

	public static void main(String[] args) {
		File file;
		double beta;
		BufferedReader reader = null;
		if (args.length == 0) {
			file = new File("data.txt");
			beta = 1;
		}
		if (args.length == 1) {
			file = new File(args[0]);
			beta = 1;
		} else {
			file = new File(args[0]);
			beta = Double.parseDouble(args[1]);
		}

		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			double currentTime = 0.0;
			boolean atMatrix = false;
			List<List<Integer>> cMatrix = new ArrayList<List<Integer>>();
			ArrayList<String> labels = new ArrayList<String>();

			while ((line = reader.readLine()) != null) {
				if (line.contains("=== Confusion Matrix ===")) {
					reader.readLine();
					reader.readLine();
					line = reader.readLine();
					atMatrix = true;
				}
				if (atMatrix) {
					if (line.indexOf("|") != -1)
						labels.add(line.substring(line.indexOf("|"), line.length()));
					List<String> params = Arrays.asList(line.split("\\s+"));
					if (params.size() <= 1) break;
					int count = 0;
					int breakIndex = 0;
					for (String s : params) {
						s = s.trim();
						if (s.equals("|")) breakIndex = count;
						count++;
					}
					List<Integer> rowNum = new ArrayList<Integer>();
					for (String s : params.subList(1, breakIndex)) {
						rowNum.add(Integer.parseInt(s));
					}
					cMatrix.add(rowNum);
				}
			}

			int count = 0;
			System.out.println("\n\n\n===== Confusion Matrix =====");
			for (List<Integer> list : cMatrix) {
				System.out.print("[ ");
				for (int i : list) {
					System.out.print(String.format("%6d", i) + " ");
				}
				System.out.print(" ] ");
				System.out.println(labels.get(count));
				count++;
			}

			List<List<Double>> correlations = new ArrayList<List<Double>>();

			for (int i = 0; i < cMatrix.size(); i++) {
				correlations.add(new ArrayList<Double>());
				for (int j = 0; j < cMatrix.size(); j++) {
					correlations.get(i).add(((1 + beta * beta) * cMatrix.get(i).get(i)) / ((1 + beta * beta) * cMatrix.get(i).get(i) + beta * cMatrix.get(i).get(j) + cMatrix.get(j).get(i)));
				}
			}

			count = 0;
			System.out.println("\n\n\n===== Correlation Matrix =====");
			for (List<Double> list : correlations) {
				System.out.print("[ ");
				for (double d : list) {
					System.out.print(String.format("%6.3f", d) + " ");
				}
				System.out.print(" ] ");
				System.out.println(labels.get(count));
				count++;
			}


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}

		return;
	}
}