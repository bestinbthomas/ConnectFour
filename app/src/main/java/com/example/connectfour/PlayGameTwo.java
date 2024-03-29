package com.example.connectfour;


import android.util.Log;

public class PlayGameTwo {
    private int columns,rows,y,x;
    public int x1,x2,y1,y2;
    private int[][] status;
    private boolean isplayer1,isSingle;
    private ArtificialIntelligence myAI;


    public PlayGameTwo (int row, int col,boolean isSgle) {
        rows = row;
        columns = col;
        isplayer1 = true;
        isSingle = isSgle;

        status = new int[columns][rows];
        for(int i = 0; i<columns;i++){
            for(int j = 0; j<columns;j++){
                status[i][j] = 0;
            }
        }
        myAI = new ArtificialIntelligence(rows,columns);

    }

    public void TogglePlayer(){
        if(isplayer1)
            isplayer1 =false;
        else
            isplayer1 = true;
        Log.i(ViewBuilder.TAG,"toggled "+isplayer1);

    }

    public boolean diskDropX(int i) {

        for (int j = columns-1; j >=0; j--) {
            if(status[j][i]==0){
                x=j;
                y=i;
                return true;
            }
        }
        return false;
    }


    public void setstatus(){
        if(isplayer1)
            status[x][y]=1;
        else
            status[x][y]=2;
    }
    public int[][] getStatus() {
        return status;
    }

    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }

    public boolean getIsPlayer1() {
        return isplayer1;
    }

    public void undo() {
        status[x][y]=0;
    }

    public boolean iswin() {

        int N = 0;
        int S = 0;
        int E = 0;
        int W = 0;
        int NE = 0;
        int SE = 0;
        int SW = 0;
        int NW = 0;

        int i = 0;
        while(status[x-i][y]==status[x][y]){
            if(i==3) {
                x1 = x-i;
                x2 = x;
                y1 = y;
                y2 = y;
                return true;
            }
            N++;
            i++;
            if(x-i < 0)
                break;

        }

        i = 0;
        while(status[x+i][y]==status[x][y]){
            if(i==3) {
                x1 = x;
                x2 = x+i;
                y1 = y;
                y2 = y;
                return true;
            }
            S++;
            i++;
            if(x+i > columns-1)
                break;

        }

        i = 0;
        while(status[x][y+i]==status[x][y]){
            if(i==3) {
                x1 = x;
                x2 = x;
                y1 = y;
                y2 = y+i;
                return true;
            }
            E++;
            i++;
            if(y+i > rows-1)
                break;

        }

        i = 0;
        while(status[x][y-i]==status[x][y]){
            if(i==3) {
                x1 = x;
                x2 = x;
                y1 = y-i;
                y2 = y;
                return true;
            }
            W++;
            i++;
            if(y-i < 0)
                break;

        }

        i = 0;
        while(status[x-i][y+i]==status[x][y]){
            if(i==3) {
                x1 = x-i;
                x2 = x;
                y1 = y+i;
                y2 = y;
                return true;
            }
            NE++;
            i++;
            if(x-i < 0 || y+i >rows-1)
                break;

        }

        i = 0;
        while(status[x+i][y+i]==status[x][y]){
            if(i==3) {
                x1 = x;
                x2 = x+i;
                y1 = y;
                y2 = y+i;
                return true;
            }
            SE++;
            i++;
            if(x+i > columns-1 || y+i >rows-1)
                break;

        }

        i = 0;
        while(status[x+i][y-i]==status[x][y]){
            if(i==3) {
                x1 = x;
                x2 = x+i;
                y1 = y;
                y2 = y-i;
                return true;
            }
            SW++;
            i++;
            if(x+i > columns-1 || y-i < 0)
                break;

        }

        i = 0;
        while(status[x-i][y-i]==status[x][y]){
            if(i==3) {
                x1 = x-i;
                x2 = x;
                y1 = y-i;
                y2 = y;
                return true;
            }
            NW++;
            i++;
            if(x-i < 0 || y-i < 0)
                break;
        }
        if(((N + S) > 4) || ((E + W) > 4) || ((NE + SW) > 4) || ((NW + SE) > 4)){
            if((N + S) > 4){
                x1 = x-(N-1);
                x2 = x+(S-1);
                y1 = y;
                y2 = y;
                Log.i(ViewBuilder.TAG, "n+s");
            }
            else if((E + W) > 4){
                x1 = x;
                x2 = x;
                y1 = y+(E-1);
                y2 = y-(W-1);
                Log.i(ViewBuilder.TAG, "e+w");
            }
            else if((NE + SW) > 4){
                x1 = x-(NE-1);
                x2 = x+(SW-1);
                y1 = y+(NE-1);
                y2 = y-(SW-1);
                Log.i(ViewBuilder.TAG, "ne+sw");
            }
            else if((NW + SE) > 4){
                x1 = x-(NW-1);
                x2 = x+(SE-1);
                y1 = y-(NW-1);
                y2 = y+(SE-1);
                Log.i(ViewBuilder.TAG, "nw+se");
                Log.i(ViewBuilder.TAG, "isWin: value set by play"+x1+x2+y1+y2);
            }
            Log.d(ViewBuilder.TAG, "iswin() returned: " + true);
            return true;
        }
        else {
            Log.d(ViewBuilder.TAG, "iswin() returned: " + false);
            return false;
        }
    }


    public boolean isdraw() {
        for (int i = 0; i < rows ; i++) {
            if(status[0][i]==0)
                return false;
        }
        return true;
    }

    public void AutoPlay(){
        int foundx ;

        foundx = myAI.whereToDrop(status);
        diskDropX(foundx);
    }
}
