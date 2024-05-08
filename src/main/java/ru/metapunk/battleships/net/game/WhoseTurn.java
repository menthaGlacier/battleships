package ru.metapunk.battleships.net.game;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public enum WhoseTurn implements Serializable {
    NO_ONE,
    PLAYER_ONE,
    PLAYER_TWO;

    public static WhoseTurn getRandomPlayerTurn() {
        final WhoseTurn[] values = WhoseTurn.values();
        return values[ThreadLocalRandom
                .current().nextInt(1, values.length)];
    }
}
