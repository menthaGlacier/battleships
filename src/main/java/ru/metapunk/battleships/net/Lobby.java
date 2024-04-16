package ru.metapunk.battleships.net;

public class Lobby {
    private final String lobbyId;
    private ClientHandler playerOne;
    private ClientHandler playerTwo;

    public Lobby(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public void setPlayerOne(ClientHandler playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(ClientHandler playerTwo) {
        this.playerTwo = playerTwo;
    }
}
