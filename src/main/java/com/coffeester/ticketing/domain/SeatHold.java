package com.coffeester.ticketing.domain;

import java.util.List;

/**
 * Created by amitsehgal on 1/30/16.
 */
public class SeatHold {
    Integer seatHoldId;
    List<Seat> seats;
    String holdExpiryTime;
    String customerEmail;

    SeatHold(Integer seatHoldId, List<Seat> seats, String holdExpiryTime, String emailId) {
        this.seatHoldId = seatHoldId;
        this.seats = seats;
        this.holdExpiryTime = holdExpiryTime;
        this.customerEmail = emailId;
    }

    public Integer getSeatHoldId() {
        return seatHoldId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public String getHoldExpiryTime() {
        return holdExpiryTime;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatHold)) return false;

        SeatHold seatHold = (SeatHold) o;

        if (getSeatHoldId() != null ? !getSeatHoldId().equals(seatHold.getSeatHoldId()) : seatHold.getSeatHoldId() != null)
            return false;
        if (getSeats() != null ? !getSeats().equals(seatHold.getSeats()) : seatHold.getSeats() != null)
            return false;
        if (getHoldExpiryTime() != null ? !getHoldExpiryTime().equals(seatHold.getHoldExpiryTime()) : seatHold.getHoldExpiryTime() != null)
            return false;
        return !(getCustomerEmail() != null ? !getCustomerEmail().equals(seatHold.getCustomerEmail()) : seatHold.getCustomerEmail() != null);

    }

    @Override
    public int hashCode() {
        int result = getSeatHoldId() != null ? getSeatHoldId().hashCode() : 0;
        result = 31 * result + (getSeats() != null ? getSeats().hashCode() : 0);
        result = 31 * result + (getHoldExpiryTime() != null ? getHoldExpiryTime().hashCode() : 0);
        result = 31 * result + (getCustomerEmail() != null ? getCustomerEmail().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "seatHoldId=" + seatHoldId +
                ", seats=" + seats +
                ", holdExpiryTime='" + holdExpiryTime + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
