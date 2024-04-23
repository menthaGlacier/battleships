package ru.metapunk.battleships.net;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public enum WhoseTurn implements Serializable {
    NO_ONE,
    PLAYER_ONE,
    PLAYER_TWO;

    public static WhoseTurn getRandomPlayerTurn() {
        WhoseTurn[] values = WhoseTurn.values();
        return values[ThreadLocalRandom.current().nextInt(1, values.length)];
    }
}
