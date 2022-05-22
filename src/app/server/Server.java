package app.server;

import app.classes.Cart;
import app.classes.Product;
import app.classes.User;
import app.main.Config;
import app.utils.FileIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    public static int clientCount = 0;
    public static HashMap<ClientHandler, String> clients = new HashMap<>();
    public static HashMap<String, User> users = new HashMap<>();
    public static ArrayList<Product> products = new ArrayList<>();
    public static HashMap<String, Cart> carts = new HashMap<>();
    public static HashMap<String, ArrayList<Product>> history = new HashMap<>();
    public ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {

        try {
            FileIO.checkDB(Config.userDatabase, users);
            FileIO.checkDB(Config.cartDatabase, carts);
            FileIO.checkDB(Config.productDatabase, products);
            FileIO.checkDB(Config.historyDatabase, history);

            users = FileIO.readObjFromFile(Config.userDatabase);
            carts = FileIO.readObjFromFile(Config.cartDatabase);
            products = FileIO.readObjFromFile(Config.productDatabase);
            history = FileIO.readObjFromFile(Config.historyDatabase);

            System.out.println("Server is waiting for client.");

            while (!serverSocket.isClosed()) {
                Socket sc = serverSocket.accept();
                ClientHandler ch = new ClientHandler(sc);
                Thread t = new Thread(ch);
                t.start();
            }
        } catch (Exception e) {
            closeServer();
            e.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
