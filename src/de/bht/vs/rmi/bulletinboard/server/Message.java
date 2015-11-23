package de.bht.vs.rmi.bulletinboard.server;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timo on 23.11.2015.
 */
public class Message {

    private String message;
    private Date dateAdded;
    private Date dateDead;

    public Message(String msg, int lifetime) {
        this.message = msg;
        this.dateAdded = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(this.dateAdded);
        c.add(Calendar.SECOND, lifetime);

        this.dateDead = c.getTime();
        System.out.println(dateAdded.getTime());
        System.out.println(dateDead.getTime());
    }

    public String getMessage() {
        return message;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public boolean isStillAlive() {
        Date now = new Date();
        if (now.getTime() > this.dateDead.getTime()) {
            return false;
        }
        return true;
    }
}
