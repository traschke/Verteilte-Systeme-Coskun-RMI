package de.bht.vs.rmi.bulletinboard.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timo on 09.11.2015.
 */
public class BulletinBoardServer extends UnicastRemoteObject implements BulletinBoardServerInterface {

    public static final int maxNumMessages = 20;
    public static final int messageLifeTime = 10 * 60;
    public static final int maxLengthMessage = 160;
    public static final String nameOfService = "Pinnwand";
    public static final String psk = "codegeruch";

    private boolean loggedIn = false;
    private List<String> messages;

    protected BulletinBoardServer() throws RemoteException {
        super();
        messages = new ArrayList<>();
    }

    @Override
    public int login(String password) throws RemoteException {
        if (password.equals(this.psk)) {
            loggedIn = true;
            return 1;
        }
        return 0;
    }

    @Override
    public int getMessageCount() throws RemoteException {
        if (loggedIn) {
            return this.messages.size();
        }
        return -1;
    }

    @Override
    public String[] getMessages() throws RemoteException {
        if (loggedIn) {
            return this.messages.toArray(new String[messages.size()]);
        }
        return new String[0];
    }

    @Override
    public String getMessage(int index) throws RemoteException {
        if (loggedIn) {
            return this.messages.get(index);
        }
        return null;
    }

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        if (loggedIn) {
            this.messages.add(msg);
        }
        return false;
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        BulletinBoardServer bulletinBoardServer = new BulletinBoardServer();
        String url = "rmi://localhost/" + BulletinBoardServer.nameOfService;
        Naming.rebind(url, bulletinBoardServer);
        System.out.println("BulletinServer bound. Access at: " + url);
    }
}
