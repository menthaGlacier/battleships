package ru.metapunk.battleships.net;

import ru.metapunk.battleships.net.dto.request.CreateLobbyRequestDto;
import ru.metapunk.battleships.net.dto.request.OpenLobbiesRequestDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    @Override
    public void run() {
        try {
            Object dto;
            while ((dto = in.readObject()) != null) {
                if (dto instanceof CreateLobbyRequestDto) {
                    String nickname = ((CreateLobbyRequestDto) dto).getNickname();
                    server.handleCreateLobbyRequest(this, nickname);
                }

                if (dto instanceof OpenLobbiesRequestDto) {
                    server.handleOpenLobbiesRequest(this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        } finally {
            try {
                server.disconnect((InetSocketAddress) socket.getRemoteSocketAddress());
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage() + "\n" + e.getCause());
            }
        }
    }

    public void sendDto(Object dto) {
        try {
            out.writeObject(dto);
            out.flush();
        } catch (IOException e) {
            server.disconnect((InetSocketAddress) socket.getRemoteSocketAddress());
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    public InetSocketAddress getInetSocketAddress() {
        return (InetSocketAddress) socket.getRemoteSocketAddress();
    }
}
