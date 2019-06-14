package com.example.connectfour;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ArtificialIntelligence {
    int rows,columns;
    final int AIREP = 2,HUMANREP=1,EMPTYREP = 0;

    ArtificialIntelligence(int row, int col){
        rows = row;
        columns = col;

    }
    private int[][] copyBoard(int[][] brd){
        int[][] board = new int[columns][rows];
        for (int i = 0; i <columns ; i++) {
            board[i] = brd[i].clone();
        }
        return board;
    }

    public int whereToDrop(int[][] brd){
        //copy the original array

        int[][] board = copyBoard(brd);
        Result main;

        main = minimax(board,5,Integer.MIN_VALUE,Integer.MAX_VALUE,true);

        return main.Row;
    }

    private List<Integer> getAvailRows(int[][] board){
        List<Integer> availRows =new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            if(board[0][i]==0){
                availRows.add(i);
            }
        }
        return availRows;
    }
    private boolean isWin(int[][] brd,int rep){

        //check horizontal
        int[] pack = new int[4];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows-3; j++) {
                if(brd[i][j]==0)continue;
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[i][j+k];
                }
                if(countElement(pack,rep)==4)return true;
            }

        }
        //check vertical
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns-3; j++) {
                if(brd[j][i]==0)continue;
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[j+k][i];
                }
                if(countElement(pack,rep)==4)return true;
            }

        }
        //check minor diagonal
        for (int i = 0; i < columns-3; i++) {
            for (int j = 0; j < rows-3; j++) {
                if(brd[i][j]==0)continue;
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[i+k][j+k];
                }
                if(countElement(pack,rep)==4)return true;
            }

        }
        //check major diagonal
        for (int i = 3; i < columns; i++) {
            for (int j = 0; j < rows-3; j++) {
                if(brd[i][j]==0)continue;
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[i-k][j+k];
                }
                if(countElement(pack,rep)==4)return true;
            }

        }
        return false;
    }
    private boolean isGameOver(int[][] brd){
        if(getAvailRows(brd).isEmpty())
            return true;
        if(isWin(brd,AIREP))
            return true;
        if(isWin(brd,HUMANREP))
            return true;
        return false;

    }

    private int countElement(int[] arr,int elem){
        int count = 0;
        for (int i:arr) {
            if (i==elem)
                count++;
        }
        return count;
    }

    private int evaluatePack(int[] pack){
        if(countElement(pack,HUMANREP)==4)
            return -1000000;
        else if(countElement(pack,AIREP)==4)
            return 1000000000;
        else if(countElement(pack,HUMANREP)==3 && countElement(pack,EMPTYREP)==1)
            return -4;
        else if(countElement(pack,AIREP)==3 && countElement(pack,EMPTYREP)==1)
            return 8;

        else if(countElement(pack,AIREP)==2 && countElement(pack,EMPTYREP)==2)
            return 2;
        else
            return 0;
    }

    private int evaluateBoard(int[][] brd){
        int score = 0;

        //scoring center Row
        for (int i = 0; i < columns; i++) {
            if(brd[i][rows/2]==AIREP)
                score+=3;
            else if(brd[i][rows/2]==HUMANREP){
                score-=2;
            }
        }
        //check horizontal
        int[] pack = new int[4];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows-3; j++) {
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[i][j+k];
                }
                score += evaluatePack(pack);
            }

        }
        //check vertical
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns-3; j++) {
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[j+k][i];
                }
                score += evaluatePack(pack);
            }

        }
        //check minor diagonal
        for (int i = 0; i < columns-3; i++) {
            for (int j = 0; j < rows-3; j++) {
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[i+k][j+k];
                }
                score += evaluatePack(pack);
            }

        }
        //check major diagonal
        for (int i = 3; i < columns; i++) {
            for (int j = 0; j < rows-3; j++) {
                for (int k = 0; k < 4; k++) {
                    pack[k] = brd[i-k][j+k];
                }
                score += evaluatePack(pack);
            }

        }
        return score;
    }

    private void dropAt(int[][]board, int row,int rep) {

        for (int j = columns-1; j >=0; j--) {
            if(board[j][row]==0){
                board[j][row]=rep;
                return;
            }
        }
    }

    private Result minimax(int[][] board, int depth, int alpha, int beta, boolean isAi) {
        if(depth == 0 || isGameOver(board)) {
            if(isWin(board,AIREP))
                return new Result(Integer.MAX_VALUE);
            else if(isWin(board,HUMANREP))
                return new Result(Integer.MIN_VALUE);
            else if(getAvailRows(board).isEmpty())
                return new Result(0);
            else if(depth == 0)
                return new Result(evaluateBoard(board));
        }
        if(isAi){
            int value = Integer.MIN_VALUE;
            int Row = 0;
            List<Integer> availbleRows = getAvailRows(board);
            for (int row:availbleRows) {
                int[][] newBoard = copyBoard(board);
                dropAt(newBoard,row,AIREP);
                Result res = minimax(newBoard,depth-1,alpha,beta,false);
                if(res.score> value) {
                    value = res.score;
                    Row = row;
                }
                alpha = Math.max(alpha,value);
                if(alpha>=beta)
                    break;
            }
            return new Result(Row,value);
        }
        else {
            int value = Integer.MAX_VALUE;
            int Row = 0;
            List<Integer> availbleRows = getAvailRows(board);
            for (int row:availbleRows) {
                int[][] newBoard = copyBoard(board);
                dropAt(newBoard,row,HUMANREP);
                Result res = minimax(newBoard,depth-1,alpha,beta,true);
                if(res.score < value) {
                    value = res.score;
                    Row = row;
                }
                beta = Math.min(beta,value);
                if(alpha>=beta)
                    break;
            }
            return new Result(Row,value);
        }

    }


}
