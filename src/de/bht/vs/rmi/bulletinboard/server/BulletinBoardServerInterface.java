package de.bht.vs.rmi.bulletinboard.server;

import java.rmi.RemoteException;

/**
 * Created by Timo on 09.11.2015.
 */
public interface BulletinBoardServerInterface {
    int login (String password) throws RemoteException;
    int getMessageCount() throws RemoteException;
    String[] getMessages() throws RemoteException;
    String getMessage(int index) throws RemoteException;
    boolean putMessage(String msg) throws RemoteException;
}
