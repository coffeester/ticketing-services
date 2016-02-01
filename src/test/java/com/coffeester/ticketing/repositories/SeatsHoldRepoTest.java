package com.coffeester.ticketing.repositories;

import com.coffeester.ticketing.domain.Seat;
import com.coffeester.ticketing.domain.SeatHold;
import com.coffeester.ticketing.repository.SeatRepository;
import com.coffeester.ticketing.repository.impl.SeatRepositoryImpl;
import com.coffeester.ticketing.testconfigs.TestSeatAvailabilityConfig;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitsehgal on 1/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest({"test.db.sql=classpath:test-seat-hold.sql", "spring.datasource" +
        ".url=jdbc:h2:mem:seathd;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "test.db.name=seathd"})
@SpringApplicationConfiguration(classes = {TestSeatAvailabilityConfig.class, SeatRepositoryImpl.class})
public class SeatsHoldRepoTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    List<Seat> seats = null;
    @Autowired
    private SeatRepository seatRepository;

    @org.junit.Before
    public void setUpTestData() {
        seats = new ArrayList<>();
        Seat seat1 = new Seat(1);
        seat1.setRowNumber(1);
        seat1.setLevel(1);
        seat1.setSeatNumber(5);
        Seat seat2 = new Seat(2);
        seat1.setRowNumber(1);
        seat1.setLevel(1);
        seat1.setSeatNumber(6);
        seats.add(seat1);
        seats.add(seat2);
    }


    @Test
    public void testSeatHold() {

        SeatHold seatHold = seatRepository.findAndHoldSeats(seats, "johndoe@example.com", 1);
        Assert.assertNotNull(seatHold);
        Assert.assertTrue(seatHold.getSeats().size() == 2);
        Assert.assertTrue(seatHold.getCustomerEmail().equals("johndoe@example.com"));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testWrongCustomerEmail() {
        SeatHold seatHold = seatRepository.findAndHoldSeats(seats, "johndoe1@example.com", 1);
    }

    @Test
    public void testSeatDetail() {
        List<Seat> seats3 = seatRepository.getSeatsDetail(1);
        Assert.assertTrue(seats3.size() == 0);
    }

    @Test
    public void testWrongLevel() {
        List<Seat> seats = seatRepository.getSeatsDetail(7);
        Assert.assertTrue(seats.size() == 0);
    }

    @Test
    public void testSeatHoldAndReserve() {

        SeatHold seatHold = seatRepository.findAndHoldSeats(seats, "johndoe@example.com", 1);
        Assert.assertNotNull(seatHold);
        Assert.assertTrue(seatHold.getSeats().size() == 2);
        Assert.assertTrue(seatHold.getCustomerEmail().equals("johndoe@example.com"));
        seatRepository.reserveSeats(seatHold.getSeatHoldId(), "johndoe@example.com");
    }


    @Test
    public void testGetSeatsHoldAndReserve() {

        List<Seat> seats2 = seatRepository.getSeatsDetail(1);

        SeatHold seatHold = seatRepository.findAndHoldSeats(seats2, "johndoe@example.com", 1);
        Assert.assertNotNull(seatHold);
        Assert.assertTrue(seatHold.getSeats().size() == 1);
        Assert.assertTrue(seatHold.getCustomerEmail().equals("johndoe@example.com"));
        seatRepository.reserveSeats(seatHold.getSeatHoldId(), "johndoe@example.com");
    }


}
