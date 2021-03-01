
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queens;

import java.util.*;

/**
 *
 * @author diegorodriguez
 */
public class Board {

    final private int[][] board;
    private int heu;
    final private int fState[][];
    private int restarts;
    private int moves;
    private int lStates;

    public Board() {
        board = new int[8][8];
        heu = 0;
        fState = new int[8][8];
        restarts = 0;
        moves = 0;
        lStates = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[x][y] = 0;
            }
        }

    }

    public void setUp() {
        for(int row = 0; row < 8; row++){
                    for(int col = 0; col < 8; col++){
                        board[row][col] = 0;
                    }
                }
        Random rand = new Random();
        for (int x = 0; x < 8; x++) {
            board[rand.nextInt(8)][x] = 1;
        }

    }
    
    public int getHeu(int state[][]){
        int count = 0;
        boolean rowCon;
        boolean diaCon;
        for(int row = 0 ; row < 8 ; row++){
            for(int col = 0 ; col < 8 ; col++){
                if(state[row][col]==1){
                    rowCon = checkRow(state,col);
                    diaCon = checkDia(state,row,col);
                    if( rowCon ||diaCon ){
                        count++;
                    }
                }
            }
        }
        return count;
    }
     public boolean dRestart(int state[][]){
        int lowerNeigh = 0;
        int currLow = heu;
        for(int row = 0 ; row < 8 ; row++){
            for( int col =0; col < 8 ; col++){
                if(state[row][col]<currLow){
                    currLow = state[row][col];
                    lowerNeigh++;
                }
            }
        }
        //checks wether there is any possible moves with a heuristic lower than current
        return lowerNeigh==0;
    }
    //Only checking row because queens shouldn't interfere with each others columns
    public boolean checkRow(int state[][], int col){
        int count =0;
        boolean found = false;
        
        for(int row = 0; row < 8 ; row++){
            if(state[row][col]==1){
                count++;
            }
        }
        if(count>1){
            found = true;
        }
        return found;
    }
    public boolean checkDia(int state[][], int row, int col){
        boolean found = false;
        //Checking if there is any conflicting diagonal queens
        //if there is any, then you break and return true else false
        for(int x = 1 ; x < 8 ; x++){
            if(found){
                return found;
            }
            
            if(row + x <8 && col +x<8){
                if(state[row + x][col + x] ==1){
                    found = true;
                }
            }
            
            if(row + x <7 && col -x >=0){
                if(state[row + x][col - x] ==1){
                    found = true;
                }
            }
            
            if(row - x >= 0 && col +x<8){
                if(state[row - x][col + x] ==1){
                    found = true;
                }
            }
            
            if(row - x >= 0 && col -x >= 0){
                if(state[row - x][col - x] ==1){
                    found = true;
                }
            }
    }
        return found;
    }
    public int fMinRow(int state[][]){
        int minVal = heu;
        int minRow =0;
        int count =0;
        
        for(int row = 0 ; row < 8 ; row++){
            for( int col =0; col < 8 ; col++){
                if(state[row][col]<minVal){
                    minVal = state[row][col];
                    minRow =row;
                    count++;
                }
            }
        }
        lStates = count;
        return minRow;
    }
    
    public int fMinCol(int state[][]){
        int minVal = heu;
        int minCol =0;
        int count =0;
        
        for(int row = 0 ; row < 8 ; row++){
            for( int col =0; col < 8 ; col++){
                if(state[row][col]<minVal){
                    minVal = state[row][col];
                    minCol =col;
                    count++;
                }
            }
        }
        lStates = count;
        return minCol;
    }
    public void update(){
        System.out.println("\n");
            System.out.println("Current Heuristic: " + heu);
            System.out.println("Current State");
            for(int row = 0; row < 8; row++){
                for(int col = 0; col < 8; col++){
                    System.out.print(board[row][col] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("Neighbor States found with lower heuritic: " + lStates);
            System.out.println("Setting new current State");
    }
    public void gameOver(){
         System.out.println("\nCurrent State");
                for(int row = 0; row < 8; row++){
                    for(int col = 0; col < 8; col++){
                        System.out.print(board[row][col] + " ");
                    }
                    System.out.print("\n");
                }
            System.out.println("Game over, solution found");
            System.out.println("Queen Moved Around  " + moves+" Times");
            System.out.println("Amount of Restarts: " + restarts);
    }
    public void move(){
        int colT;
        int oStateQ = 0;
        int [] [] hBoard = new int [8][8];
        int minRow;
        int minCol;
        while(true){
        colT=0;
        while(colT<8){
            
            for(int row = 0 ; row < 8; row++){
                System.arraycopy(board[row] ,0,fState[row],0,8);
            }
            for(int row = 0; row < 8 ; row++){
                fState[row][colT] = 0;
                if(board[row][colT]==1){
                    oStateQ = row;
                    fState[row][colT]=0;
                }
                fState[row][colT] = 1;
                hBoard[row][colT] = getHeu(fState);
                fState[row][colT] = 0;
                
            }
            fState[oStateQ][colT]=1;
                    colT++;
                
        }
        if(dRestart(hBoard)){
                setUp( );
                System.out.println("Restarting");
                restarts++;
            }
      
            minCol = fMinCol(hBoard);
            minRow = fMinRow(hBoard);
      
            for(int i = 0; i < 8; i++){
                board[i][minCol] = 0;
            }
      
            board[minRow][minCol] = 1;
            moves++;
            heu= getHeu(board);
          
            if(getHeu(board) == 0){
               gameOver();
            break;
            }
            update();
            
    }
        }
        
    }
    

