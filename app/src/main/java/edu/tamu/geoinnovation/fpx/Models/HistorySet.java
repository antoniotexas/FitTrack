package edu.tamu.geoinnovation.fpx.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by matt1 on 3/28/2018.
 */

public class HistorySet {
    public ArrayList<String> ourlist;
    public String username;
    public String exercise;
    public Integer reps;
    public String datetime;

    public HistorySet(String x, String y, Integer z, String a) {
        this.username = x;
        this.exercise = y;
        this.reps = z;
        this.datetime = a;
        this.ourlist = new ArrayList<String>(Arrays.asList(x, y, Integer.toString(z)+" reps", a));
    }
}