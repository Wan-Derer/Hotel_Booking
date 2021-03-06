CREATE SCHEMA `Hotel_Booking` ;

CREATE TABLE `Hotel_Booking`.`room` (
  `room_id` INT NOT NULL AUTO_INCREMENT,
  `room_description` VARCHAR(100) NULL,
  `room_price` DECIMAL(6,2) NULL,
  `reg_date` DATE DEFAULT (CURDATE()),
  PRIMARY KEY (`idroom`));

CREATE TABLE `Hotel_Booking`.`booking` (
  `booking_id` INT NOT NULL AUTO_INCREMENT,
  `room_id` INT NULL,
  `date_start` DATE NULL,
  `date_end` DATE NULL,
  PRIMARY KEY (`idbooking`),
  INDEX `idroom_idx` (`idroom` ASC) VISIBLE,
  CONSTRAINT `idroom`
    FOREIGN KEY (`idroom`)
    REFERENCES `Hotel_Booking`.`room` (`idroom`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

