package com.example.connectfour;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class singlePlayerActivity extends AppCompatActivity {

    private int columns,rows,player1col,player2col;
    private TextView player1tag,player2tag;
    private ViewBuilder viewBuilder;
    private MediaPlayer backPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        hideNavigationBar();

        viewBuilder = findViewById(R.id.MyView);
        player1tag = findViewById(R.id.Player1Tag);
        player2tag = findViewById(R.id.Player2Tag);
        Intent intent = getIntent();
        rows = intent.getIntExtra(MainActivity.ROWS,7);
        columns = intent.getIntExtra(MainActivity.COLUMNS,6);
        player1col = intent.getIntExtra(MainActivity.PLAYER1COLOR,R.color.RedLight);
        player2col = intent.getIntExtra(MainActivity.PLAYER2COLOR,R.color.YellowLight);
        viewBuilder.Setup(rows,columns,getResources().getColor(player1col),getResources().getColor(player2col));
        viewBuilder.giveContext(this);
        switch(player1col){
            case R.color.RedLight :
                player1tag.setBackgroundResource(R.drawable.player1_back_r);
                break;
            case R.color.GreenLight :
                player1tag.setBackgroundResource(R.drawable.player1_back_g);
                break;
            case R.color.YellowLight :
                player1tag.setBackgroundResource(R.drawable.player1_back_y);
                break;
        }
        switch(player2col){
            case R.color.RedLight :
                player2tag.setBackgroundResource(R.drawable.player2_back_r);
                break;
            case R.color.GreenLight :
                player2tag.setBackgroundResource(R.drawable.player2_back_g);
                break;
            case R.color.YellowLight :
                player2tag.setBackgroundResource(R.drawable.player2_back_y);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideNavigationBar();
        backPlayer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        backPlayer = MediaPlayer.create(singlePlayerActivity.this,R.raw.background_music);
        backPlayer.setLooping(true);
        backPlayer.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        backPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backPlayer.pause();
    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}
