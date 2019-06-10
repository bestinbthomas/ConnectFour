package com.example.connectfour;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button SinglePlayer, TwoPlayer;
    int col, row, p1c, p2c;
    public static final String COLUMNS = "getColumns";
    public static final String ROWS = "getRows";
    public static final String PLAYER1COLOR = "Player1color";
    public static final String PLAYER2COLOR = "Player2color";
    private Button G1, G2, G3, p1c1, p1c2, p1c3, p2c1, p2c2, p2c3;
    private View alertView;
    private MediaPlayer backPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideNavigationBar();

        SinglePlayer = findViewById(R.id.SPBtn);
        TwoPlayer = findViewById(R.id.TPBtn);
        col = 6;
        row = 7;
        p1c = R.color.RedLight;
        p2c = R.color.YellowLight;
        backPlayer = MediaPlayer.create(MainActivity.this,R.raw.background_music);
        backPlayer.setLooping(true);
        backPlayer.start();


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

        alertView = getLayoutInflater().inflate(R.layout.options_layout, null);
        G1 = alertView.findViewById(R.id.Grid1);
        G2 = alertView.findViewById(R.id.Grid2);
        G3 = alertView.findViewById(R.id.Grid3);
        p1c1 = alertView.findViewById(R.id.P1col1);
        p1c2 = alertView.findViewById(R.id.P1col2);
        p1c3 = alertView.findViewById(R.id.P1col3);
        p2c1 = alertView.findViewById(R.id.P2col1);
        p2c2 = alertView.findViewById(R.id.P2col2);
        p2c3 = alertView.findViewById(R.id.P2col3);

        SinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateAlert(true);

            }
        });

        TwoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateAlert(false);

            }
        });
        G1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ViewBuilder.TAG, "onClick: G1");
                row = 5;
                col = 4;
                G1.setBackgroundResource(R.drawable.selectedback);
                G2.setBackgroundResource(R.drawable.undo_back);
                G3.setBackgroundResource(R.drawable.undo_back);
            }
        });
        G2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ViewBuilder.TAG, "onClick: G2");
                row = 7;
                col = 6;
                G1.setBackgroundResource(R.drawable.undo_back);
                G2.setBackgroundResource(R.drawable.selectedback);
                G3.setBackgroundResource(R.drawable.undo_back);
            }
        });
        G3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ViewBuilder.TAG, "onClick: G3");
                row = 9;
                col = 8;
                G1.setBackgroundResource(R.drawable.undo_back);
                G2.setBackgroundResource(R.drawable.undo_back);
                G3.setBackgroundResource(R.drawable.selectedback);
            }
        });
        p1c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (p2c == R.color.RedLight)){
                    p1c = R.color.RedLight;
                    p1c1.setBackgroundResource(R.drawable.selectedback);
                    p1c2.setBackgroundResource(R.drawable.undo_back);
                    p1c3.setBackgroundResource(R.drawable.undo_back);
                }

            }
        });
        p1c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (p2c == R.color.GreenLight)){
                    p1c = R.color.GreenLight;
                    p1c1.setBackgroundResource(R.drawable.undo_back);
                    p1c2.setBackgroundResource(R.drawable.selectedback);
                    p1c3.setBackgroundResource(R.drawable.undo_back);
                }
            }
        });
        p1c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (p2c == R.color.YellowLight)){
                    p1c = R.color.YellowLight;
                    p1c1.setBackgroundResource(R.drawable.undo_back);
                    p1c2.setBackgroundResource(R.drawable.undo_back);
                    p1c3.setBackgroundResource(R.drawable.selectedback);
                }
            }
        });
        p2c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (p1c == R.color.RedLight)){
                    p2c = R.color.RedLight;
                    p2c1.setBackgroundResource(R.drawable.selectedback);
                    p2c2.setBackgroundResource(R.drawable.undo_back);
                    p2c3.setBackgroundResource(R.drawable.undo_back);
                }
            }
        });
        p2c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (p1c == R.color.GreenLight)){
                    p2c = R.color.GreenLight;
                    p2c1.setBackgroundResource(R.drawable.undo_back);
                    p2c2.setBackgroundResource(R.drawable.selectedback);
                    p2c3.setBackgroundResource(R.drawable.undo_back);
                }
            }
        });
        p2c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (p1c == R.color.YellowLight)){
                    p2c = R.color.YellowLight;
                    p2c1.setBackgroundResource(R.drawable.undo_back);
                    p2c2.setBackgroundResource(R.drawable.undo_back);
                    p2c3.setBackgroundResource(R.drawable.selectedback);
                }
            }
        });
    }

    private void hideNavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    boolean Single;
    private void inflateAlert(boolean isSingle) {
        Single = isSingle;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        TextView P1Txt, P2Txt;
        P1Txt = alertView.findViewById(R.id.Player1colTxt);
        P2Txt = alertView.findViewById(R.id.Player2colTxt);

        if (isSingle) {
            P1Txt.setText("Your Color");
            P2Txt.setText("AI Color");
        }
        else {
            P1Txt.setText("Player 1 color");
            P2Txt.setText("Player 2 color");
        }

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                if (Single)
                    intent = new Intent(getApplicationContext(), singlePlayerActivity.class);
                else
                    intent = new Intent(getApplicationContext(), twoPlayerActivity.class);
                intent.putExtra(COLUMNS, col);
                intent.putExtra(ROWS, row);
                intent.putExtra(PLAYER1COLOR, p1c);
                intent.putExtra(PLAYER2COLOR, p2c);
                startActivity(intent);
            }

        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = MainActivity.this;
                activity.recreate();
            }
        });
        mBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Activity activity = MainActivity.this;
                activity.recreate();
            }
        });

        mBuilder.setView(alertView);
        AlertDialog alert = mBuilder.create();
        alert.show();

    }

    @Override
    protected void onPause() {
        super.onPause();

        backPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backPlayer.release();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder backBuilder = new AlertDialog.Builder(MainActivity.this);
        backBuilder.setCancelable(false)
                .setTitle("Exit !!!")
                .setMessage("Do you really want to exit ?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                });
        AlertDialog alertDialog = backBuilder.create();
        alertDialog.show();
    }
}
