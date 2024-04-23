package ru.metapunk.battleships.observer;

import ru.metapunk.battleships.net.dto.response.JoinLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;

public interface IClientJoinGameObserver extends IClientEventsObserver {
    public void onLobbiesReceived(OpenLobbiesResponseDto openLobbiesResponseDto);
    public void onJoinLobbyResponse(JoinLobbyResponseDto joinLobbyResponseDto);
}
