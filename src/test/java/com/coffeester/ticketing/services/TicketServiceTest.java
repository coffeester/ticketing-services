package com.coffeester.ticketing.services;

import com.coffeester.ticketing.domain.Seat;
import com.coffeester.ticketing.domain.SeatHold;
import com.coffeester.ticketing.exception.LevelDoesntExistsException;
import com.coffeester.ticketing.repository.impl.SeatRepositoryImpl;
import com.coffeester.ticketing.service.TicketService;
import com.coffeester.ticketing.service.impl.TicketServiceImpl;
import com.coffeester.ticketing.testconfigs.TestSeatAvailabilityConfig;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Created by amitsehgal on 1/30/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest({"test.db.sql=classpath:test-seat-hold.sql", "spring.datasource" +
        ".url=jdbc:h2:mem:tsvc;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "test.db.name=tsvc"})
@SpringApplicationConfiguration(classes = {TestSeatAvailabilityConfig.class, TicketServiceImpl
        .class, SeatRepositoryImpl.class})
public class TicketServiceTest {

    @Autowired
    TicketService ticketService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testAvailability() {
        //no data is inserted for level 4
        Assert.assertTrue(ticketService.numSeatsAvailable(Optional.of(4)) == 0);
        // row * number_of_seats
        Assert.assertTrue(ticketService.numSeatsAvailable(Optional.of(1)) == 1250);
    }


    @Test(expected = LevelDoesntExistsException.class)
    public void testNonAvailableLevel() {
        //no data is inserted for level 4
        int returnVal = ticketService.numSeatsAvailable(Optional.of(8));
        Assert.assertTrue(returnVal == 0);
    }

    @Test
    public void testHoldSeatsWrongEmail() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Not a valid email");
        ticketService.findAndHoldSeats(2, Optional.of(1), Optional.of(3), "wrongemailformat");
        ticketService.findAndHoldSeats(2, Optional.of(1), Optional.of(3),
                "wrongemailaddress@test.com");
    }

    @Test
    public void testHoldTicketsPossitive() {
        SeatHold seatHold = ticketService.findAndHoldSeats(1, Optional.of(1), Optional.of
                        (3),
                "johndoe@example.com");
        Assert.assertNotNull(seatHold);
        Assert.assertEquals(seatHold.getSeats().size(), 1);
        Assert.assertEquals(seatHold.getCustomerEmail(), "johndoe@example.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(seatHold.getHoldExpiryTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(date.getTime() > new Date().getTime());

    }

    @Test
    public void testHoldTicketsNoSeatsAvailable() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("No seats available in requested levels");
        SeatHold seatHold = ticketService.findAndHoldSeats(1, Optional.of(4), Optional.of
                        (4),
                "johndoe@example.com");
        System.out.println(seatHold.toString());
    }


    /**
     * make sure the booked seats are in sequence
     */

    @Test
    public void testSeatsHoldAndReserve() {

        String customerEmail = "johndoe@example.com";

            SeatHold seatHold = ticketService.findAndHoldSeats(3, Optional.of(2), Optional.of
                            (2),
                    customerEmail);
        // Assert the seats are in sequence
        Assert.assertTrue(seatHold.getSeats().get(0).getSeatNumber() == seatHold.getSeats().get
                (1).getSeatNumber() - 1);

        String confirmationCode = ticketService.reserveSeats(seatHold.getSeatHoldId(),
                customerEmail);
        Assert.assertNotNull(confirmationCode);

        //Total seats inserted in Seat table for level 2 are only 4
        //try to hold 3 more seats after 3 already held and reserved
        //You should get an exception with a message seat not available


        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("No seats available in requested levels");
        ticketService.findAndHoldSeats(3, Optional.of(2), Optional.of
                        (2),
                customerEmail);
    }


}
