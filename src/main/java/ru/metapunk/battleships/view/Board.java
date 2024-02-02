package ru.metapunk.battleships.view;

import javafx.scene.layout.GridPane;

public class Board {
    public static final int GRID_SIZE = 10;
    private final GridPane grid;

    public Board(boolean isPlayerBoard) {
        this.grid = new GridPane();
    }
}
