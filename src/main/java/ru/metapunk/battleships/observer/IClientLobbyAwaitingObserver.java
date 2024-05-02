package ru.metapunk.battleships.observer;

import ru.metapunk.battleships.net.dto.signal.OtherPlayerJoinedSignalDto;

public interface IClientLobbyAwaitingObserver extends IClientEventsObserver {
    public void onOtherPlayerJoined(OtherPlayerJoinedSignalDto data);
}
