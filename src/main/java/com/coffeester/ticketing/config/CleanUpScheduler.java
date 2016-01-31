package com.coffeester.ticketing.config;

import com.coffeester.ticketing.repository.SeatRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by amitsehgal on 1/30/16.
 */
@Component
public class CleanUpScheduler {

    private static final Logger logger = LoggerFactory
            .getLogger(CleanUpScheduler.class);

    @Autowired
    private SeatRepository seatRepository;

    @Scheduled(fixedRate = 120000)
    public void cleanUpHolds() {
        logger.info("test logging");
        seatRepository.cleanUpSeatHolds();
    }


}
