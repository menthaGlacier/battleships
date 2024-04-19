package ru.metapunk.battleships.net;

import ru.metapunk.battleships.net.dto.response.JoinLobbyResponseDto;
import ru.metapunk.battleships.net.dto.response.OpenLobbiesResponseDto;
import ru.metapunk.battleships.net.dto.response.CreateLobbyResponseDto;
import ru.metapunk.battleships.net.observer.IClientEventsObserver;
import ru.metapunk.battleships.net.observer.IClientJoinGameObserver;
import ru.metapunk.battleships.net.observer.IClientObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private static final String SERVER_DEFAULT_ADDRESS = "localhost";
    private static final int SERVER_DEFAULT_PORT = 25821;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private IClientEventsObserver eventsObserver;

    public Client() {
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
                    ((IClientObserver) eventsObserver).onLobbyCreated();
                } else if (dto instanceof OpenLobbiesResponseDto) {
                    ((IClientJoinGameObserver) eventsObserver)
                            .onLobbiesReceived((OpenLobbiesResponseDto) dto);
                } else if (dto instanceof JoinLobbyResponseDto) {
                    ((IClientJoinGameObserver) eventsObserver)
                            .onJoinLobbyResponse((JoinLobbyResponseDto) dto);
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

    public void setEventsObserver(IClientEventsObserver eventsObserver) {
        this.eventsObserver = eventsObserver;
    }
}
