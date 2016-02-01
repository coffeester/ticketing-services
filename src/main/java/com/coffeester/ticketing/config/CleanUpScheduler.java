package com.coffeester.ticketing.config;

import com.coffeester.ticketing.repository.SeatRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by amitsehgal on 1/30/16.
 */
@Component
public class CleanUpScheduler {

    private static final Logger logger = LoggerFactory
            .getLogger(CleanUpScheduler.class);

    @Autowired
    private SeatRepository seatRepository;


    @Scheduled(cron = "${ticketing.hold.expiry.schedule}")
    public void cleanUpHolds() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        logger.info("Cleaning up the hold expiry entries: " + simpleDateFormat.format(date));
        seatRepository.cleanUpSeatHolds();
    }


}
