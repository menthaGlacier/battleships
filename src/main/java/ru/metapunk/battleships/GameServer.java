package ru.metapunk.battleships;

import ru.metapunk.battleships.net.server.Server;

public class GameServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.serve();
    }
}
