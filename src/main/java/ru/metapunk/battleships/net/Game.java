package ru.metapunk.battleships.net;

import ru.metapunk.battleships.model.ship.ShipsData;
import ru.metapunk.battleships.model.tile.cell.Cell;

public class Game {
    private final String gameId;
    private final Player playerOne;
    private final Player playerTwo;
    private Cell[][] playerOneBoard = null;
    private Cell[][] playerTwoBoard = null;
    private ShipsData playerOneShipsData;
    private ShipsData playerTwoShipsData;
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
        this.playerOneShipsData = new ShipsData(playerOneBoard);
    }

    public Cell[][] getPlayerTwoBoard() {
        return playerTwoBoard;
    }

    public void setPlayerTwoBoard(Cell[][] playerTwoBoard) {
        this.playerTwoBoard = playerTwoBoard;
        this.playerTwoShipsData = new ShipsData(playerTwoBoard);
    }

    public ShipsData getPlayerOneShipsData() {
        return playerOneShipsData;
    }

    public ShipsData getPlayerTwoShipsData() {
        return playerTwoShipsData;
    }

    public WhoseTurn getWhoseTurn() {
        return whoseTurn;
    }

    public void passTurn() {
        if (whoseTurn == WhoseTurn.PLAYER_ONE) {
            whoseTurn = WhoseTurn.PLAYER_TWO;
        } else if (whoseTurn == WhoseTurn.PLAYER_TWO) {
            whoseTurn = WhoseTurn.PLAYER_ONE;
        }
    }
}
