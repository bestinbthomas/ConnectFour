package com.example.connectfour;

import android.support.annotation.Nullable;

public class Result{
    public int Row;
    public int score;
    Result(@Nullable int row,int scor){
        Row = row;
        score = scor;
    }
}
