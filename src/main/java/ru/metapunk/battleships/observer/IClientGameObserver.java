package ru.metapunk.battleships.observer;

import ru.metapunk.battleships.net.dto.EnemyShotDto;
import ru.metapunk.battleships.net.dto.response.ShotEnemyTileResponseDto;
import ru.metapunk.battleships.net.dto.response.WhoseTurnResponseDto;
import ru.metapunk.battleships.net.dto.signal.GameFinishedSignalDto;

public interface IClientGameObserver extends IClientEventsObserver {
    public void onWhoseTurnResponse(WhoseTurnResponseDto data);
    public void onPassedTurn();
    public void onShotEnemyTileResponse(ShotEnemyTileResponseDto data);
    public void onEnemyShot(EnemyShotDto data);
    public void onGameFinished(GameFinishedSignalDto data);
}
