package edu.tamu.geoinnovation.fpx.utils.tracking;

import java.util.*;

public class Window{

	List<Double> x;
	List<Double> y;
	List<Double> z;

	List<Double> rawX;
	List<Double> rawY;
	List<Double> rawZ;

	List<Double> x2;
	List<Double> y2;
	List<Double> z2;
	List<Double> euc;
	List<Double> euc2;
	List<Double> jerk;
	List<Double> times;
	double startTime;
	double endTime;
	String label;


	Tree25RF tree;

	public Window(double time, int x, int y, int z, String label){
		this.times = new ArrayList<Double>();
		this.x = new ArrayList<Double>();
		this.y = new ArrayList<Double>();
		this.z = new ArrayList<Double>();
		this.x2 = new ArrayList<Double>();
		this.y2 = new ArrayList<Double>();
		this.z2 = new ArrayList<Double>();
		this.euc = new ArrayList<Double>();
		this.euc2 = new ArrayList<Double>();
		this.jerk = new ArrayList<Double>();

		times.add(time);
		this.x.add(x+0.0);
		this.y.add(y+0.0);
		this.z.add(z+0.0);
		this.x2.add(x*x+0.0);
		this.y2.add(y*y+0.0);
		this.z2.add(z*z+0.0);
		double euclidean = Math.sqrt(x*x+y*y+z*z);
		euc.add(euclidean);
		euc2.add(euclidean*euclidean);
		this.label =label;
		startTime = time;
	}

	public Window(double time, double x, double y, double z, String label){
		this.times = new ArrayList<Double>();
		this.x = new ArrayList<Double>();
		this.y = new ArrayList<Double>();
		this.z = new ArrayList<Double>();
		this.rawX = new ArrayList<Double>();
		this.rawY = new ArrayList<Double>();
		this.rawZ = new ArrayList<Double>();
		this.x2 = new ArrayList<Double>();
		this.y2 = new ArrayList<Double>();
		this.z2 = new ArrayList<Double>();
		this.euc = new ArrayList<Double>();
		this.euc2 = new ArrayList<Double>();
		this.jerk = new ArrayList<Double>();

		times.add(time);
		this.x.add(x+0.0);
		this.y.add(y+0.0);
		this.z.add(z+0.0);
		this.x2.add(x*x+0.0);
		this.y2.add(y*y+0.0);
		this.z2.add(z*z+0.0);
		double euclidean = Math.sqrt(x*x+y*y+z*z);
		euc.add(euclidean);
		euc2.add(euclidean*euclidean);
		this.label = label;
		startTime = time;
	}

	public List<Double> getCoords(char type) {
		switch(type) {
			case 'x':
				return rawX;
			case 'y':
				return rawY;
			case 'z':
				return rawZ;
			default:
				return Collections.<Double>emptyList();
		}
	}

	public List<Double> getCoords() {
		return x;
	}
	public List<Double> getXCoords() {
		return x;
	}

	public void addValues(double time, int x, int y, int z){
		times.add(time);
		this.x.add(x+0.0);
		this.y.add(y+0.0);
		this.z.add(z+0.0);
		this.x2.add(x*x+0.0);
		this.y2.add(y*y+0.0);
		this.z2.add(z*z+0.0);
		double euclidean = Math.sqrt(x*x+y*y+z*z);
		euc.add(euclidean);
		euc2.add(euclidean*euclidean);
		jerk.add((euclidean - euc.get(euc.size()-2)) / (time - times.get(times.size()-2)));
	}

	public void addValues(double time, double x, double y, double z){
		times.add(time);
		this.x.add(x+0.0);
		this.y.add(y+0.0);
		this.z.add(z+0.0);
		this.x2.add(x*x+0.0);
		this.y2.add(y*y+0.0);
		this.z2.add(z*z+0.0);
		double euclidean = Math.sqrt(x*x+y*y+z*z);
		euc.add(euclidean);
		euc2.add(euclidean*euclidean);
		jerk.add((euclidean - euc.get(euc.size()-2)) / (time - times.get(times.size()-2)));
	}


