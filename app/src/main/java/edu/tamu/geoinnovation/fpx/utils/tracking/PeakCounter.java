package edu.tamu.geoinnovation.fpx.utils.tracking;

import java.util.*;
import java.io.*;

class PeakCounter {

    //calculate quartiles
    public static List<Double> getQuartiles(List<Double> values){
        List<Double> quartiles = new ArrayList<Double>();
        ArrayList<Double> sorted = new ArrayList<Double>(values);
        Collections.sort(sorted);
        quartiles.add(sorted.get(sorted.size()/4));
        quartiles.add(sorted.get(sorted.size()/2));
        quartiles.add(sorted.get(sorted.size()/4 + sorted.size()/2));
        return quartiles;
    }

    //count peaks based on Q3 and Q1
    public static int countPeaks(List<Double> values){
        int count = 0;
        boolean peak = false;
        boolean aboveQ3;
        boolean aboveQ2;
        boolean aboveQ1;
        List<Double> quarts = getQuartiles(values);
        double q1 = quarts.get(0);
        double q2 = quarts.get(1);
        double q3 = quarts.get(2);

        for(int i = 0; i<values.size(); i++){
            double curr = values.get(i);
            aboveQ1 = curr > q1 ? true : false;
            aboveQ2 = curr > q2 ? true : false;
            aboveQ3 = curr > q3 ? true : false;
            //determine initial conditions
            if(i == 0){
                peak = aboveQ1 && aboveQ2 && aboveQ3 ? true : false;
                if(peak) count++;
            }
            //add to count if it goes above Q3 after being below Q1
            if(!(peak) && aboveQ3) count++;
            peak = peak && aboveQ1 || !(peak) && aboveQ3 ? true : false;
        }

        return count;
    }

    public static double getMean(List<Double> values){
        double sum = 0.0;
        for(double value : values)
            sum += value;
        return sum/values.size();
    }

    public static double getVariance(List<Double> values){
        double mean = getMean(values);
        double sumDiffs = 0.0;
        for(double value : values)
            sumDiffs += (value-mean)*(value-mean);
        return sumDiffs/(values.size()-1);
    }

    public static void main(String[] args){
        List<Double> xValues = new ArrayList<Double>();
        List<Double> yValues = new ArrayList<Double>();
        List<Double> zValues = new ArrayList<Double>();
        List<Double> eucValues = new ArrayList<Double>();
        File file;
        BufferedReader reader = null;
        if (args.length == 0){
            file = new File("data.txt");
        }else if(args.length == 1){
            file = new File(args[0]);
        }else{
            file = new File(args[0]);
        }

        try {

            reader = new BufferedReader(new FileReader(file));
            String line = null;
            String currentExercise = "";
            boolean firstPassed = false;
            double startTime = 0.0;
            double endTime = 0.0;
            System.out.println("\n\n");


            while ((line = reader.readLine()) != null) {
                List<String> params = Arrays.asList(line.split(","));
                if(!(params.get(5).equals(currentExercise)) && firstPassed){
                    //new test data
                    currentExercise = params.get(5);
                    //purge lists and output peak count
                    System.out.println(currentExercise + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("the peak count for X is :" + countPeaks(xValues) + " with a variance of: " + getVariance(xValues));
                    System.out.println("the peak count for Y is :" + countPeaks(yValues) + " with a variance of: " + getVariance(yValues));
                    System.out.println("the peak count for Z is :" + countPeaks(zValues) + " with a variance of: " + getVariance(zValues));
                    System.out.println("the peak count for Euc is :" + countPeaks(eucValues) + " with a variance of: " + getVariance(eucValues));
                    System.out.println("Time Elapsed: " + (endTime-startTime));
                    System.out.println("\n\n");
                    xValues.clear();
                    yValues.clear();
                    zValues.clear();
                    eucValues.clear();
                    startTime = Integer.parseInt(params.get(0)) + Integer.parseInt(params.get(1))/1000.0;
                }

                if(firstPassed == false){
                    startTime = Integer.parseInt(params.get(0)) + Integer.parseInt(params.get(1))/1000.0;
                }
                firstPassed = true;
                currentExercise = params.get(5);

                double x = Double.parseDouble(params.get(2));
                double y = Double.parseDouble(params.get(3));
                double z = Double.parseDouble(params.get(4));
                endTime = Integer.parseInt(params.get(0)) + Integer.parseInt(params.get(1))/1000.0;

                xValues.add(x);
                yValues.add(y);
                zValues.add(z);
                eucValues.add(Math.sqrt(x*x + y*y + z*z));

            }

            System.out.println(currentExercise + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("the peak count for X is :" + countPeaks(xValues) + " with a variance of: " + getVariance(xValues));
            System.out.println("the peak count for Y is :" + countPeaks(yValues) + " with a variance of: " + getVariance(yValues));
            System.out.println("the peak count for Z is :" + countPeaks(zValues) + " with a variance of: " + getVariance(zValues));
            System.out.println("the peak count for Euc is :" + countPeaks(eucValues) + " with a variance of: " + getVariance(eucValues));
            System.out.println("Time Elapsed: " + (endTime-startTime));
            System.out.println("\n\n\n");

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