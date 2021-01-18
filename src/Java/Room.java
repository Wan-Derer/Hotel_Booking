package Java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;

@JsonAutoDetect
public class Room {
    private int roomID;
    private String roomDescription;
    private double roomPrice;
    private Date regDate;

//    public Room(int roomID, String roomDescription, double roomPrice, Date regDate) {
//        this.roomID = roomID;
//        this.roomDescription = roomDescription;
//        this.roomPrice = roomPrice;
//        this.regDate = regDate;
//    }


    public Room() { }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "{" +
                "\"roomID\":" + roomID +
                ", \"roomDescription\":\"" + roomDescription + '\"' +
                ", \"roomPrice\":" + roomPrice +
                ", \"regDate\":\"" + regDate + '\"' +
                "}\n";
    }
}
