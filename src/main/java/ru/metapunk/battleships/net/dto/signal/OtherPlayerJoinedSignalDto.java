package ru.metapunk.battleships.net.dto.signal;

import java.io.Serializable;

public record OtherPlayerJoinedSignalDto(String gameId)
        implements Serializable {
}
