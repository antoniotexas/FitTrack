package edu.tamu.geoinnovation.fpx.utils.tracking;

import java.util.*;
import java.io.*;

public class EMAverage {
    private double alpha;
    private Double oldValue;
    public EMAverage(double alpha) {
        this.alpha = alpha;
    }

    public void purge(){
        oldValue = null;
    }

    public double average(double value) {
        if (oldValue == null) {
            oldValue = value;
            return value;
        }
        double newValue = oldValue + alpha * (value - oldValue);
        oldValue = newValue;
        return newValue;
    }
}