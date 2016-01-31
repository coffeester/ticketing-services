package com.coffeester.ticketing.repository;

import com.coffeester.ticketing.domain.Seat;
import com.coffeester.ticketing.domain.SeatHold;

import java.util.List;

/**
 * Created by amitsehgal on 1/30/16.
 */
public interface SeatRepository {

    Integer findAvailableSeats(Integer level);

    SeatHold findAndHoldSeats(List<Seat> seats, String customerEmail, Integer level);

    List<Seat> getSeatsDetail(int level);

    String reserveSeats(int seatHoldId, String customerEmail);


    void cleanUpSeatHolds();

    public Integer findByCustomerEmail(String email);
}
