package ru.metapunk.battleships.net.dto.response;

import java.io.Serializable;

public record WhoseTurnResponseDto(boolean isPlayerTurn)
        implements Serializable {
}
