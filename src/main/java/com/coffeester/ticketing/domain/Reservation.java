package com.coffeester.ticketing.domain;

import java.util.Date;

/**
 * Created by amitsehgal on 1/30/16.
 */
public class Reservation {

    int id;
    int status;
    int level;
    int customerId;
    String confirmationCode;
    int numberofSeats;
    Date holdExpiryTime;

    public Reservation(int id) {
        this.id = id;
    }

    public Date getHoldExpiryTime() {
        return holdExpiryTime;
    }

    public void setHoldExpiryTime(Date holdExpiryTime) {
        this.holdExpiryTime = holdExpiryTime;
    }

    public int getNumberofSeats() {
        return numberofSeats;
    }

    public void setNumberofSeats(int numberofSeats) {
        this.numberofSeats = numberofSeats;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }
}
