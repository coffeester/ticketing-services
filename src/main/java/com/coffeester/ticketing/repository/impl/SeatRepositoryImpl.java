package com.coffeester.ticketing.repository.impl;

import com.coffeester.ticketing.domain.Reservation;
import com.coffeester.ticketing.domain.Seat;
import com.coffeester.ticketing.domain.SeatHold;
import com.coffeester.ticketing.domain.SeatHoldBuilder;
import com.coffeester.ticketing.domain.Status;
import com.coffeester.ticketing.repository.SeatRepository;
import com.coffeester.ticketing.util.SQLConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.transaction.Transactional;


/**
 * Created by amitsehgal on 1/30/16.
 */
@Component
public class SeatRepositoryImpl implements SeatRepository {

    private static final Logger logger = LoggerFactory
            .getLogger(SeatRepositoryImpl.class);
    @Autowired
    Environment env;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private DataSource dataSource;


    @PostConstruct
    public void initialize() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Integer findAvailableSeats(Integer level) {
        int availableSeats;
        try {
            availableSeats = this.jdbcTemplate.queryForObject(SQLConstants
                    .SQL_QUERY_TOTAL_SEATS_AVAILABLE, new
                    Object[]{
                    level, level
            }, Integer.class);
            logger.info("Number of available seats = {} in level={}", availableSeats, level);
            return availableSeats;
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    @Transactional
    @Override
    public SeatHold findAndHoldSeats(List<Seat> seats, String customerEmail, Integer level) {

        Assert.notNull(seats, "Seats cannot be null");
        Assert.notEmpty(seats, "Seats cannot be empty");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String confirmationNumber = UUID.randomUUID().toString();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement pst =
                                con.prepareStatement(SQLConstants.INSERT_RESERVATION_SQL, new
                                        String[]{"id"});
                        pst.setInt(1, Status.HOLD.getStatus());
                        pst.setInt(2, level);
                        pst.setInt(3, findByCustomerEmail(customerEmail));
                        pst.setString(4, confirmationNumber);
                        pst.setInt(5, seats.size());

                        return pst;
                    }
                },
                keyHolder);
        Long reservationId = keyHolder.getKey().longValue();

        Reservation reservation = findReservation(reservationId);

        jdbcTemplate.batchUpdate(SQLConstants.INSERT_SEAT_HOLD,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, seats.get(i).getId());
                        ps.setLong(2, reservationId);
                    }

                    @Override
                    public int getBatchSize() {
                        return seats.size();
                    }
                }
        );

        return new SeatHoldBuilder()
                .setSeatHoldId(reservation.getId())
                .setEmailId(customerEmail)
                .setHoldExpiryTime(dateFormat(reservation.getHoldExpiryTime()))
                .setSeats(seats)
                .createSeatHold();
    }


    private Reservation findReservation(long reservationId) {
        Reservation reservation;
        try {
            reservation = this.jdbcTemplate.queryForObject(SQLConstants.SELECT_RESERVATION, new
                    Object[]{
                    reservationId
            }, new ReservationMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

        logger.info("Reservation code returned :" + reservation.getConfirmationCode());
        return reservation;

    }


    private List<Integer> findReservationbyHoldStatus() {

        try {
            return this.jdbcTemplate.queryForList(SQLConstants.SELECT_RESERVATION_BY_STATUS, new
                    Object[]{
                    Status.HOLD.getStatus()
            }, Integer.class);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    @Override
    public List<Seat> getSeatsDetail(int level) {
        try {
            return this.jdbcTemplate.query(SQLConstants.SQL_NONRESERVED_SEAT_IDS, new Object[]{
                    level
            }, new SeatMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Transactional
    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        Reservation reservation = null;
        try {
            this.jdbcTemplate.update(SQLConstants.SQL_RESERVE_SEATS, Status.RESERVED.getStatus(),
                    seatHoldId);
            reservation = findReservation(seatHoldId);
        } catch (DuplicateKeyException de) {

        }
        return reservation.getConfirmationCode();
    }

    @Override
    public void cleanUpSeatHolds() {
        List<Integer> listOfIds = findReservationbyHoldStatus();
        Assert.notNull(listOfIds, "List of IDs cannot be null");
        String idsToBeDeleted = listOfIds.toString().replace("[", "").replace("]", "");
        if (idsToBeDeleted != null && !idsToBeDeleted.isEmpty()) {
            deleteFromSeatHold(idsToBeDeleted);
            deleteFromReservation(idsToBeDeleted);
        }
    }


    private void deleteFromSeatHold(String IdsToBeDeleted) {
        int rows = jdbcTemplate.update(SQLConstants.DELETE_SEAT_HOLD_SQL, IdsToBeDeleted);
    }

    private void deleteFromReservation(String IdsToBeDeleted) {
        int rows = jdbcTemplate.update(SQLConstants.DELETE_RESERVATION_SQL, IdsToBeDeleted);
    }

    @Override
    public Integer findByCustomerEmail(String customerEmail) {
        Integer customerId;
        String schemaName = env.getProperty("db.schema.name") != null ? env.getProperty("db.schema" +
                ".name") + "." : "";
        try {
            customerId = jdbcTemplate.queryForObject("SELECT ID FROM " +
                    schemaName + "CUSTOMER WHERE " +
                    "EMAIL = ?", new
                    Object[]{
                    customerEmail
            }, Integer.class);
            return customerId;
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }

    }

    private String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private static class SeatMapper implements RowMapper<Seat> {

        public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapToSeat(rs);
        }

        protected Seat mapToSeat(ResultSet rs) throws SQLException {

            int id = rs.getInt("ID");
            int seatNumber = rs.getInt("SEAT_NUMBER");
            int rowNumber = rs.getInt("ROW_NUMBER");
            int level = rs.getInt("LEVEL");

            Seat seat = new Seat(id);
            seat.setSeatNumber(seatNumber);
            seat.setRowNumber(rowNumber);
            seat.setLevel(level);
            return seat;
        }


    }

    private static class ReservationMapper implements RowMapper<Reservation> {

        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapToReservation(rs);
        }

        protected Reservation mapToReservation(ResultSet rs) throws SQLException {

            int id = rs.getInt("ID");
            int status = rs.getInt("STATUS");
            int level = rs.getInt("LEVEL");
            String confirmationCode = rs.getString("CONFIRMATION_CODE");
            int numberofSeats = rs.getInt("NUMBER_OF_SEATS");
            Date holdTime = rs.getTimestamp("HOLD_TIME");


            Reservation reservation = new Reservation(id);
            reservation.setStatus(status);
            reservation.setLevel(level);
            reservation.setConfirmationCode(confirmationCode);
            reservation.setNumberofSeats(numberofSeats);
            reservation.setHoldExpiryTime(holdTime);
            return reservation;
        }
    }
}
