package com.coffeester.ticketing.domain;

import java.util.List;

public class SeatHoldBuilder {
    private Integer seatHoldId;
    private List<Seat> seats;
    private String holdExpiryTime;
    private String emailId;

    public SeatHoldBuilder setSeatHoldId(Integer seatHoldId) {
        this.seatHoldId = seatHoldId;
        return this;
    }

    public SeatHoldBuilder setSeats(List<Seat> seats) {
        this.seats = seats;
        return this;
    }

    public SeatHoldBuilder setHoldExpiryTime(String holdExpiryTime) {
        this.holdExpiryTime = holdExpiryTime;
        return this;
    }

    public SeatHoldBuilder setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    public SeatHold createSeatHold() {
        return new SeatHold(seatHoldId, seats, holdExpiryTime, emailId);
    }
}