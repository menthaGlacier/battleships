package ru.metapunk.battleships.observer;

import ru.metapunk.battleships.net.dto.response.WhoseTurnResponseDto;

public interface IClientGameObserver extends IClientEventsObserver {
    public void onWhoseTurnResponse(WhoseTurnResponseDto whoseTurnResponseDto);
}
