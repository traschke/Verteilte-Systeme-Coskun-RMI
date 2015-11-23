package de.bht.vs.rmi.bulletinboard.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
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
    public static final int userTimeout = 3 * 60;
    public static final String nameOfService = "Pinnwand";
    public static final String psk = "codegeruch";

    private List<User> userList;

    private List<Message> messages;
    private Thread lifetimeThread;
    private Thread userTimeoutThread;

    protected BulletinBoardServer() throws RemoteException {
        super();
        messages = new ArrayList<>();
        this.userList = new ArrayList<>();

        Runnable msgCheck = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        checkMsgsLifeTime();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.lifetimeThread = new Thread(msgCheck);
        this.lifetimeThread.start();

        Runnable userCheck = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        checkUserTimeout();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        this.userTimeoutThread = new Thread(userCheck);
        this.userTimeoutThread.start();
    }

    @Override
    public int login(String password) throws RemoteException {
        if (password.equals(psk)) {
            try {
                User user = new User(this.getClientHost(), userTimeout);
                this.userList.add(user);
                LOGGER.info("User " + user.getUsername() + " logged in!");
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
                return 0;
            }
            return 1;
        }
        LOGGER.info("Password incorrect.");
        return 0;
    }

    @Override
    public int getMessageCount() throws RemoteException {
        if (this.isLoggedIn()) {
            LOGGER.info("Messages count: " + this.messages.size());
            return this.messages.size();
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return -1;
    }

    @Override
    public String[] getMessages() throws RemoteException {
        if (this.isLoggedIn()) {
            List<String> strMsgs = new ArrayList<>();
            for (Message message:this.messages) {
                strMsgs.add(message.getMessage());
            }
            String[] msgsStrings = strMsgs.toArray(new String[strMsgs.size()]);
            return msgsStrings;
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return null;
    }

    @Override
    public String getMessage(int index) throws RemoteException {
        if (this.isLoggedIn()) {
            LOGGER.info("Message: " + this.messages.get(index));
            return this.messages.get(index).getMessage();
        }
        LOGGER.warning("Client not logged in. Access denied.");
        return null;
    }

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        if (this.isLoggedIn()) {
            if (this.messages.size() < maxNumMessages) {
                if (msg.length() < maxLengthMessage) {
                    LOGGER.info("Message \"" + msg + "\" added to bulletin board.");
                    this.messages.add(new Message(msg, messageLifeTime));
                    return true;
                } else {
                    LOGGER.info("Message \"" + msg + "\" has more than " + maxLengthMessage + " characters. Not added.");
                    return false;
                }
            } else {
                LOGGER.info("Server has already " + maxNumMessages + " Messages stored. \"" + msg + "\" not added.");
                return false;
            }
        } else {
            LOGGER.warning("Client not logged in. Access denied.");
            return false;
        }
    }

    private void checkMsgsLifeTime() {
        List<Message> msgRemove = new ArrayList<>();
        for (Message message : this.messages) {
            if (!message.isStillAlive()) {
                msgRemove.add(message);
            }
        }

        for (Message message:msgRemove) {
            this.messages.remove(message);
            LOGGER.info("Message " + message.getMessage() + " removed, because it's to old.");
        }
    }

    private void checkUserTimeout() {
        List<User> usersToRemove = new ArrayList<>();
        for (User user : this.userList) {
            if (!user.isStillAlive()) {
                usersToRemove.add(user);
            }
        }

        for (User user : usersToRemove) {
            this.userList.remove(user);
            LOGGER.info("User \"" + user.getUsername() + "\" logged out, because of inactivity.");
        }
    }

    private boolean isLoggedIn() {
        String username = null;
        try {
            username = this.getClientHost();
            for (User user : this.userList) {
                if (user.getUsername().equals(username)) {
                    user.action();
                    return true;
                }
            }
            return false;
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        BulletinBoardServer bulletinBoardServer = new BulletinBoardServer();
        String url = "rmi://localhost/" + BulletinBoardServer.nameOfService;
        Naming.rebind(url, bulletinBoardServer);
        LOGGER.info("BulletinServer bound. Access at: " + url);
    }
}
