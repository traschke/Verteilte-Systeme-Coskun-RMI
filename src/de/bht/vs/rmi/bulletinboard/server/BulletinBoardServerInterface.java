package de.bht.vs.rmi.bulletinboard.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Timo on 09.11.2015.
 */
public interface BulletinBoardServerInterface extends Remote {
    /**
     * Logs in a client.
     * @param password The server password.
     * @return 1 for success, 0 for failure.
     * @throws RemoteException
     */
    int login (String password) throws RemoteException;

    /**
     * Gets the the current count of messages.
     * @return The current count of messages. -1 if user is not logged in.
     * @throws RemoteException
     */
    int getMessageCount() throws RemoteException;

    /**
     * Gets all messages from server.
     * @return Array with all messages as strings. Null if user is not logged in.
     * @throws RemoteException
     */
    String[] getMessages() throws RemoteException;

    /**
     * Gets message at position.
     * @param index The position.
     * @return Message at index as string. Null if user is not logged in.
     * @throws RemoteException
     */
    String getMessage(int index) throws RemoteException;

    /**
     * Put message to the bulletin board.
     * @param msg The Messages as string.
     * @return true if message is posted, false if an error appeared.
     * @throws RemoteException
     */
    boolean putMessage(String msg) throws RemoteException;
}
