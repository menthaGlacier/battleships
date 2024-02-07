package ru.metapunk.battleships.model.board;

import javafx.scene.layout.GridPane;
import ru.metapunk.battleships.model.tile.Cell;

public class Board extends GridPane {
    public static final int DEFAULT_ROWS = 10;
    public static final int DEFAULT_COLUMNS = 10;

    public void clear() {
        this.getChildren().clear();
    }
}
