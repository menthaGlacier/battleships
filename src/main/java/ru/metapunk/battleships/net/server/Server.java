package ru.metapunk.battleships.net.server;

import ru.metapunk.battleships.model.ship.ShipsData;
import ru.metapunk.battleships.model.tile.cell.Cell;
import ru.metapunk.battleships.net.dto.EnemyShotDto;
import ru.metapunk.battleships.net.dto.response.*;
import ru.metapunk.battleships.net.dto.signal.GameFinishedSignalDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerJoinedSignalDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerReadySignalDto;
import ru.metapunk.battleships.net.dto.signal.PassedTurnSignalDto;
import ru.metapunk.battleships.net.game.*;

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

    public void handleOpenLobbiesRequest(ClientHandler client) {
        synchronized (lobbies) {
            List<Lobby> lobbyList = new ArrayList<>(lobbies.values());
            client.sendDto(new OpenLobbiesResponseDto(lobbyList));
        }
    }

    public void handleLobbyAbandoned(String lobbyId) {
        synchronized (lobbies) {
            lobbies.remove(lobbyId);
        }
    }

    public void handlePlayerBoardSetup(String gameId, String playerId, Cell[][] cells) {
        Game game;
        synchronized (games) {
            game = games.get(gameId);
            if (game == null) {
                return;
            }
        }

        synchronized (game) {
            if (game.getPlayerOne().getId().equals(playerId)) {
                game.setPlayerOneBoard(cells);
            } else if (game.getPlayerTwo().getId().equals(playerId)) {
                game.setPlayerTwoBoard(cells);
            } else {
                return;
            }

            if (game.getPlayerOneBoard() != null && game.getPlayerTwoBoard() != null) {
                OtherPlayerReadySignalDto dto = new OtherPlayerReadySignalDto();
                game.getPlayerOne().getClientHandler().sendDto(dto);
                game.getPlayerTwo().getClientHandler().sendDto(dto);
            }
        }
    }

    public void handleWhoseTurnRequest(ClientHandler client, String gameId, String playerId) {
        Game game;
        synchronized (games) {
            game = games.get(gameId);
            if (game == null) {
                return;
            }
        }

        if (game.getWhoseTurn() == WhoseTurn.PLAYER_ONE
                && game.getPlayerOne().getId().equals(playerId)) {
            client.sendDto(new WhoseTurnResponseDto(true));
            return;
        }

        if (game.getWhoseTurn() == WhoseTurn.PLAYER_TWO
                && game.getPlayerTwo().getId().equals(playerId)) {
            client.sendDto(new WhoseTurnResponseDto(true));
            return;
        }

        client.sendDto(new WhoseTurnResponseDto(false));
    }

    private void processGameFinish(ClientHandler winner, ClientHandler loser,
                                   String gameId) {
        winner.sendDto(new GameFinishedSignalDto(true));
        loser.sendDto(new GameFinishedSignalDto(false));
        synchronized (games) {
            games.remove(gameId);
        }
    }

    public void handlePlayerSurrendered(ClientHandler client,
                                        String gameId, String playerId) {
        Game game;
        synchronized (games) {
            game = games.get(gameId);
            if (game == null) {
                return;
            }
        }

        ClientHandler otherPlayerClient;
        if (game.getPlayerOne().getId().equals(playerId)) {
            otherPlayerClient = game.getPlayerTwo().getClientHandler();
        } else if (game.getPlayerTwo().getId().equals(playerId)) {
            otherPlayerClient = game.getPlayerOne().getClientHandler();
        } else {
            return;
        }

        processGameFinish(otherPlayerClient, client, gameId);
    }

    private void makeShot(ClientHandler client, Game game, String gameId,
                          String playerId, int row, int column) {
        final ShotWrapper shotWrapper = new ShotWrapper();

        ClientHandler otherPlayerClient;
        ShipsData otherPlayerShipsData;
        Cell[][] otherPlayerBoard;

        if (game.getWhoseTurn() == WhoseTurn.PLAYER_ONE
                && game.getPlayerOne().getId().equals(playerId)) {
            otherPlayerClient = game.getPlayerTwo().getClientHandler();
            otherPlayerShipsData = game.getPlayerTwoShipsData();
            otherPlayerBoard = game.getPlayerTwoBoard();
        } else if (game.getWhoseTurn() == WhoseTurn.PLAYER_TWO
                && game.getPlayerTwo().getId().equals(playerId)) {
            otherPlayerClient = game.getPlayerOne().getClientHandler();
            otherPlayerShipsData = game.getPlayerOneShipsData();
            otherPlayerBoard = game.getPlayerOneBoard();
        } else {
            client.sendDto(new ShotEnemyTileResponseDto(false, row, column,
                    shotWrapper.getIsShotConnected(),
                    shotWrapper.getIsShipDestroyed(),
                    shotWrapper.getDestroyedShip()));
            return;
        }

        if (otherPlayerBoard[row][column].getBombarded()) {
            client.sendDto(new ShotEnemyTileResponseDto(false, row, column,
                    shotWrapper.getIsShotConnected(),
                    shotWrapper.getIsShipDestroyed(),
                    shotWrapper.getDestroyedShip()));
            return;
        }

        otherPlayerBoard[row][column].setBombarded(true);
        otherPlayerShipsData.handleShotData(row, column, shotWrapper);
        client.sendDto(new ShotEnemyTileResponseDto(true, row, column,
                shotWrapper.getIsShotConnected(),
                shotWrapper.getIsShipDestroyed(),
                shotWrapper.getDestroyedShip()));
        otherPlayerClient.sendDto(new EnemyShotDto(row, column,
                shotWrapper.getDestroyedShip()));

        if (!shotWrapper.getIsShotConnected()) {
            game.passTurn();
            otherPlayerClient.sendDto(new PassedTurnSignalDto());
            return;
        }

        if (otherPlayerShipsData.getTotalShipsAlive() == 0) {
            processGameFinish(client, otherPlayerClient, gameId);
        }
    }

    public void handleShotEnemyTile(ClientHandler client, String gameId,
                                    String playerId, int row, int column) {
        Game game;
        synchronized (games) {
            game = games.get(gameId);
            if (game == null) {
                return;
            }
        }

        synchronized (game) {
            makeShot(client, game, gameId, playerId, row, column);
        }
    }
}
