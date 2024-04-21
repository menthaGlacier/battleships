package ru.metapunk.battleships.net.dto;

import ru.metapunk.battleships.model.tile.cell.Cell;

import java.io.Serializable;

public class PlayerBoardSetupDto implements Serializable {
    private final String lobbyId;
    private final String playerId;
    private final Cell[][] cells;

    public PlayerBoardSetupDto(String lobbyId, String  playerId, Cell[][] cells) {
        this.lobbyId = lobbyId;
        this.playerId = playerId;
        this.cells = cells;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Cell[][] getCells() {
        return cells;
    }
}
