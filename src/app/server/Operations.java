package app.server;

import app.main.User;
import app.utils.FileIO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Operations {
    synchronized public static void register(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
        try {
            User user = (User) receiveObj.readObject();
            System.out.println(" - Attempt to registration");
            if (!Server.data.containsKey(user.getEmail())) {
                Server.data.put(user.getEmail(), user);
                FileIO.writeObjToFile(Server.data, "database.ser");
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
            User user = (User) receiveObj.readObject();
            System.out.println(" - Attempt to Login");
            System.out.println(" - Passwords Hash (C): " + user.getPasswords());
            System.out.println(" - Passwords Hash (S): " + Server.data.get(user.getEmail()).getPasswords());
            if (Server.data.containsKey(user.getEmail())) {
                if (Server.data.get(user.getEmail()).getPasswords().equals(user.getPasswords())) {
                    System.out.println(" - login success");
                    sendObj.writeObject("SUCCESS");
                    System.out.println(" - sending user info");
                    user = Server.data.get(user.getEmail());
                    sendObj.writeObject(user);
                    System.out.println(" - info send successfully!");
                    return;
                }
            }
            System.out.println(" - Invalid credentials, login failed!!");
            sendObj.writeObject("FAILED!");
        }catch (Exception ignore) {}
    }

    synchronized public static void updateInfo(ObjectInputStream receiveObj) {
        try {
            User user = (User) receiveObj.readObject();
            System.out.println(" - Attempt to update user info");
            Server.data.put(user.getEmail(), user);
            System.out.println(" - New Pass Hash: " + user.getPasswords());
            FileIO.writeObjToFile(Server.data, "database.ser");
            System.out.println(" - Update Info Successful");
        }catch (Exception ignore) {}
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
