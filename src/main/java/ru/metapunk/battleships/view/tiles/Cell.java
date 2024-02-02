package ru.metapunk.battleships.view.tiles;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.text.NumberFormat;
import java.util.Locale;

public class Cell extends StackPane {
    public static final Color EMPTY_TILE_FILL_COLOR = Color.rgb(255, 255, 255);
    public static final Color EMPTY_TILE_BORDER_COLOR = Color.rgb(100, 100, 100);
    public static final Color PLAYER_SHIP_TILE_FILL_COLOR = Color.rgb(60, 60, 255, 0.2);
    public static final Color PLAYER_SHIP_TILE_BORDER_COLOR = Color.rgb(45, 45, 220);
    public static final Color ENEMY_SHIP_TILE_FILL_COLOR = Color.rgb(255, 40, 40, 0.2);
    public static final Color ENEMY_SHIP_TILE_BORDER_COLOR = Color.rgb(220, 14, 14);
    public static final int TILE_SIZE = 50;

    private final Pane tile;
    private TileType tileType;
    private boolean isPlayerBoardTile;
    private boolean hasShip;
    private boolean bombarded;

    public Cell() {
        this(TileType.SINGULAR, false, false);
    }

    public Cell(TileType tileType, boolean isPlayerBoardTile, boolean hasShip) {
        this.tile = new Pane();
        this.tileType = tileType;
        this.isPlayerBoardTile = isPlayerBoardTile;
        this.hasShip = hasShip;
        this.bombarded = false;

        this.getTile().setPrefSize(TILE_SIZE, TILE_SIZE);
        this.applyTileStyle();
        this.getChildren().add(this.getTile());

        if (!this.getIsPlayerBoardTile()) {
            this.handleMouseEvents();
        }
    }

    public Cell(boolean isPlayerBoardTile) {
        this(TileType.SINGULAR, isPlayerBoardTile, false);
    }

    private void handleMouseEvents() {
        this.setOnMouseClicked(e -> {
            if (!this.getBombarded()) {
                this.setBombarded(true);
                this.putDotMark();
            }
        });

        this.setOnMouseEntered(e -> {
            this.getTile().setStyle("-fx-border-color: wheat; " +
                    "-fx-border-width: 2; " + this.getBackgroundColorStyle());
        });

        this.setOnMouseExited(e -> {
            this.applyTileStyle();
        });
    }

    public Pane getTile() {
        return this.tile;
    }

    public TileType getTileType() {
        return this.tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public boolean getIsPlayerBoardTile() {
        return this.isPlayerBoardTile;
    }

    public void setIsPlayerBoardTile(boolean isPlayerBoardTile) {
        this.isPlayerBoardTile = isPlayerBoardTile;
    }

    public boolean getHasShip() {
        return this.hasShip;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public boolean getBombarded() {
        return this.bombarded;
    }

    public void setBombarded(boolean bombarded) {
        this.bombarded = bombarded;
    }

    private String colorToRgbaString(Color color) {
        // Весь этот цирк существует только из-за того что Java конвертит
        // числа с плавающей точкой в локаль РФ, где разделитель - запятая лол
        String opacity = NumberFormat.getNumberInstance(Locale.US)
                .format(color.getOpacity());
        return String.format("rgba(%d, %d, %d, ",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        ) + opacity + ")";
    }

    private String getBackgroundColorStyle() {
        String color = "-fx-background-color: ";

        if (!this.getHasShip()) {
            color += (colorToRgbaString(EMPTY_TILE_FILL_COLOR) + ";");
        } else {
            if (this.getIsPlayerBoardTile()) {
                color += colorToRgbaString(PLAYER_SHIP_TILE_FILL_COLOR) + ";";
            } else {
                color += colorToRgbaString(ENEMY_SHIP_TILE_FILL_COLOR) + ";";
            }
        }

        return color;
    }

    private String getBorderColorStyle() {
        String color = "-fx-border-color: ";

        if (!this.getHasShip()) {
            color += (colorToRgbaString(EMPTY_TILE_BORDER_COLOR) + ";");
        } else {
            switch (this.getTileType()) {
                case SINGULAR ->
                        color += "holder holder holder holder;";
                case UPMOST_VERTICAL ->
                        color += "holder holder transparent holder;";
                case BOTTOMMOST_VERTICAL ->
                        color += "transparent holder holder holder;";
                case BRIDGE_VERTICAL ->
                        color += "transparent holder transparent holder;";
                case LEFTMOST_HORIZONTAL ->
                        color += "holder transparent holder holder;";
                case RIGHTMOST_HORIZONTAL ->
                        color += "holder holder holder transparent;";
                case BRIDGE_HORIZONTAL ->
                        color += "holder transparent holder transparent;";
            }

            if (this.getIsPlayerBoardTile()) {
                color = color.replaceAll("holder",
                        colorToRgbaString(PLAYER_SHIP_TILE_BORDER_COLOR));
            } else {
                color = color.replaceAll("holder",
                        colorToRgbaString(ENEMY_SHIP_TILE_BORDER_COLOR));
            }
        }

        return color;
    }

    public void applyTileStyle() {
        String fillColorStyle = this.getBackgroundColorStyle();
        String borderColorStyle = this.getBorderColorStyle();

        this.getTile().setStyle(fillColorStyle + " " + borderColorStyle);
    }

    public void putDotMark() {
        Circle circle;

        if (isPlayerBoardTile) {
            circle = new Circle(2, EMPTY_TILE_BORDER_COLOR);
        } else {
            circle = new Circle(2, ENEMY_SHIP_TILE_BORDER_COLOR);
        }

        this.getChildren().add(circle);
    }

    public void putXMark() {
        Line first = new Line(0, 0, TILE_SIZE * 0.7, TILE_SIZE * 0.7);
        Line second = new Line(0, TILE_SIZE * 0.7, TILE_SIZE * 0.7, 0);

        first.setStrokeWidth(2);
        second.setStrokeWidth(2);

        if (isPlayerBoardTile) {
            first.setStroke(EMPTY_TILE_BORDER_COLOR);
            second.setStroke(EMPTY_TILE_BORDER_COLOR);
        } else {
            first.setStroke(ENEMY_SHIP_TILE_BORDER_COLOR);
            second.setStroke(ENEMY_SHIP_TILE_BORDER_COLOR);
        }

        this.getChildren().add(first);
        this.getChildren().add(second);
    }
}
