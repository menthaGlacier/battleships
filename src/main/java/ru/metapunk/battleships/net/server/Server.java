package ru.metapunk.battleships.net.server;

import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.net.Game;
import ru.metapunk.battleships.net.Lobby;
import ru.metapunk.battleships.net.Player;
import ru.metapunk.battleships.net.dto.response.CreateLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.JoinLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerJoinedSignalDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerReadySignalDto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public final static int DEFAULT_PORT = 25821;
    private final List<ClientHandler> clients;
    private final Map<String, Lobby> lobbies;
    private final Map<String, Game> games;

    private final int port;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.lobbies = new HashMap<>();
        this.games = new HashMap<>();
    }

    public void serve() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                synchronized (clients) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler client = new ClientHandler(this, clientSocket);
                    clients.add(client);
                    new Thread(client).start();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    public void disconnect(InetSocketAddress address) {
        synchronized (clients) {
            clients.removeIf(client -> client.getInetSocketAddress().equals(address));
        }
    }

    public void handleCreateLobbyRequest(Player host) {
        synchronized (lobbies) {
            String lobbyId = UUID.randomUUID().toString();
            Lobby lobby = new Lobby(lobbyId, host);
            lobbies.put(lobbyId, lobby);
            host.getClientHandler().sendDto(new CreateLobbyResponseDto(lobbyId));
        }
    }

    public void handleOpenLobbiesRequest(ClientHandler client) {
        synchronized (lobbies) {
            List<Lobby> lobbyList = new ArrayList<>(lobbies.values());
            client.sendDto(new OpenLobbiesResponseDto(lobbyList));
        }
    }

    public void handleJoinLobbyRequest(String lobbyId, Player player) {
        Lobby lobby;
        synchronized (lobbies) {
            lobby = lobbies.get(lobbyId);
            if (lobby == null) {
                player.getClientHandler()
                        .sendDto(new JoinLobbyResponseDto(lobbyId, false));
                return;
            }

            lobbies.remove(lobbyId, lobby);
        }

        synchronized (games) {
            String gameId = UUID.randomUUID().toString();
            Game game = new Game(gameId, lobby.getHost(), player);
            games.put(gameId, game);
            player.getClientHandler().sendDto(new JoinLobbyResponseDto(gameId, true));
            lobby.getHost().getClientHandler()
                    .sendDto(new OtherPlayerJoinedSignalDto(gameId));
        }
    }

    public void handlePlayerBoardSetup(String gameId, String playerId, Cell[][] cells) {
        Game game = games.get(gameId);
        if (game == null) {
            return;
        }
        if (game.getPlayerOne().getId().equals(playerId)) {
            game.setPlayerOneBoard(cells);
            if (game.getPlayerTwoBoard() != null) {
                game.getPlayerOne().getClientHandler().sendDto(new OtherPlayerReadySignalDto());
                game.getPlayerTwo().getClientHandler().sendDto(new OtherPlayerReadySignalDto());
            }
        } else if (game.getPlayerTwo().getId().equals(playerId)) {
            game.setPlayerTwoBoard(cells);
            if (game.getPlayerOneBoard() != null) {
                game.getPlayerOne().getClientHandler().sendDto(new OtherPlayerReadySignalDto());
                game.getPlayerTwo().getClientHandler().sendDto(new OtherPlayerReadySignalDto());
            }
        }
    }
}
