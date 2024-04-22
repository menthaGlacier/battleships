package ru.metapunk.battleships.observer;

import ru.metapunk.battleships.net.dto.response.CreateLobbyResponseDto;

public interface IClientMainObserver extends IClientEventsObserver {
    public void onLobbyCreated(CreateLobbyResponseDto createLobbyResponseDto);
}
