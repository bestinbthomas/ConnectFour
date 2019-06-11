package com.example.connectfour;

import android.support.annotation.Nullable;

public class Result{
    public int Row;
    public int score;
    Result(int scor){
        score = scor;
    }
    Result(int row ,int scor){
        Row = row;
        score = scor;
    }
}
