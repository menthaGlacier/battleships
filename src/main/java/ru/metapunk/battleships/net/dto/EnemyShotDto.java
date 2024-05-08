package ru.metapunk.battleships.net.dto;

import ru.metapunk.battleships.model.ship.Ship;

import java.io.Serializable;

public record EnemyShotDto(int row, int column, Ship destroyedShip)
        implements Serializable {
}