	public void addRawValues(double x, double y, double z) {
		this.rawX.add(x+0.0);
		this.rawY.add(y+0.0);
		this.rawZ.add(z+0.0);
	}

	public String getLabel(){
		return label;
	}

	public boolean differentLabel(String test){
		return !(label.equals(test));
	}

	public void setLastTimeAsEndTime(){
		// calculate end times once window is full
		endTime = times.get(times.size()-1);
		//calculate variance

	}

	public double getStartTime(){
		return startTime;
	}

	public double getEndTime(){
		return endTime;
	}

	public double getMean(List<Double> list){
		double d = 0.0;
		for(Double i : list)
			d += i;
		return d/list.size();
	}

	public double getStdDev(List<Double> list){
		double d = 0.0;
		for(Double i : list)
			d += i;
		double avg = d/list.size();
		double sd = 0;
		for(Double i : list)
			sd += Math.pow(i-avg,2) / list.size();
		return Math.sqrt(sd);
	}

	//Modify by Jose
	public double[] getStdDev(List<Double> list, double avg){

		double[] rtn = new double[2];
		double sd = 0;
		for(Double i : list)
			sd += Math.pow(i-avg,2) / list.size();
		rtn[0] = avg;
		rtn[1] = Math.sqrt(sd);
		//return avg + ",TE " + Math.sqrt(sd);
		return rtn;
	}


	public double getMax(List<Double> list){
		double d = Double.MIN_VALUE;
		for(Double i : list)
			if(i > d)
				d = i;
		return d;
	}

	public double getMin(List<Double> list){
		double d = Double.MAX_VALUE;
		for(Double i : list)
			if(i < d)
				d = i;
		return d;
	}


	public String toString(){
		return endTime - startTime + ", " + getStdDev(x,getMean(x)) + ", " + getMax(x) + ", " + getMin(x) +
				", " + getStdDev(y,getMean(y)) + ", " + getMax(y) + ", " + getMin(y) +
				", " + getStdDev(z,getMean(z)) + ", " + getMax(z) + ", " + getMin(z) +
				", " + getStdDev(x2,getMean(x2)) + ", " + getMax(x2) + ", " + getMin(x2) +
				", " + getStdDev(y2,getMean(y2)) + ", " + getMax(y2) + ", " + getMin(y2) +
				", " + getStdDev(z2,getMean(z2)) + ", " + getMax(z2) + ", " + getMin(z2) +
				", " + getStdDev(euc,getMean(euc)) + ", " + getMax(euc) + ", " + getMin(euc) +
				", " + getStdDev(euc,getMean(euc2)) + ", " + getMax(euc2) + ", " + getMin(euc2) +
				", " + getStdDev(euc,getMean(jerk)) + ", " + getMax(jerk) + ", " + getMin(jerk) + ", " + getLabel();
	}

	//Code by Jose
	public Tree25RF calculateActivity(){
		tree = new Tree25RF(endTime - startTime,getStdDev(x,getMean(x))[0],getStdDev(x,getMean(x))[1] ,getMax(x),getMin(x),
				getStdDev(y,getMean(y))[0],getStdDev(y,getMean(y))[1],getMax(y),getMin(y),
				getStdDev(z,getMean(z))[0],getStdDev(z,getMean(z))[1],getMax(z),getMin(z),
				getStdDev(x2,getMean(x2))[0],getStdDev(x2,getMean(x2))[1] ,getMax(x2),getMin(x2),
				getStdDev(y2,getMean(y2))[0],getStdDev(y2,getMean(y2))[1] ,getMax(y2) ,getMin(y2),
				getStdDev(z2,getMean(z2))[0],getStdDev(z2,getMean(z2))[1] ,getMax(z2),getMin(z2),
				getStdDev(euc,getMean(euc))[0],getStdDev(euc,getMean(euc))[1] ,getMax(euc),getMin(euc),
				getStdDev(euc,getMean(euc2))[0],getStdDev(euc,getMean(euc2))[1],getMax(euc2), getMin(euc2),
				getStdDev(euc,getMean(jerk))[0],getStdDev(euc,getMean(jerk))[1] , getMax(jerk),getMin(jerk));
		return tree;
	}

