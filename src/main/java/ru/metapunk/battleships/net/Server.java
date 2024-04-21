package ru.metapunk.battleships.net;

import ru.metapunk.battleships.net.dto.response.CreateLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.JoinLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerJoinedSignalDto;

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

    public void handleCreateLobbyRequest(ClientHandler client, String playerId,
                                         String nickname) {
        String lobbyId = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(lobbyId);
        lobby.setPlayerOne(client, playerId, nickname);
        lobbies.put(lobbyId, lobby);
        client.sendDto(new CreateLobbyResponseDto(lobbyId));
    }

    public void handleOpenLobbiesRequest(ClientHandler client) {
        List<Lobby> lobbyList = new ArrayList<>(lobbies.values());
        client.sendDto(new OpenLobbiesResponseDto(lobbyList));
    }

    public void handleJoinLobbyRequest(ClientHandler client, String lobbyId,
                                       String playerId, String nickname) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            client.sendDto(new JoinLobbyResponseDto(lobbyId, false));
            return;
        }

        lobby.setPlayerTwo(client, playerId, nickname);
        lobby.setIsOpen(false);
        client.sendDto(new JoinLobbyResponseDto(lobbyId, true));
        lobby.getPlayerOne().sendDto(new OtherPlayerJoinedSignalDto());

    }
}
