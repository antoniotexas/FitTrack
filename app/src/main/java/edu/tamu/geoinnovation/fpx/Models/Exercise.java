package edu.tamu.geoinnovation.fpx.Models;

import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

import java.io.Serializable;

/**
 * Created by joseramos on 4/16/18.
 */

public class Exercise implements Serializable {
    public String name, countType, count;

    public Exercise(){}

    public Exercise(String name, String countType, String count) {
        this.name = name;
        this.countType = countType;
        this.count = count;
    }

    public void decrementCount() {
        count = Integer.toString(Integer.parseInt(count) - 1);
    }

    public void incrementCount() { count = Integer.toString(Integer.parseInt(count) + 1); }
}