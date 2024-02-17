package ru.metapunk.battleships.net;

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

    public Client() {

    }

    @Override
    public void run() {
        try {
            socket = new Socket(SERVER_DEFAULT_ADDRESS, SERVER_DEFAULT_PORT);
            System.out.println("Connected to server");

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            receiveDto();
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + e.getCause());
        }
    }

    private void receiveDto() {
        try {
            Object dto;
            while ((dto = in.readObject()) != null) {
                // TODO
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendDto(Object dto) {
        try {
            out.writeObject(dto);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
