package de.bht.vs.rmi.bulletinboard.client;

import de.bht.vs.rmi.bulletinboard.server.BulletinBoardServerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Timo on 09.11.2015.
 */
public class BulletinBoardClient {

    public static void main(String args[]) {

        BulletinBoardServerInterface bulletinServer;
        int messageCount;
        String input;
        String msg = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            bulletinServer = (BulletinBoardServerInterface) Naming.lookup("rmi://141.64.164.58/Pinnwand");
            while ((input = br.readLine()) != null) {
                if (input.equals("hilfe")) {
                    System.out.println("hilfe\t\t-\tRuft die Hilfe auf.\n" +
                            "login\t\t-\tLoggt den User ein.\n" +
                            "getMsgCount\t-\tGibt die Anzahl der Nachrichten aus.\n" +
                            "getMsgs\t\t-\tGibt alle Nachrichten aus.\n" +
                            "getMsgAt <i>\t\t-\tGibt die Nachricht an der Stelle i aus.\n" +
                            "putMsg\t\t-\tErstellt eine Nachricht an der Pinnwand.\n" +
                            "ende\t\t-\tBeendet das Programm.");
                }
                if (input.startsWith("login")) {
                    System.out.println("Logging in at: " + bulletinServer.toString());
                    if (bulletinServer.login("codegeruch") == 0) {
                        System.out.println("Login failed.");
                        break;
                    }
                    System.out.println("Login successful.");
                }
                if (input.startsWith("getMsgCount")) {
                    System.out.println("Message count: " + bulletinServer.getMessageCount());
                }
                if (input.startsWith("getMsgs")) {
                    System.out.println("Message count: " + bulletinServer.getMessages());
                }
                if (input.startsWith("getMsgsAt")) {
                    if (bulletinServer.getMessageCount() == 0) {
                        System.out.println("No messages..");
                        break;
                    }
                    int i = Integer.parseInt(input.split(" ")[1]);
                    System.out.println("Message at [" + i + "]= " + bulletinServer.getMessage(i));
                }
                if (input.startsWith("putMsg")) {
                    if (bulletinServer.getMessageCount() > 19) {
                        System.out.println("To many messages.");
                        break;
                    }
                    msg = input.split(" ")[1];
                    if (msg.isEmpty()) {
                        System.out.println("There is no.");
                        break;
                    }
                    if (msg.length() > 160) {
                        System.out.println("Message to long. Maximum of 160 Characters required.");
                    }
                    System.out.println("Printing Message:" + msg);
                    bulletinServer.putMessage(msg);

                }
                if (input.equals("ende")) {
                    break;
                }
                System.out.print("$>");
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
