package ru.metapunk.battleships.net.observer;

import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;

public interface IClientJoinGameObserver extends IClientEventsObserver {
    public void onLobbiesReceived(OpenLobbiesResponseDto lobbyListDto);
}
