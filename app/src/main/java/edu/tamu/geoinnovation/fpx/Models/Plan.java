package edu.tamu.geoinnovation.fpx.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseramos on 3/28/18.
 */

public class Plan {
    public String name;
    public List<Exercise> exerciseList = new ArrayList<>();

    public Plan(){

    }

    public Plan(String name, List<Exercise> exerciseList){
        this.name = name;
        this.exerciseList = exerciseList;
    }

    public int getSize(){
        return exerciseList.size();
    }

}

