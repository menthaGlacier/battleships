package ru.metapunk.battleships.net;

import ru.metapunk.battleships.model.tile.cell.Cell;

public class Game {
    private final String gameId;
    private final String playerOneId;
    private final String playerTwoId;
    private Cell[][] playerOneBoard;
    private Cell[][] playerTwoBoard;

    public Game(String gameId, String playerOneId, String playerTwoId) {
        this.gameId = gameId;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerOneId() {
        return playerOneId;
    }

    public String getPlayerTwoId() {
        return playerTwoId;
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
}
