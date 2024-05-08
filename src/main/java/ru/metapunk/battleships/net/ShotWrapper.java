package ru.metapunk.battleships.net;

import ru.metapunk.battleships.model.ship.Ship;

public class ShotWrapper {
    boolean isShotConnected;
    boolean isShipDestroyed;
    Ship destroyedShip;

    public ShotWrapper() {
        this.isShotConnected = false;
        this.isShipDestroyed = false;
        this.destroyedShip = null;
    }

    public boolean getIsShotConnected() {
        return isShotConnected;
    }

    public void setIsShotConnected(boolean shotConnected) {
        isShotConnected = shotConnected;
    }

    public boolean getIsShipDestroyed() {
        return isShipDestroyed;
    }

    public void setIsShipDestroyed(boolean shipDestroyed) {
        isShipDestroyed = shipDestroyed;
    }

    public void setDestroyedShip(Ship destroyedShip) {
        this.destroyedShip = destroyedShip;
    }

    public Ship getDestroyedShip() {
        return destroyedShip;
    }
}
