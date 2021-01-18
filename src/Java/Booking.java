package Java;


import java.sql.Date;

public class Booking {
    private int bookingID;
    private int roomID;
    private Date bookingStart;  // use java.sql.Date !!!
    private Date bookingEnd;    //

    public Booking() { }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public Date getBookingStart() {
        return bookingStart;
    }

    public void setBookingStart(Date bookingStart) {
        this.bookingStart = bookingStart;
    }

    public Date getBookingEnd() {
        return bookingEnd;
    }

    public void setBookingEnd(Date bookingEnd) {
        this.bookingEnd = bookingEnd;
    }

    @Override
    public String toString() {
        return "{" +
                "\"bookingID\":" + bookingID +
                ", \"bookingStart\":\"" + bookingStart + "\"" +
                ", \"bookingEnd\":\"" + bookingEnd + "\"" +
                "}\n";
    }
}








