package ru.metapunk.battleships.net;

import ru.metapunk.battleships.net.dto.request.CreateLobbyRequestDto;
import ru.metapunk.battleships.net.dto.request.JoinLobbyRequestDto;
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
            while (true) {
                dto = in.readObject();
                // TODO make calls to "handleX" methods consistent
                // perhaps just by passing dto to handle methods
                if (dto instanceof CreateLobbyRequestDto) {
                    server.handleCreateLobbyRequest(this,
                            ((CreateLobbyRequestDto) dto).getPlayerId(),
                            ((CreateLobbyRequestDto) dto).getNickname());
                } else if (dto instanceof OpenLobbiesRequestDto) {
                    server.handleOpenLobbiesRequest(this);
                } else if (dto instanceof JoinLobbyRequestDto) {
                    server.handleJoinLobbyRequest(this,
                            ((JoinLobbyRequestDto) dto).getLobbyId(),
                            ((JoinLobbyRequestDto) dto).getPlayerId(),
                            ((JoinLobbyRequestDto) dto).getNickname());
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