	public void setLabel(String curWorkout) {
		label = curWorkout;
	}



/*
	public static void main(String[] args){
		List<Window> windows = new ArrayList<Window>();
		File file;
		BufferedReader reader = null;
		int windowSize;
		if (args.length == 0){
			file = new File("data.txt");
			windowSize = 2;
		}else if(args.length == 1){
			file = new File(args[0]);
			windowSize = 2;
		}else{
			file = new File(args[0]);
			windowSize = Integer.parseInt(args[1]);
		}

		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			double currentTime = 0.0;


			while ((line = reader.readLine()) != null) {
				List<String> params = Arrays.asList(line.split(","));
				for(String s : params)
					s = s.trim();
				double time = Integer.parseInt(params.get(0)) + Integer.parseInt(params.get(1))/1000.0;
				if(time - currentTime >= windowSize || windows.size() == 0 || windows.get(windows.size()-1).differentLabel(params.get(5))){
					currentTime = time;
					double x = Double.parseDouble(params.get(2));
					double y = Double.parseDouble(params.get(3));
					double z = Double.parseDouble(params.get(4));

					if(windows.size() != 0){
						windows.get(windows.size()-1).setLastTimeAsEndTime();
					}
					Window window = new Window(time,x,y,z,params.get(5));
					windows.add(window);
				}else{
					double x = Double.parseDouble(params.get(2));
					double y = Double.parseDouble(params.get(3));
					double z = Double.parseDouble(params.get(4));
					windows.get(windows.size()-1).addValues(time,x,y,z);
				}
			}
			windows.get(windows.size()-1).setLastTimeAsEndTime();
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
		try{

			//prepare output file
			String filename = null;
			if(args.length != 0){
				String temp = args[0].substring(0,args[0].indexOf("."));
				filename = temp + "Windowed-Size" + windowSize + ".csv";
			}else{
				filename = "dataWindowed" + windowSize + ".csv";
			}

			File outFile = new File(filename);
			FileWriter fileWriter = new FileWriter(outFile);
			fileWriter.write("Time Range, Mean x, StdDev x, Max x, Min x, Mean y, StdDev y, Max y, Min y, Mean z, StdDev z, Max z, Min z, " +
					"Mean x2, StdDev x2, Max x2, Min x2, Mean y2, StdDev y2, Max y2, Min y2, Mean z2, StdDev z2, Max z2, Min z2, " +
					"Mean euc, StdDev euc, Max euc, Min euc, Mean euc2, StdDev euc2, Max euc2, Min euc2, " +
					"Mean jerk, StdDev jerk, Max jerk, Min jerk, Label\n");
			for(int i = 0; i < windows.size(); i++){
				Window w = windows.get(i);
				//if it is a first or last window, dont record it.
				if(i != 0 && i != windows.size()-1){
					//if it is a first or last window of an session, dont record it
					if(w.getStartTime() - windows.get(i-1).getEndTime() > windowSize+1){
						//this is the start of a new session, don't record this window
					}else if(windows.get(i+1).getStartTime() - w.getEndTime()> windowSize+1){
						//this is the end of a session, don't record it
					}else if(w.getEndTime() - w.getStartTime() < windowSize/2){
						//this window is too short to use
					}else{
						fileWriter.write(w.toString() + "\n");
						w.getActivityPerform(); // Added by Jose
					}
				}
			}
			fileWriter.flush();
			fileWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		return;
	}
	*/
}