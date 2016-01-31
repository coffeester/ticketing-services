package com.coffeester.ticketing.domain;

/**
 * Created by amitsehgal on 1/30/16.
 */
public enum Status {

    AVAILABLE(1),
    HOLD(2),
    RESERVED(3);

    private int status;

    private Status(int s) {
        status = s;
    }

    public int getStatus() {
        return status;
    }

}
