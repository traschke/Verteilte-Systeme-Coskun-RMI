package de.bht.vs.rmi.bulletinboard.client;

import de.bht.vs.rmi.bulletinboard.server.BulletinBoardServerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Timo on 09.11.2015.
 * Edited by Khaled Reguieg on 20.11.2015.
 */
public class BulletinBoardClient {

    private static String serverAddress = "141.64.171.187";
    private static BulletinBoardServerInterface bulletinServer;
    private static String input;

    public static void main(String args[]) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(serverAddress);
        if (args.length != 0)
            serverAddress = args[0];
        try {
            bulletinServer = (BulletinBoardServerInterface) Naming.lookup("rmi://" + serverAddress + "/Pinnwand");
            System.out.println(bulletinServer);
            hilfe();
            while ((input = br.readLine()) != null) {
                if (input.equals("hilfe")) {
                    hilfe();
                    System.out.println("\n\n$>");
                }
                if (input.startsWith("login")) {
                    login();
                }
                if (input.startsWith("getMsgCount")) {
                    System.out.println("Message count: " + bulletinServer.getMessageCount());
                }
                if (input.startsWith("getMsgs")) {
                    getMsgs();
                }
                if (input.startsWith("getMsgAt")) {
                    getMsgAt();
                }
                if (input.startsWith("putMsg")) {
                    putMsg();
                }
                if (input.equals("ende")) {
                    break;
                }
                System.out.print("$>");
            }
        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void hilfe() {
        System.out.println("hilfe\t\t -\tRuft die Hilfe auf.\n" +
                "login\t\t -\tLoggt den User ein.\n" +
                "getMsgCount\t -\tGibt die Anzahl der Nachrichten aus.\n" +
                "getMsgs\t\t -\tGibt alle Nachrichten aus.\n" +
                "getMsgAt <i> -\tGibt die Nachricht an der Stelle i aus.\n" +
                "putMsg\t\t -\tErstellt eine Nachricht an der Pinnwand.\n" +
                "ende\t\t -\tBeendet das Programm.");
    }

    private static void login() throws RemoteException {
        System.out.println("Logging in at: " + bulletinServer.toString());
        if (bulletinServer.login("codegeruch") == 0) {
            System.out.println("Login failed.");
            return;
        }
        System.out.println("Login successful.");
    }

    private static void putMsg() throws RemoteException {
        if (bulletinServer.getMessageCount() > 19) {
            System.out.println("To many messages.");
            return;
        }
        String msg = input.split("putMsg ")[1];
        if (msg.isEmpty()) {
            System.out.println("There is no message written, to put on bulletin board.");
            return;
        }
        if (msg.length() > 160) {
            System.out.println("Message to long. Maximum of 160 Characters required.");
            return;
        }
        System.out.println("\"" + msg + "\"");
        if (!bulletinServer.putMessage(msg)) {
            System.out.println("You are not logged in.");
        }
    }

    private static void getMsgAt() throws RemoteException {
        int messageCount = bulletinServer.getMessageCount();

        if (!(messageCount == -1)) {
            if (messageCount == 0) {
                System.out.println("No messages...");
                return;
            }
            int i = Integer.parseInt(input.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1]);
            System.out.println("Message[" + i + "]= " + bulletinServer.getMessage(i - 1));
        } else {
            System.out.println("You are not logged in.");
        }
    }

    private static void getMsgs() throws RemoteException {
        String[] messages = bulletinServer.getMessages();
        if (messages != null) {
            if (messages.length == 0) {
                System.out.println("No messages available...");
                return;
            }

            int counter = 0;
            for (String message : messages) {
                System.out.println("[Messages " + ++counter + ": " + message + "\n\n");
            }
        } else {
            System.out.println("You are not logged in.");
        }
    }
}
