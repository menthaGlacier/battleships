package ru.metapunk.battleships.view;

import javafx.scene.layout.GridPane;
import ru.metapunk.battleships.view.tiles.Cell;

public class Board {
    public static final int DEFAULT_GRID_SIZE = 10;
    private final GridPane grid;
    private final int rows;
    private final int columns;
    private final boolean isPlayerBoard;

    public Board(boolean isPlayerBoard) {
        this.grid = new GridPane();
        this.rows = DEFAULT_GRID_SIZE;
        this.columns = DEFAULT_GRID_SIZE;
        this.isPlayerBoard = isPlayerBoard;

        for (int row = 0; row < DEFAULT_GRID_SIZE; row++) {
            for (int column = 0; column < DEFAULT_GRID_SIZE; column++) {
                Cell cell = new Cell(isPlayerBoard);
                this.getGrid().add(cell, column, row);
            }
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isPlayerBoard() {
        return isPlayerBoard;
    }
}
