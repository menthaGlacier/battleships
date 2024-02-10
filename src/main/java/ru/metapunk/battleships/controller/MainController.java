package ru.metapunk.battleships.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.metapunk.battleships.model.board.Board;
import ru.metapunk.battleships.model.tile.Tile;
import ru.metapunk.battleships.model.tile.cell.Cell;

import java.io.IOException;

public class MainController {
    @FXML
    private AnchorPane root;
    @FXML
    private Board board;

    private Tile[][] tiles;

    public MainController() {
        this.tiles = new Tile[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];
    }

    private void initializeBoard(Cell[][] cells) {
        for (int row = 0; row < Board.DEFAULT_ROWS; row++) {
            for (int column = 0; column < Board.DEFAULT_COLUMNS; column++) {
                Tile tile = new Tile(cells[row][column]);

                tiles[row][column] = tile;
                board.add(tile, column, row);
            }
        }
    }

    private void callPlacementDialog() {
        final Stage dialog = new Stage();
        Cell[][] cells = new Cell[Board.DEFAULT_ROWS][Board.DEFAULT_COLUMNS];
        FXMLLoader loader = new FXMLLoader((getClass()
                .getResource("/ru/metapunk/battleships/fxml/placement-view.fxml")));
        loader.setControllerFactory(controllerClass->
                new PlacementController(dialog, cells)
        );

        try {
            dialog.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException();
        }

        dialog.setTitle("Place ships");
        dialog.setResizable(false);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(root.getScene().getWindow());
        dialog.showAndWait();

        initializeBoard(cells);
    }

    @FXML
    private void test() {
        callPlacementDialog();
    }
}
