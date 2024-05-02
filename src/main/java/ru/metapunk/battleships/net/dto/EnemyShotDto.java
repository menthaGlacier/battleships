package ru.metapunk.battleships.net.dto;

import java.io.Serializable;

public record EnemyShotDto(int row, int column)
        implements Serializable {
}
