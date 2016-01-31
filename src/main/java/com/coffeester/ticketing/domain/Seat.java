package com.coffeester.ticketing.domain;

/**
 * Created by amitsehgal on 1/30/16.
 */
public class Seat {

    Integer id;
    Integer rowNumber;
    Integer seatNumber;
    Integer level;

    public Seat(Integer id) {
        this.id = id;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;

        Seat seat = (Seat) o;

        if (getId() != null ? !getId().equals(seat.getId()) : seat.getId() != null) return false;
        if (getRowNumber() != null ? !getRowNumber().equals(seat.getRowNumber()) : seat.getRowNumber() != null)
            return false;
        if (getSeatNumber() != null ? !getSeatNumber().equals(seat.getSeatNumber()) : seat.getSeatNumber() != null)
            return false;
        return !(getLevel() != null ? !getLevel().equals(seat.getLevel()) : seat.getLevel() != null);

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRowNumber() != null ? getRowNumber().hashCode() : 0);
        result = 31 * result + (getSeatNumber() != null ? getSeatNumber().hashCode() : 0);
        result = 31 * result + (getLevel() != null ? getLevel().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                ", level=" + level +
                '}';
    }
}
