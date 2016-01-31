-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema ticketing
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS ticketing ;

-- -----------------------------------------------------
-- Schema ticketing
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS ticketing ;
USE ticketing ;

-- -----------------------------------------------------
-- Table ticketing.LEVEL
-- -----------------------------------------------------
DROP TABLE IF EXISTS ticketing.LEVEL ;

CREATE TABLE IF NOT EXISTS ticketing.LEVEL (
  ID INT NOT NULL,
  LEVEL_NAME VARCHAR(100) NULL,
  PRICE DECIMAL(10) NULL,
  NUMBER_OF_ROW INT NULL,
  SEATS_PER_ROW INT NULL,
  PRIMARY KEY (ID))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table ticketing.SEAT
-- -----------------------------------------------------
DROP TABLE IF EXISTS ticketing.SEAT ;

CREATE TABLE IF NOT EXISTS ticketing.SEAT (
  ID INT NOT NULL AUTO_INCREMENT,
  SEAT_NUMBER INT NULL,
  ROW_NUMBER INT NOT NULL,
  LEVEL INT NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_LEVEL
    FOREIGN KEY (LEVEL)
    REFERENCES ticketing.LEVEL (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX FK_LEVEL_idx ON ticketing.SEAT (LEVEL ASC);


-- -----------------------------------------------------
-- Table ticketing.CUSTOMER
-- -----------------------------------------------------
DROP TABLE IF EXISTS ticketing.CUSTOMER ;

CREATE TABLE IF NOT EXISTS ticketing.CUSTOMER (
  ID INT NOT NULL AUTO_INCREMENT,
  EMAIL VARCHAR(45) NOT NULL,
  PRIMARY KEY (ID))
ENGINE = InnoDB;

CREATE UNIQUE INDEX EMAIL_UNIQUE ON ticketing.CUSTOMER (EMAIL ASC);


-- -----------------------------------------------------
-- Table ticketing.STATUS
-- -----------------------------------------------------
DROP TABLE IF EXISTS ticketing.STATUS ;

CREATE TABLE IF NOT EXISTS ticketing.STATUS (
  ID INT NOT NULL,
  STATUS_DESC VARCHAR(20) NOT NULL,
  PRIMARY KEY (ID))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table ticketing.RESERVATION
-- -----------------------------------------------------
DROP TABLE IF EXISTS ticketing.RESERVATION ;

CREATE TABLE IF NOT EXISTS ticketing.RESERVATION (
  ID INT NOT NULL AUTO_INCREMENT,
  STATUS INT NULL,
  LEVEL INT NULL,
  CUSTOMER_ID INT NOT NULL,
  CONFIRMATION_CODE VARCHAR(36) NOT NULL,
  NUMBER_OF_SEATS INT NULL,
  HOLD_TIME DATETIME NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_CUSTOMER
    FOREIGN KEY (CUSTOMER_ID)
    REFERENCES ticketing.CUSTOMER (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_RESERVATION_STATUS
    FOREIGN KEY (STATUS)
    REFERENCES ticketing.STATUS (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_RESERVATION_LEVEL
    FOREIGN KEY (LEVEL)
    REFERENCES ticketing.LEVEL (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX FK_STATUS_idx ON ticketing.RESERVATION (STATUS ASC);

CREATE INDEX FK_LEVEL_RESER_idx ON ticketing.RESERVATION (LEVEL ASC);

CREATE INDEX FK_CUSTOMER_idx ON ticketing.RESERVATION (CUSTOMER_ID ASC);


-- -----------------------------------------------------
-- Table ticketing.SEAT_HOLD
-- -----------------------------------------------------
DROP TABLE IF EXISTS ticketing.SEAT_HOLD ;

CREATE TABLE IF NOT EXISTS ticketing.SEAT_HOLD (
  ID INT NOT NULL AUTO_INCREMENT,
  SEAT_ID INT NOT NULL,
  RESERVATION_ID INT NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_SEAT_ID
    FOREIGN KEY (SEAT_ID)
    REFERENCES ticketing.SEAT (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_RESERVATION_ID
    FOREIGN KEY (RESERVATION_ID)
    REFERENCES ticketing.RESERVATION (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX FK_RESERVATION_ID_idx ON ticketing.SEAT_HOLD (RESERVATION_ID ASC);

CREATE INDEX FK_SEAT_ID_idx ON ticketing.SEAT_HOLD (SEAT_ID ASC);
