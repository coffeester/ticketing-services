package com.coffeester.ticketing.rest.controller;

import com.coffeester.ticketing.domain.SeatHold;
import com.coffeester.ticketing.exception.LevelDoesntExistsException;
import com.coffeester.ticketing.service.TicketService;
import com.wordnik.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * Created by amitsehgal on 1/30/16.
 */
@Controller
@RequestMapping(TicketController.REQUEST_MAPPING)
@Api(value = "ticketing", description = "Ticketing services")
public class TicketController {

    public static final String REQUEST_MAPPING = "/api/v1/ticketing/";
    private static final Logger logger = LoggerFactory
            .getLogger(TicketController.class);


    @Autowired
    private TicketService ticketService;

    @RequestMapping(method = RequestMethod.GET, value = "/seats", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ExceptionHandler(LevelDoesntExistsException.class)
    public ResponseEntity<Integer> numSeatsAvailable(@RequestParam(value = "level")
                                                     Optional<Integer> level) {
        Integer numOfSeats = 0;
        logger.info("requested level for seats availability {}", level);
        numOfSeats = ticketService.numSeatsAvailable(level);
        logger.info("Number of seats returned {}", numOfSeats);
        return new ResponseEntity<Integer>(numOfSeats, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/hold", produces
            = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SeatHold> findAndHold(@RequestParam(value = "numberOfSeats")
                                                int numberOfSeats,
                                                @RequestParam
                                                        (value = "minLevel") Optional<Integer> minLevel,
                                                @RequestParam
                                                        (value = "maxLevel") Optional<Integer>
                                                        maxLevel,
                                                @RequestParam(value = "customerEmail") String
                                                        customerEmail) {

        logger.info("requested hold for numberOfSeats={} customer ={} ", numberOfSeats,
                customerEmail);

        SeatHold seatHold = ticketService.findAndHoldSeats(numberOfSeats, minLevel, maxLevel,
                customerEmail);
        return new ResponseEntity<SeatHold>(seatHold, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/reserve")
    @ResponseBody
    public ResponseEntity<String> reserveSeats(@RequestParam(value = "seatHoldId")
                                               int seatHoldId,
                                               @RequestParam(value = "customerEmail") String
                                                       customerEmail) {
        logger.info("Reserve tickets for customer ={} ", customerEmail);


        String confirmationCode = ticketService.reserveSeats(seatHoldId, customerEmail);
        logger.info("ConfirmationCode  code  returned ={} ", confirmationCode);

        return new ResponseEntity<String>("{confirmation_code : " + confirmationCode + "}", HttpStatus
                .OK);


    }
}
