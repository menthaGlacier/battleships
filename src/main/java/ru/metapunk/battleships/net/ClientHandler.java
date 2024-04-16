package ru.metapunk.battleships.net;

import ru.metapunk.battleships.net.dto.CreateLobbyRequestDto;
import ru.metapunk.battleships.net.dto.OpenLobbiesRequestDto;
import ru.metapunk.battleships.net.dto.OpenLobbiesResponseDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            receiveDto();
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage() + "\n" + e.getCause());
            }
        }
    }

    private void receiveDto() {
        try {
            Object dto;
            while ((dto = in.readObject()) != null) {
                System.out.println("Received DTO " + dto);
                if (dto instanceof CreateLobbyRequestDto) {
                    server.handleCreateLobbyRequest(this);
                }

                if (dto instanceof OpenLobbiesRequestDto) {
                    server.handleOpenLobbiesRequest(this);
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
}
