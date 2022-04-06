package com.javaspring.repository;

import com.javaspring.model.Game;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class GameRepository {

    private static Map<String, Game> games;

    private static GameRepository game_repo_instance;
    private GameRepository(){
        games=new HashMap<>() ;
    }
    public static synchronized  GameRepository getInstance(){
        if(game_repo_instance==null) {
            game_repo_instance= new GameRepository();
        }

            return game_repo_instance;
    }
    public  Map<String, Game> getGames(){

        return games;
    }
    public void setGame(Game aGame){
        log.info("Game Id saving :"+aGame.getGameId());
        games.put(aGame.getGameId(),aGame);

    }

}
