package xo.demo.Manager;
import xo.demo.Model.Game;
import xo.demo.State.GameState;
import java.util.*;
import java.util.concurrent.*;

/**
 * Manager class for the XO games.
 * Handles adding and removing players from games, and storing and retrieving the current games.
 */
public class GameManager{
    private final Map<String, Game> games;
    protected final Map<String, String> waitingPlayers;

   public GameManager(){
       // Map of active Tic-Tac-Toe games, with the game ID as the key
       games = new ConcurrentHashMap<>();
       // Map of players waiting to join a Tic-Tac-Toe game, with the player's name as the key
       waitingPlayers = new ConcurrentHashMap<>();
   }

    /**
     * Attempts to add a player to an existing XO game, or creates a new game if no open games are available.
     * @param player the name of the player
     * @return the XO game the player was added to
     */
   public synchronized Game joinGame(String player){
       if(games.values().stream().anyMatch(game -> game.getPlayer1().equals(player)
               || (game.getPlayer2() != null)
               && game.getPlayer2().equals(player))){
           return games.values().stream().filter(game -> game.getPlayer1().equals(player)
               || game.getPlayer2().equals(player)).findFirst().orElse(null);
       }
       for(Game game : games.values()){
           if(game.getPlayer1() != null && game.getPlayer2() == null){
               game.setPlayer2(player);
               game.setGameState(GameState.player1Turn);
               return game;
           }
       }
       Game game = new Game(player, null);
       games.put(game.getID(), game);
       waitingPlayers.put(player, game.getID());
       return game;
   }

    /**
     * Removes a player from XO game. If the player was the only player in the game,
     * the game is removed.
     * @param player the name of the player
     */
    public synchronized Game leaveGame(String player){
       String gameID = getGameByPlayer(player) != null ? getGameByPlayer(player).getID() : null;
       if(gameID != null){
           waitingPlayers.remove(player);
           Game game = games.get(gameID);
           if(player.equals(game.getPlayer1())){
               if(game.getPlayer2() != null){
                    game.setPlayer1(game.getPlayer2());
                    game.setPlayer2(null);
                    game.setGameState(GameState.waitingPlayer);
                    game.setBoard(new String[3][3]);
                    waitingPlayers.put(game.getPlayer1(), game.getID());
               }else{
                   games.remove(gameID);
                   return null;
               }
           }else if(player.equals(game.getPlayer2())){
               game.setPlayer2(null);
               game.setGameState(GameState.waitingPlayer);
               game.setBoard(new String[3][3]);
               waitingPlayers.put(game.getPlayer1(), game.getID());
           }
           return game;
       }
       return null;
    }

    /**
     * Returns the XO game with the given game ID.
     * @param gameID the ID of the game
     * @return the XO game with the given game ID, or null if no such game exists
     */
    public Game getGame(String gameID){
        return games.get(gameID);
    }

    /**
     * Returns the XO game the given player is in.
     * @param player the name of the player
     * @return the XO game the given player is in, or null if the player is not in a game
     */
    public Game getGameByPlayer(String player){
        return games.values().stream().filter(game -> game.getPlayer1().equals(player)
                || (game.getPlayer2() != null && game.getPlayer2().equals(player))).findFirst().orElse(null);
    }

    /**
     * Removes the XO game with the given game ID.
     * @param gameID the ID of the game to remove
     */
    public void removeGame(String gameID){
        games.remove(gameID);
    }
}