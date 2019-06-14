package com.example.connectfour;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

public class ViewBuilder extends View  {

    private Paint mRectPaint,mRectPaintDark,mDiskPaint,fadedrectPaint,mTextPaint,winlinePaint;
    private Path holesPath;
    private Bitmap mBitmap;
    private Canvas SubCanvas;
    private float padding;
    private float Mtop;
    private float Mleft;
    private float Mright;
    private float Mbottom;
    private float Mrx;
    private float Mry;
    private float Crad;
    private float cx;
    private float cy;
    private int player1col;
    private int player2col;
    private float[][] Cxs;
    private float[][] Cys;
    private int width;
    private int height;
    private int rows;
    private int columns;
    private int i;
    private int j;
    private PlayGameTwo play;
    private int[][] status;
    public static final String TAG = "MYtag";
    private  Context ctx,viewctx;
    private boolean isdropping;
    private boolean isSingle;
    private MediaPlayer dropsound;
    private boolean isGameover;
    private boolean AIplaying;
    private AI_Task ai_task;

    public ViewBuilder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ViewBuilder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public ViewBuilder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    public ViewBuilder(Context context) {
        super(context);
        init(context,null);
    }


    public void init (Context context, @Nullable AttributeSet set){

        viewctx = context;
        if(set != null){
            TypedArray ta = viewctx.obtainStyledAttributes(set,R.styleable.ViewBuilder);
            rows = ta.getInt(R.styleable.ViewBuilder_Rows,7);
            columns = ta.getInt(R.styleable.ViewBuilder_Columns,6);
            player1col = ta.getColor(R.styleable.ViewBuilder_player_1_color,getResources().getColor(R.color.RedLight));
            player2col = ta.getColor(R.styleable.ViewBuilder_player_2_color,getResources().getColor(R.color.YellowLight));
            isSingle = ta.getBoolean((R.styleable.ViewBuilder_is_Single_player),false);
            ta.recycle();

        }



        dropsound = MediaPlayer.create(getContext(),R.raw.drop_music);
        isGameover = false;
        dropsound.setLooping(false);
        play = new PlayGameTwo(rows,columns,isSingle);
        holesPath = new Path();
        mRectPaint = new Paint();
        mRectPaintDark = new Paint();
        mDiskPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mTextPaint = new Paint();
        winlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        winlinePaint.setStrokeWidth(10);
        winlinePaint.setStyle(Paint.Style.STROKE);
        winlinePaint.setStrokeCap(Paint.Cap.ROUND);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,22,viewctx.getResources().getDisplayMetrics()));
        mTextPaint.setTypeface(Typeface.SANS_SERIF);
        fadedrectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fadedrectPaint.setColor(getResources().getColor(R.color.FadedRect));
        mRectPaint.setColor(getResources().getColor(R.color.BoxBlue));
        mRectPaintDark.setAntiAlias(true);
        mRectPaintDark.setColor(getResources().getColor(R.color.BoxBlueDark));
        mDiskPaint.setAntiAlias(true);
        Cxs = new float[columns][rows];
        Cys = new float[columns][rows];
        status = new int[columns][rows];
        status = play.getStatus();
        isdropping = false;
        invalidate();
    }

    public void Setup(int row,int col,int p1c,int p2c){
        rows = row;
        columns = col;
        player1col = p1c;
        player2col = p2c;
        init(viewctx,null);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        mBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        SubCanvas = new Canvas(mBitmap);
        padding = (width/rows)*0.15f;
        Crad = (((width-(3*padding))/rows)-padding)/2;
        Mrx = padding;
        Mry = padding;
        Mleft = padding;
        Mright = Mleft + padding + (rows*((2*Crad)+padding));
        Mtop = (height/2f)-((padding + ((columns)*((2*Crad)+padding)))/2);
        Mbottom = Mtop + padding + (columns*((2*Crad)+padding));



        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.save();
        cx = Mleft + padding + Crad;
        cy = Mtop + padding + Crad;
        for (i = 0; i < columns; i++) {
            for (j = 0; j < rows; j++) {
                holesPath.addCircle(cx, cy, Crad, Path.Direction.CW);
                Cxs[i][j] = cx;
                Cys[i][j] = cy;
                cx += padding + (2 * Crad);
            }
            cx = Mleft + padding + Crad;
            cy += padding + (2 * Crad);
        }
        if (isdropping)
            canvas.drawBitmap(mBitmap, 0, 0, new Paint());
        canvas.save();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            canvas.clipOutPath(holesPath);
        else
            canvas.clipPath(holesPath, Region.Op.DIFFERENCE);

        mRectPaintDark.setColor(getResources().getColor(R.color.BoxBlueDark));
        canvas.drawRoundRect(Mleft, Mtop, Mright + 8, Mbottom + 8, Mrx, Mry, mRectPaintDark);
        canvas.drawRoundRect(Mleft, Mtop, Mright, Mbottom, Mrx, Mry, mRectPaint);
        cy = Mtop + padding + Crad;
        for (i = 0; i < columns; i++) {
            for (j = 0; j < rows; j++) {
                mRectPaintDark.setColor(Color.WHITE);
                canvas.drawCircle(cx + 2, cy + 2, Crad + 1, mRectPaintDark);
                cx += padding + (2 * Crad);
            }
            cx = Mleft + padding + Crad;
            cy += padding + (2 * Crad);
        }
        cy = Mtop + padding + Crad;
        canvas.restore();

        for (i = 0; i < columns; i++) {
            for (j = 0; j < rows; j++) {
                if (status[i][j] == 1) {
                    mDiskPaint.setColor(player1col);
                    canvas.drawCircle(Cxs[i][j], Cys[i][j], Crad, mDiskPaint);
                } else if (status[i][j] == 2) {
                    mDiskPaint.setColor(player2col);
                    canvas.drawCircle(Cxs[i][j], Cys[i][j], Crad, mDiskPaint);
                }

            }
        }
        mTextPaint.setColor(Color.WHITE);
        if(isSingle){
            if (play.getIsPlayer1())
                canvas.drawText("Your turn", width / 2, Mtop - (mTextPaint.getTextSize() + 20) * 2, mTextPaint);
            else if(!play.getIsPlayer1() || AIplaying)
                canvas.drawText("Opponent Playing", width / 2, Mtop - (mTextPaint.getTextSize() + 20) * 2, mTextPaint);
            canvas.drawText("Tap on the grid to drop your disk ", width / 2, Mtop - mTextPaint.getTextSize() - 16, mTextPaint);
        }
        else
        {
            if (play.getIsPlayer1())
                canvas.drawText("PLayer 1's turn", width / 2, Mtop - (mTextPaint.getTextSize() + 20) * 2, mTextPaint);
            else
                canvas.drawText("PLayer 2's turn", width / 2, Mtop - (mTextPaint.getTextSize() + 20) * 2, mTextPaint);
            canvas.drawText("Tap on the grid to drop your disk ", width / 2, Mtop - mTextPaint.getTextSize() - 16, mTextPaint);
        }
        if(!isdropping) {

            canvas.drawBitmap(mBitmap, 0, 0, new Paint());
        }
        if(isGameover){
            Log.i(TAG, "onDraw: winline drawn");
            winlinePaint.setColor(player1col);
            if(play.getIsPlayer1())
                winlinePaint.setColor(player2col);
            mTextPaint.setColor(Color.BLACK);
            canvas.drawText("Tap anywhere to Restart",width/2f,height/2f,mTextPaint);
            canvas.drawLine(Cxs[play.x1][play.y1],Cys[play.x1][play.y1],Cxs[play.x2][play.y2],Cys[play.x2][play.y2],winlinePaint);
            Log.i(TAG, "onDraw: value from play"+play.x1+play.x2+play.y1+play.y2);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getY() > (Mtop-Crad) && event.getY() < Mbottom && !isdropping && !isGameover && !AIplaying) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    drawFadedRect(event.getX());
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawFadedRect(event.getX());
                    break;
                case MotionEvent.ACTION_UP:
                    confirmDrop(event.getX());
                    break;
                default:
                    return false;
            }
            return true;
        }
        if(isGameover){
            Activity act = (Activity)ctx;
            act.recreate();
            return true;
        }
        return false;
    }

    private void confirmDrop(float X) {
        float gap = (2f*Crad)+padding;
        float ends = 1.5f*padding;
        for(int i=0;i<rows;i++){
            if(X < ends+((i+1)*gap)){
                Log.i(TAG,"detected x = "+i);
                if(play.diskDropX(i)){
                    DropDisc(play.getX(),play.getY());
                    Log.i(TAG,"drop at ("+i+","+play.getY()+")");

                    return;
                }
                else {
                    Toast.makeText(getContext(),"This row is full",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void GameOver(boolean isWin) {
        dropsound.release();
        AlertDialog alertDialog;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);
        if(isSingle)
            alertBuilder.setTitle(isWin?(play.getIsPlayer1()?"YOU    WON":"YOU   LOST"):"GAME  DRAWN");
        else
            alertBuilder.setTitle(isWin?(play.getIsPlayer1()?"PLAYER 1 WON":"PLAYER 2 WON"):"GAME  DRAWN");
        alertBuilder.setPositiveButton("PLAY AGAIN ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = (Activity) ctx;
                activity.recreate();
            }
        });
        alertBuilder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 Activity activity = (Activity) ctx;
                 activity.finish();
            }
        });

        alertDialog = alertBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        isGameover = true;


    }

    private float x=-1;
    private void drawFadedRect(float X) {
        Log.i(TAG,"fadedRectexecuted");
        if(X!=x)
            mBitmap.eraseColor(Color.TRANSPARENT);
        x=X;
        float gap = (2*Crad)+padding;
        float ends = 1.5f*padding;
        for(int i=0;i<rows;i++){
            if(X < ends+((i+1)*gap)){
                mBitmap.eraseColor(Color.TRANSPARENT);
                SubCanvas.drawRoundRect(ends+((i)*gap),Mtop+(0.5f*padding),(ends+((i)*gap))+gap,Mbottom-(0.5f*padding),padding,padding,fadedrectPaint);
                invalidate();
                return;
            }
        }

    }

    float ani_y;
    int x_,y_;
    public void DropDisc (int x, int y){

        x_ = x;
        y_ = y;
        isdropping = true;
        PropertyValuesHolder cent_y = PropertyValuesHolder.ofFloat("Centre_Y",Mtop-(2*padding)-Crad,Cys[x][y]);
        ValueAnimator animator = new ValueAnimator();
        animator.setValues(cent_y);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        Log.i(TAG,"drop disc function at ("+x+","+y+")");
        mBitmap.eraseColor(Color.TRANSPARENT);
        dropsound = MediaPlayer.create(getContext(),R.raw.drop_music);
        dropsound.start();
        //Log.i(TAG, "DropDisc: is player 1 "+is1);


        SubCanvas.save();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBitmap.eraseColor(Color.TRANSPARENT);
                ani_y = (float) animation.getAnimatedValue("Centre_Y");
                if(play.getIsPlayer1()) {
                    mDiskPaint.setColor(player1col);
                    Log.i(TAG, "DropDisc: set color 1");
                }
                else{
                    mDiskPaint.setColor(player2col);
                    Log.i(TAG, "DropDisc: set color 2");
                }
                SubCanvas.drawCircle(Cxs[x_][y_],ani_y,Crad,mDiskPaint);
                if(Cys[x_][y_]==ani_y) {
                    isdropping = false;
                    dropsound.release();
                    play.setstatus();
                    if(play.iswin()){
                        GameOver(true);
                        invalidate();
                        return;
                    }
                    if(play.isdraw()){
                        GameOver(false);
                    }
                    play.TogglePlayer();
                    status = play.getStatus();
                    if(isSingle && !play.getIsPlayer1()) {
                        ai_task = new AI_Task();
                        ai_task.execute();
                    }
                }
                invalidate();
            }
        });
        animator.start();
        Log.i(TAG,"drop disc function draw circle at ("+x+","+y+")");


        SubCanvas.restore();
        invalidate();
    }

    public void undo() {
        if(play.getStatus()[play.getX()][play.getY()]==0) {
            Toast.makeText(getContext(), "You can only undo once", Toast.LENGTH_SHORT).show();
            return;
        }

        play.undo();
        play.TogglePlayer();
        mBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }


    public void giveContext(Context context) {
        ctx = context;
    }

    public class AI_Task extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            AIplaying = true;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            AIplaying = false;
            DropDisc(play.getX(),play.getY());
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            play.AutoPlay();
            return null;
        }
    }
}
