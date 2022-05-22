package app.server;

import app.classes.Product;
import app.classes.User;
import app.main.Config;
import app.utils.FileIO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Operations {
    synchronized public static void register(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
        try {
            User user = (User) receiveObj.readObject();
            System.out.println(" - Attempt to registration");
            if (!Server.users.containsKey(user.getEmail())) {
                Server.users.put(user.getEmail(), user);
                FileIO.writeObjToFile(Server.users, Config.userDatabase);
                sendObj.writeObject("SUCCESS");
                System.out.println(" - Registration Successful");
            } else {
                sendObj.writeObject("FAILED!");
                System.out.println(" - Registration failed! Duplicate email found!");
            }
        }catch (Exception ignore) {}
    }

    public static void login(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
        try {
            System.out.println(" - Attempt to Login");
            User user = (User) receiveObj.readObject();
            User sUser = Server.users.get(user.getEmail());
            if (sUser != null) {
                System.out.println(" - Passwords Hash (C): " + user.getPasswords());
                System.out.println(" - Passwords Hash (S): " + sUser.getPasswords());
                if (sUser.getPasswords().equals(user.getPasswords())) {
                    System.out.println(" - login success");
                    sendObj.writeObject("SUCCESS");
                    System.out.println(" - sending user info");
                    sendObj.writeObject(sUser);
                    System.out.println(" - info send successfully!");
                    return;
                }
            }
            System.out.println(" - Invalid credentials, login failed!!");
            sendObj.writeObject("FAILED");
        }catch (Exception ignore) {}
    }

    synchronized public static void updateInfo(ObjectInputStream receiveObj) {
        try {
            User user = (User) receiveObj.readObject();
            System.out.println(" - Attempt to update user info");
            Server.users.put(user.getEmail(), user);
            System.out.println(" - New Pass Hash: " + user.getPasswords());
            FileIO.writeObjToFile(Server.users, Config.userDatabase);
            System.out.println(" - Update Info Successful");
        }catch (Exception ignore) {}
    }

    public static void addBalance(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
        try {
            User user = (User) receiveObj.readObject();
            double amount = (double) receiveObj.readObject();
            synchronized (Server.users) {
                Server.users.get(user.getEmail()).addBalance(amount);
                FileIO.writeObjToFile(Server.users, Config.userDatabase);
            }
            sendObj.writeObject("SUCCESS");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addToCart(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
        try {
            User user = (User) receiveObj.readObject();
            Product p = (Product) receiveObj.readObject();
            synchronized (Server.carts) {
                Server.carts.get(user.getEmail()).add(p);
                FileIO.writeObjToFile(Server.carts, Config.cartDatabase);
            }
            sendObj.writeObject("SUCCESS");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    synchronized public static void refresh(String cmd) {
//        System.out.println(" - Start Refreshing...");
//        try {
//            for (Map.Entry<ClientHandler, String> entry : Server.clients.entrySet()) {
//                System.out.println(" - Refresh Client " + entry.getValue());
//                entry.getKey().sendObj.writeObject(cmd);
//                entry.getKey().sendObj.writeObject(Server.busData);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
