package com.javaspring.controller;

import com.javaspring.dto.ConnectRequest;
import com.javaspring.exception.InvalidGameEntryException;
import com.javaspring.exception.InvalidGameIdException;
import com.javaspring.model.Game;
import com.javaspring.model.GamePlay;
import com.javaspring.model.Player;
import com.javaspring.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@ApiOperation(value="/game", tags="Game Controller")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @ApiOperation(value="Start a Game" , response= ResponseEntity.class)
    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player){
        log.info("start game request { } ",player);
        return ResponseEntity.ok(gameService.createGame(player));
    }
    @ApiOperation(value="Connect a game with GameId" , response= ResponseEntity.class)
    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest connectRequest) throws InvalidGameIdException, InvalidGameEntryException {
        log.info("connect game request { } ",connectRequest);
        return ResponseEntity.ok(gameService.connectGame(connectRequest.getPlayer(),connectRequest.getGameId()));
    }
   @ApiOperation(value="Connect a random Game" , response= ResponseEntity.class)
    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws InvalidGameIdException, InvalidGameEntryException {
        log.info("connect random game request { } ",player);
        return ResponseEntity.ok(gameService.connectRandomGame(player));
    }
    @ApiOperation(value="Game Play" , response= ResponseEntity.class)
    @PostMapping("/gamePlay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay gamePlayRequest){
        log.info("game play request { } ",gamePlayRequest);
        Game game=gameService.gamePlay(gamePlayRequest);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/"+game.getGameId(),game);
        return ResponseEntity.ok(game);
    }
}
