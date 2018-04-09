package org.cfp.citizenconnect;

/**
 * Created by shahzaibshahid on 16/01/2018.
 */

public class CustomCallBack {
    public interface Listener<T> {
        void onResponse(T response);
    }

    public interface ErrorListener<T> {
        void onErrorResponse(T error);
    }
}
