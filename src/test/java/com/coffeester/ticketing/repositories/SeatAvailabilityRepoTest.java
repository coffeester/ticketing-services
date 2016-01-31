package com.coffeester.ticketing.repositories;

import com.coffeester.ticketing.repository.SeatRepository;
import com.coffeester.ticketing.repository.impl.SeatRepositoryImpl;
import com.coffeester.ticketing.testconfigs.TestSeatAvailabilityConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by amitsehgal on 1/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest({"test.db.sql=classpath:test-seat-availability.sql", "spring.datasource" +
        ".url=jdbc:h2:mem:seatav;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "test.db" +
        ".name=seatav"})
@SpringApplicationConfiguration(classes = {TestSeatAvailabilityConfig.class, SeatRepositoryImpl.class})
public class SeatAvailabilityRepoTest {


    @Autowired
    private SeatRepository seatRepository;

    @Test
    public void testSeatsAvailabilty() {
        Assert.assertTrue(seatRepository.findAvailableSeats(3) == 1500);
    }


    //@Test(expected = LevelDoesntExistsException.class)
    public void testLevelNotFound() {
        seatRepository.findAvailableSeats(7);
    }

    //level 4 has  1 seat and 1 row in DB and is reserved
    @Test
    public void testNotAvaiableSeats() {
        Assert.assertTrue(seatRepository.findAvailableSeats(4) == 0);
    }


}
