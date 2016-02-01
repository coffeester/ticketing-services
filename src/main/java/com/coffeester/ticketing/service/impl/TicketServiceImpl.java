package com.coffeester.ticketing.service.impl;

import com.coffeester.ticketing.domain.Level;
import com.coffeester.ticketing.domain.Seat;
import com.coffeester.ticketing.domain.SeatHold;
import com.coffeester.ticketing.exception.ExceptionCode;
import com.coffeester.ticketing.exception.LevelDoesntExistsException;
import com.coffeester.ticketing.repository.SeatRepository;
import com.coffeester.ticketing.service.TicketService;
import com.coffeester.ticketing.util.Validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by amitsehgal on 1/30/16.
 */
@Service("ticketService")
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory
            .getLogger(TicketServiceImpl.class);

    SeatHold seatHold = null;

    @Value("${venue.highest.level:4}")
    private String highestLevel;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public int numSeatsAvailable(Optional<Integer> venueLevel) {

        if (venueLevel.isPresent() && venueLevel.get() > Integer.valueOf(highestLevel)) {
            throw new LevelDoesntExistsException(ExceptionCode.LEVEL_DOES_NOT_EXIST_1001,
                    "Level Doesn't exists");
        } else {
            return seatRepository.findAvailableSeats(venueLevel.get());
        }

    }

    /**
     * This method ietrates over levels and holds the best available seats
     *
     * @param numSeats      the number of seats to find and hold
     * @param minLevel      the minimum venue level
     * @param maxLevel      the maximum venue level
     * @param customerEmail unique identifier for the customer
     */

    @Override
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {


        if (!Validation.isEmailValid(customerEmail) || isCustomerDoesNotExists(customerEmail)) {
            logger.error("Not a valid email");
            throw new RuntimeException("Not a valid email");
        }


        //Start from lowest level to highest by default
        int startLevel = Level.ORCHESTRA.getLevel();
        int max = Level.BALCONY_2.getLevel();

        //min & max level is present
        if (maxLevel.isPresent() && minLevel.isPresent()) {
            startLevel = minLevel.get();
            max = maxLevel.get();
            //only min level is present
        } else if (!maxLevel.isPresent() && minLevel.isPresent()) {
            startLevel = minLevel.get();
            max = startLevel;
        } else if (maxLevel.isPresent() && !minLevel.isPresent()) {
            startLevel = maxLevel.get();
            max = startLevel;
        } else {
            throw new LevelDoesntExistsException(ExceptionCode.LEVEL_DOES_NOT_EXIST_1001, "No " +
                    "Atleast one level (min or max) is required");
        }

        List<Seat> seatsToHold = holdTheSeats(startLevel, max, numSeats);

        if (seatsToHold != null) {
            seatHold = seatRepository.findAndHoldSeats(seatsToHold, customerEmail, startLevel);
        }

        return seatHold;

    }

    /**
     * This method finds seats available in serial
     */

    private List<Seat> getSerialSeats(List<Seat> seats) {

        int minSubSequenceLength = 1;
        int maxSubSequenceLength = 1;
        int indexStart = 0;
        int indexEnd = 0;

        List<Seat> returnSeats = new ArrayList<>();

        for (int i = 0; i < seats.size() - 1; i++) {
            if (seats.get(i).getSeatNumber() == seats.get(i + 1).getSeatNumber() - 1) {
                minSubSequenceLength++;
                if (minSubSequenceLength > maxSubSequenceLength) {
                    maxSubSequenceLength = minSubSequenceLength;
                    indexStart = i + 2 - minSubSequenceLength;
                    indexEnd = i + 2;
                }

            } else {
                minSubSequenceLength = 1;
            }
        }

        for (int i = indexStart; i < indexEnd; i++) {
            returnSeats.add(seats.get(i));
        }

        return returnSeats;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        return seatRepository.reserveSeats(seatHoldId, customerEmail);
    }

    /**
     * This method holds the best available seats provided startlevel and max level
     */
    private List<Seat> holdTheSeats(int startLevel, int max, int numSeats) {
        List<Seat> seats = null;
        List<Seat> seatInSerial = null;
        while (startLevel <= max) {
            seats = seatRepository.getSeatsDetail(startLevel);
            //break as soon as we find the availability
            if (seats.size() >= numSeats) {
                break;
            } else if (startLevel == max) { //we are done searching for seats
                logger.warn("No seats available in requested levels");
                throw new RuntimeException("No seats available in requested levels");
            }
            startLevel++;
        }

        seatInSerial = getSerialSeats(seats);

        //if we can find seats is serial
        if (seatInSerial != null && seatInSerial.size() >= numSeats) {
            return removeExtraSeats(seatInSerial, numSeats);
        }

        return removeExtraSeats(seats, numSeats);
    }

    /**
     * check against db
     */

    boolean isCustomerDoesNotExists(String email) {
        if (seatRepository.findByCustomerEmail(email) > 0) {
            return false;
        }
        return true;
    }

    /**
     * remove extra seats from end
     */
    List<Seat> removeExtraSeats(List<Seat> seats, int numOfSeats) {

        List<Seat> seatsAfterRemoval = null;
        if (seats.size() > numOfSeats) {
            int extraSeats = seats.size() - numOfSeats;
            seatsAfterRemoval = seats.subList(0, numOfSeats);
            return seatsAfterRemoval;
        }
        return seats;
    }


}
