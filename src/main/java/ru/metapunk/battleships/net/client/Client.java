package ru.metapunk.battleships.net.client;

import ru.metapunk.battleships.net.dto.response.JoinLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;
import ru.metapunk.battleships.net.dto.response.CreateLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.WhoseTurnResponseDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerJoinedSignalDto;
import ru.metapunk.battleships.net.dto.signal.OtherPlayerReadySignalDto;
import ru.metapunk.battleships.observer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class Client implements Runnable {
    private static final String SERVER_DEFAULT_ADDRESS = "localhost";
    private static final int SERVER_DEFAULT_PORT = 25821;

    private final String clientId;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private IClientEventsObserver eventsObserver;

    public Client() {
        this.clientId = UUID.randomUUID().toString();
        try {
            socket = new Socket(SERVER_DEFAULT_ADDRESS, SERVER_DEFAULT_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server");
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    @Override
    public void run() {
        receiveDto();
    }

    private void receiveDto() {
        try {
            Object dto;
            while (true) {
                dto = in.readObject();
                if (dto instanceof CreateLobbyResponseDto) {
                    ((IClientMainObserver) eventsObserver)
                            .onLobbyCreated((CreateLobbyResponseDto) dto);
                } else if (dto instanceof OpenLobbiesResponseDto) {
                    ((IClientJoinGameObserver) eventsObserver)
                            .onLobbiesReceived((OpenLobbiesResponseDto) dto);
                } else if (dto instanceof JoinLobbyResponseDto) {
                    ((IClientJoinGameObserver) eventsObserver)
                            .onJoinLobbyResponse((JoinLobbyResponseDto) dto);
                } else if (dto instanceof OtherPlayerJoinedSignalDto) {
                    ((IClientLobbyAwaitingObserver) eventsObserver)
                            .onOtherPlayerJoined((OtherPlayerJoinedSignalDto) dto);
                } else if (dto instanceof OtherPlayerReadySignalDto) {
                    ((IClientGameAwaitingObserver) eventsObserver)
                            .onOtherPlayerReady();
                } else if (dto instanceof WhoseTurnResponseDto) {
                    ((IClientGameObserver) eventsObserver)
                            .onWhoseTurnResponse((WhoseTurnResponseDto) dto);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    public void sendDto(Object dto) {
        try {
            out.writeObject(dto);
            out.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setEventsObserver(IClientEventsObserver eventsObserver) {
        this.eventsObserver = eventsObserver;
    }
}
