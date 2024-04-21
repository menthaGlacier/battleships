package ru.metapunk.battleships.net.dto;

import ru.metapunk.battleships.model.tile.cell.Cell;

import java.io.Serializable;

public record PlayerBoardSetupDto(String lobbyId, String playerId, Cell[][] cells)
        implements Serializable {
}
