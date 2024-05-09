package ru.metapunk.battleships.model.tile;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.model.tile.cell.CellShipPresence;
import ru.metapunk.battleships.model.tile.cell.CellType;
import ru.metapunk.battleships.model.tile.cell.CellWarSide;

import java.text.NumberFormat;
import java.util.Locale;

public class Tile extends StackPane {
    public static final Color NEUTRAL_TILE_FILL_COLOR = Color.rgb(255, 255, 255);
    public static final Color NEUTRAL_TILE_BORDER_COLOR = Color.rgb(100, 100, 100);
    public static final Color PLAYER_SHIP_TILE_FILL_COLOR = Color.rgb(60, 60, 255, 0.2);
    public static final Color PLAYER_SHIP_TILE_BORDER_COLOR = Color.rgb(45, 45, 220);
    public static final Color ENEMY_SHIP_TILE_FILL_COLOR = Color.rgb(255, 40, 40, 0.2);
    public static final Color ENEMY_SHIP_TILE_BORDER_COLOR = Color.rgb(220, 14, 14);
    public static final int TILE_SIZE = 50;

    private final Pane tilePane;
    private final Cell cell;

    public Tile() {
        this(new Cell());
    }

    public Tile(Cell cell) {
        this.tilePane = new Pane();
        this.cell = cell;

        this.tilePane.setPrefSize(TILE_SIZE, TILE_SIZE);
        this.applyTileStyle();
        this.getChildren().add(tilePane);
    }

    public void clear() {
        cell.setType(CellType.SINGULAR);
        cell.setWarSide(CellWarSide.NEUTRAL);
        cell.setShipPresence(CellShipPresence.ABSENT);
        getChildren().removeIf(node -> node != tilePane);
        applyTileStyle();
    }

    public Cell getCell() {
        return cell;
    }

    private void putDotMark() {
        final Circle circle = new Circle(4);

        if (cell.getWarSide() == CellWarSide.ENEMY) {
            circle.setFill(ENEMY_SHIP_TILE_BORDER_COLOR);
        } else {
            circle.setFill(NEUTRAL_TILE_BORDER_COLOR);
        }

        getChildren().add(circle);
    }

    private void removeDotMark() {
        getChildren().removeIf(node -> node instanceof Circle);
    }

    private void putXMark() {
        final Line first = new Line(0, 0, TILE_SIZE * 0.7, TILE_SIZE * 0.7);
        final Line second = new Line(0, TILE_SIZE * 0.7, TILE_SIZE * 0.7, 0);

        first.setStrokeWidth(2);
        second.setStrokeWidth(2);

        if (cell.getWarSide() == CellWarSide.ENEMY) {
            first.setStroke(ENEMY_SHIP_TILE_BORDER_COLOR);
            second.setStroke(ENEMY_SHIP_TILE_BORDER_COLOR);
        } else {
            first.setStroke(NEUTRAL_TILE_BORDER_COLOR);
            second.setStroke(NEUTRAL_TILE_BORDER_COLOR);
        }

        getChildren().add(first);
        getChildren().add(second);
    }

    private void removeXMark() {
        getChildren().removeIf(node -> node instanceof Line);
    }

    public void setMark(MarkType markType) {
        if (markType == MarkType.DOT) {
            removeXMark();
            putDotMark();
        } else if (markType == MarkType.X) {
            removeDotMark();
            putXMark();
        }
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

        switch (cell.getWarSide()) {
            case PLAYER -> color += colorToRgbaString(PLAYER_SHIP_TILE_FILL_COLOR) + ";";
            case ENEMY -> color += colorToRgbaString(ENEMY_SHIP_TILE_FILL_COLOR) + ";";
            default -> color += (colorToRgbaString(NEUTRAL_TILE_FILL_COLOR) + ";");
        }

        return color;
    }

    private String getBorderColorStyle() {
        String color = "-fx-border-color: ";

        if (cell.getShipPresence() == CellShipPresence.PRESENT) {
            switch (cell.getType()) {
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

            switch (cell.getWarSide()) {
                case PLAYER -> color = color.replaceAll("holder",
                        colorToRgbaString(PLAYER_SHIP_TILE_BORDER_COLOR));
                case ENEMY -> color = color.replaceAll("holder",
                        colorToRgbaString(ENEMY_SHIP_TILE_BORDER_COLOR));
            }
        } else {
            color += (colorToRgbaString(NEUTRAL_TILE_BORDER_COLOR) + ";");
        }

        return color;
    }

    public void applyTileStyle() {
        String fillColorStyle = getBackgroundColorStyle();
        String borderColorStyle = getBorderColorStyle();

        tilePane.setStyle(fillColorStyle + " " + borderColorStyle);
    }
}
