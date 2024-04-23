package ru.metapunk.battleships.net;

import ru.metapunk.battleships.model.tile.cell.Cell;

public class Game {
    private final String gameId;
    private final Player playerOne;
    private final Player playerTwo;
    private Cell[][] playerOneBoard = null;
    private Cell[][] playerTwoBoard = null;
    private WhoseTurn whoseTurn;

    public Game(String gameId, Player playerOne, Player playerTwo) {
        this.gameId = gameId;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.whoseTurn = WhoseTurn.getRandomPlayerTurn();
    }

    public String getGameId() {
        return gameId;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Cell[][] getPlayerOneBoard() {
        return playerOneBoard;
    }

    public void setPlayerOneBoard(Cell[][] playerOneBoard) {
        this.playerOneBoard = playerOneBoard;
    }

    public Cell[][] getPlayerTwoBoard() {
        return playerTwoBoard;
    }

    public void setPlayerTwoBoard(Cell[][] playerTwoBoard) {
        this.playerTwoBoard = playerTwoBoard;
    }

    public WhoseTurn getWhoseTurn() {
        return whoseTurn;
    }
}
