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
        String msg = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            bulletinServer = (BulletinBoardServerInterface) Naming.lookup("rmi://141.64.169.203/Pinnwand");
            System.out.println("Logging in at: " + bulletinServer.toString());
            bulletinServer.login("codegeruch");
            System.out.println("Check Message Count...");
            messageCount = bulletinServer.getMessageCount();
            System.out.println("Message Count= " + messageCount);
            if (messageCount > 19) {
                System.out.println("You already have posted the maximum of 20 messages.");
            }
            System.out.print("$>");
            while (msg.length() < 160) {
                System.out.println(msg.length());
                msg = br.readLine();
            }
            bulletinServer.putMessage(msg);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
