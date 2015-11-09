package de.bht.vs.rmi.bulletinboard.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Timo on 09.11.2015.
 */
public class BulletinBoardServer extends UnicastRemoteObject implements BulletinBoardServerInterface {

    protected BulletinBoardServer() throws RemoteException {
    }

    @Override
    public int login(String password) throws RemoteException {
        return 0;
    }

    @Override
    public int getMessageCount() throws RemoteException {
        return 0;
    }

    @Override
    public String[] getMessages() throws RemoteException {
        return new String[0];
    }

    @Override
    public String getMessage(int index) throws RemoteException {
        return null;
    }

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        return false;
    }
}
