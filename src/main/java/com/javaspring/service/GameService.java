package com.javaspring.service;

import com.javaspring.exception.InvalidGameEntryException;
import com.javaspring.exception.InvalidGameIdException;
import com.javaspring.exception.NoGameException;
import com.javaspring.model.*;
import com.javaspring.repository.GameRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static com.javaspring.model.Status.NEW;
import static com.javaspring.model.Status.IN_PROGRESS;
import static com.javaspring.model.Status.FINISHED;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class GameService {

    public Game  createGame(Player player1){ // one player creates game
        Game game=new Game();
        game.setGameBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player1);
        game.setGameStatus(NEW);
        GameRepository.getInstance().setGame(game);// to keep track of games in repository/map
        return game;

    }

    public Game connectGame(Player player2,String gameId) throws InvalidGameIdException,InvalidGameEntryException{// second player connects the existing game(with game id)
        log.info("Get map size from service: ",GameRepository.getInstance().getGames().size() );
        if(!GameRepository.getInstance().getGames().containsKey(gameId)){
            throw new InvalidGameIdException("Not a valid Game Id");
        }
        Game game=GameRepository.getInstance().getGames().get(gameId);
        if(game.getPlayer2()!=null)
            throw new InvalidGameEntryException("Two Players are already playing the game");
        game.setPlayer2(player2);
        game.setGameStatus(IN_PROGRESS);
        GameRepository.getInstance().setGame(game);
        return game; // returning game to player2 to which he connected
    }
    public Game connectRandomGame(Player player2) throws NoGameException{
        Game availableGame=GameRepository.getInstance().getGames().values().stream()
                .filter(game->game.getGameStatus().equals(NEW))
                .findFirst().orElseThrow(()-> new NoGameException("Game Not Found"));
        availableGame.setPlayer2(player2);
        availableGame.setGameStatus(IN_PROGRESS);
        GameRepository.getInstance().setGame(availableGame);
        return availableGame;

    }
    public Game gamePlay(GamePlay gamePlay) throws NoGameException,InvalidGameEntryException{
        if(!GameRepository.getInstance().getGames().containsKey(gamePlay.getGameId()))
            throw new NoGameException("Game Not Found") ;
            Game game=GameRepository.getInstance().getGames().get(gamePlay.getGameId());
            if(game.getGameStatus().equals(FINISHED))
                throw new InvalidGameEntryException("Game already finished");
            int[][] board=game.getGameBoard();
            board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()]=gamePlay.getType().getValue();
            // we check after every game play
           boolean WinnerX= checkWinner(board, TicTacToe.X);// player1 move
            boolean WinnerO=checkWinner(board, TicTacToe.O);//player2 move
        if(WinnerX) {
            game.setWinner(TicTacToe.X);
            game.setGameStatus(FINISHED);
        }
        else if(WinnerO) {
            game.setWinner(TicTacToe.O);
            game.setGameStatus(FINISHED);
        }
            GameRepository.getInstance().setGame(game);
            return game;
    }

    private Boolean checkWinner(int[][] board, TicTacToe ticTac) {
        int[] boardArray = new int[9];
        int counterIndex=0;
        for(int i=0;i<board.length;i++)// row iteration
            for(int j=0;j<board[i].length;j++) { // column iteration
                boardArray[counterIndex]=board[i][j];
                counterIndex++;
            }
        // to check winning combinations
        int[][] winCombinations={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for(int i=0;i<winCombinations.length;i++){
            int counter=0;
            for(int j=0;j<winCombinations[i].length;j++) {
                if (boardArray[winCombinations[i][j]] == ticTac.getValue()) {
                    counter++;
                    if (counter == 3)
                        return true;
                }
            }


        }
        return false;

    }

}
