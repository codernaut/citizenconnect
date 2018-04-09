package org.cfp.citizenconnect.Model;

/**
 * Created by shahzaibshahid on 08/02/2018.
 */

public class MessageEvent {

    public final String message;
    public final boolean status;

    public MessageEvent(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}