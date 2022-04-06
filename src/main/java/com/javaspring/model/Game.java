package com.javaspring.model;

import lombok.Data;

@Data
public class Game {
    private String gameId;
    private Player player1;
    private Player player2;
    private Status gameStatus;
    private int[][] gameBoard;
    TicTacToe winner;

}
