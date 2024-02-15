package ru.metapunk.battleships;

import ru.metapunk.battleships.net.Server;

public class GameServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.serve();
    }
}
