package xo.demo.Model;
import xo.demo.State.GameState;
import lombok.*;
import java.util.*;

@Setter @Getter
public class Game{
    private String[][] board;
    private String ID;
    private String player1, player2;
    private String winner;
    private String turn;
    private GameState gameState;

    public Game(String player1, String player2){
        this.ID = UUID.randomUUID().toString();
        this.turn = player1;
        this.player1 = player1;
        this.player2 = player2;
        this.board = new String[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                this.board[i][j] = " ";
            }
        }
        gameState = GameState.waitingPlayer;
    }

    public void Move(String player, int move){
        int row = move / 3;
        int col = move / 3;
        if(Objects.equals(board[row][col]," ")){
            board[row][col] = Objects.equals(player, player1) ? "X" : "O";
            turn = player.equals(player1) ? player2 : player1;
            checkWin();
            updateGameState();
        }
    }

    private void checkWin(){
        /**Checking rows
         * Check if all three elements in the current row are equal and not empty (" ")
         * Set the winner based on the equal elements
         **/
        for(int i = 0; i < 3; i++){
           if(Objects.equals(board[i][0],board[i][1]) && Objects.equals(board[i][0], board[i][2])){
               if(!Objects.equals(board[i][0], " ")){
                    setWinner(Objects.equals(board[i][0], player1) ? player1 : player2);
                    return;
               }
           }
        }

        /**Checking columns
         * Check if all three elements in the current columns are equal and not empty (" ")
         * Set the winner based on the equal elements
         */
        for(int i = 0; i < 3; i++){
            if(Objects.equals(board[0][i],board[1][i]) && Objects.equals(board[0][i], board[2][i])){
                if(!Objects.equals(board[0][i], " ")){
                    setWinner(Objects.equals(board[0][i], player1) ? player1 : player2);
                    return;
                }
            }
        }

        /**Checking diagonals
         * Checking from top-left corner to bottom-right corner
         * Set the winner based on the equal elements
         */
        for(int i = 0; i < 3; i++){
            if(Objects.equals(board[0][0],board[1][1]) && Objects.equals(board[0][0], board[2][2])){
                if(!Objects.equals(board[0][0], " ")){
                    setWinner(Objects.equals(board[0][0], player1) ? player1 : player2);
                    return;
                }
            }
        }

        /**Checking diagonals
         * Checking from top-right corner to bottom-left corner
         * Set the winner based on the equal elements
         */
        for(int i = 0; i < 3; i++){
            if(Objects.equals(board[0][2],board[1][1]) && Objects.equals(board[0][2], board[2][0])){
                if(!Objects.equals(board[0][2], " ")){
                    setWinner(Objects.equals(board[0][2], player1) ? player1 : player2);
                    return;
                }
            }
        }
    }

    private void updateGameState(){
        if(winner != null){
            gameState = winner.equals(player1) ? GameState.player1Win : GameState.player2Win;
        }else if(isFull()){
            gameState = GameState.tie;
        }else{
            gameState = turn.equals(player1) ? GameState.player1Turn : GameState.player2Turn;
        }
    }

    private boolean isFull(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(Objects.equals(board[i][j], " ")){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameOver(){
        return winner != null || isFull();
    }
}