package de.bht.vs.rmi.bulletinboard.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Timo on 09.11.2015.
 */
public class BulletinBoardServer extends UnicastRemoteObject implements BulletinBoardServerInterface {

    private final static Logger LOGGER = Logger.getLogger(BulletinBoardServer.class.getName());
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
            LOGGER.info("Password correct. Client logged in.");
            loggedIn = true;
            return 1;
        }
        LOGGER.info("Password incorrect.");
        return 0;
    }

    @Override
    public int getMessageCount() throws RemoteException {
        if (loggedIn) {
            LOGGER.info("Messages count: " + this.messages.size());
            return this.messages.size();
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return -1;
    }

    @Override
    public String[] getMessages() throws RemoteException {
        if (loggedIn) {
            LOGGER.info("Messages: " + this.messages.toArray(new String[messages.size()]));
            return this.messages.toArray(new String[messages.size()]);
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return null;
    }

    @Override
    public String getMessage(int index) throws RemoteException {
        if (loggedIn) {
            LOGGER.info("Message: " + this.messages.get(index));
            return this.messages.get(index);
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return null;
    }

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        if (loggedIn) {
            LOGGER.info("Message \"" + msg + "\" added to bulletin board.");
            this.messages.add(msg);
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return false;
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        BulletinBoardServer bulletinBoardServer = new BulletinBoardServer();
        String url = "rmi://localhost/" + BulletinBoardServer.nameOfService;
        Naming.rebind(url, bulletinBoardServer);
        LOGGER.info("BulletinServer bound. Access at: " + url);
    }
}
