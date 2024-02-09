package ru.metapunk.battleships.model.tile;

public class Cell {
    private CellType type;
    private CellWarSide warSide;
    private CellShipPresence shipPresence;
    private boolean bombarded;

    public Cell() {
        this(CellType.SINGULAR, CellWarSide.NEUTRAL, CellShipPresence.ABSENT);
    }

    public Cell(CellType type, CellWarSide warSide, CellShipPresence shipPresence) {
        this.type = type;
        this.warSide = warSide;
        this.shipPresence = shipPresence;
        this.bombarded = false;
    }

    public Cell(CellWarSide warSide) {
        this(CellType.SINGULAR, warSide, CellShipPresence.ABSENT);
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public CellWarSide getWarSide() {
        return warSide;
    }

    public void setWarSide(CellWarSide warSide) {
        this.warSide = warSide;
    }

    public CellShipPresence getShipPresence() {
        return shipPresence;
    }

    public void setShipPresence(CellShipPresence shipPresence) {
        this.shipPresence = shipPresence;
    }

    public boolean getBombarded() {
        return bombarded;
    }

    public void setBombarded(boolean bombarded) {
        this.bombarded = bombarded;
    }
}
