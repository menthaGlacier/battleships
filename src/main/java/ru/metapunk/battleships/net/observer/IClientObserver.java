package ru.metapunk.battleships.net.observer;

import ru.metapunk.battleships.net.dto.OpenLobbiesResponseDto;

public interface IClientObserver extends IClientEventsObserver {
    public void onLobbyCreated();
    //public void onLobbiesReceived(OpenLobbiesResponseDto lobbyListDto);
}
