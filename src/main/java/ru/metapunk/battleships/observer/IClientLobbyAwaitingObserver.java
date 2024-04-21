package ru.metapunk.battleships.observer;

public interface IClientLobbyAwaitingObserver extends IClientEventsObserver {
    public void onOtherPlayerJoined();
}
