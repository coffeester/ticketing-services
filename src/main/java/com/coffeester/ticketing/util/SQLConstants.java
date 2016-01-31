package com.coffeester.ticketing.util;

import com.coffeester.ticketing.domain.Status;

/**
 * Created by amitsehgal on 1/30/16.
 */
public class SQLConstants {


    public static final String INSERT_RESERVATION_SQL = "INSERT INTO RESERVATION( STATUS, LEVEL, " +
            "CUSTOMER_ID, CONFIRMATION_CODE, NUMBER_OF_SEATS, HOLD_TIME) VALUES (?, ?, ?, ?, ?, " +
            "DATEADD('MINUTE', 30, CURRENT_TIMESTAMP))";
    //" CURRENT_TIMESTAMP() + interval 30 minute)"; //mysql syntax
    public static final String INSERT_SEAT_HOLD = "INSERT INTO SEAT_HOLD (SEAT_ID, RESERVATION_ID)" +
            " VALUES ( ?, ?)";
    public static final String SELECT_RESERVATION = "SELECT ID, STATUS, LEVEL , CUSTOMER_ID , " +
            "CONFIRMATION_CODE, NUMBER_OF_SEATS, HOLD_TIME FROM RESERVATION WHERE ID = ?";
    public static final String SELECT_RESERVATION_BY_STATUS = "SELECT ID from RESERVATION where " +
            "CURRENT_TIMESTAMP() >= HOLD_TIME AND STATUS = ?";


    public static String SQL_QUERY_TOTAL_SEATS_AVAILABLE = "SELECT " +
            "(SELECT NUMBER_OF_ROW*SEATS_PER_ROW FROM LEVEL WHERE ID = ?)" +
            " - " +
            "(SELECT COUNT(ID) FROM  RESERVATION  WHERE LEVEL = ? AND STATUS IN " +
            "(" + Status.HOLD.getStatus() + "," + Status.RESERVED.getStatus() + ")) " +
            "as SEATCOUNT";
    public static String SQL_NONRESERVED_SEAT_IDS = " SELECT ID, SEAT_NUMBER,ROW_NUMBER, LEVEL from SEAT where id in ( " +
            "SELECT ID FROM ( SELECT id FROM SEAT " +
            "UNION ALL SELECT SEAT_ID FROM SEAT_HOLD " +
            ") tbl " +
            "GROUP BY id " +
            "HAVING count(*) = 1 and " +
            "LEVEL = ? " +
            "ORDER BY id)";

    public static String SQL_RESERVE_SEATS = "UPDATE RESERVATION SET  STATUS = ? WHERE ID = ?";

    public static String DELETE_RESERVATION_SQL = "DELETE FROM RESERVATION WHERE ID IN (?)";
    public static String DELETE_SEAT_HOLD_SQL = "DELETE FROM SEAT_HOLD WHERE RESERVATION_ID IN " +
            "(?)";


}
