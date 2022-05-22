package app.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    public Socket sc;
    public ObjectInputStream receiveObj;
    public ObjectOutputStream sendObj;
    private int id;

    public ClientHandler(Socket sc) {
        try {
            this.sc = sc;
            OutputStream oo = sc.getOutputStream();
            sendObj = new ObjectOutputStream(oo);

            InputStream inputStream = sc.getInputStream();
            receiveObj = new ObjectInputStream(inputStream);

            id = ++Server.clientCount;
            Server.clients.put(this, Server.clientCount + "");
            System.out.println(" - Connected with client " + id + "!");
        } catch (IOException e) {
            close(sc, receiveObj, sendObj);
        }
    }

    @Override
    public void run() {
        String request;
        try {
            while (sc.isConnected()) {
                // Receiving Request
                request = (String) receiveObj.readObject();
                task(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            close(sc, receiveObj, sendObj);
            System.out.println(" - Disconnected client " + id + "!\n\n");
        }
    }

    void task(String request) {
        try {
            System.out.println(" - Received request for " + request);

            // Handling Request
            switch (request) {
                case "registration":
                    Operations.register(sendObj, receiveObj);
                    break;
                case "login":
                    Operations.login(sendObj, receiveObj);
                    break;
                case "updateInfo":
                    Operations.updateInfo(receiveObj);
                    break;
                case "addBalance":
                    Operations.addBalance(sendObj, receiveObj);
                    break;
                case "addToCart":
                    Operations.addToCart(sendObj, receiveObj);
                    break;
                case "buy":
                    Operations.buy(sendObj, receiveObj);
                    break;
//                case "addBus" -> {
//                    Operations.addBus(receiveObj, "refreshAll");
//                }
//                case "removeBus" -> {
//                    Operations.overWrite(receiveObj, "refreshAll");
//                }
//                case "buy" -> {
//                    Operations.overWrite(receiveObj, "refresh");
//                }
//                case "getBusList" -> Operations.getBusList(sendObj);
//                case "getBusData" -> Operations.getBusData(sendObj);
            }
        } catch (Exception e) {
            close(sc, receiveObj, sendObj);
            e.printStackTrace();
        }
    }

    void removeClientHandler() {
        System.out.println(" - Finish Serving client " + Server.clients.get(this));
        Server.clients.remove(this);
    }

    void close(Socket sc, ObjectInputStream ois, ObjectOutputStream oos) {
        removeClientHandler();
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (sc != null) sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
