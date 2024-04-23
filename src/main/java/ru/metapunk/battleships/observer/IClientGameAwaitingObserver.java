package ru.metapunk.battleships.observer;

public interface IClientGameAwaitingObserver extends IClientEventsObserver {
    public void onOtherPlayerReady();
}
