package ru.metapunk.battleships.model.board;

import javafx.scene.layout.GridPane;

public class Board extends GridPane {
    public static final int MAX_ROWS = 10;
    public static final int MAX_COLUMNS = 10;

    public void clear() {
        this.getChildren().clear();
    }
}
