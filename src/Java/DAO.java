package Java;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.sun.rowset.JdbcRowSetImpl;

import javax.sql.rowset.JdbcRowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String DB_NAME = "Hotel_Booking";
    public static final String DB_HOST = "jdbc:mysql://localhost/" + DB_NAME;
    public static final String DB_USER = "root";
    public static final String DB_PWD = "rootroot";
    private final Connection connection;

    private static final String ROOM = DB_NAME + ".room";
    private static final String BOOKING = DB_NAME + ".booking";


    public DAO() throws SQLException {
        connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PWD);
    }

    // Add new room, returns room_id
    // If incorrect arguments come to the method, the IllegalArgumentException will be thrown
    public int addRoom(Room room) throws SQLException {
        String description = room.getRoomDescription();
        double price = room.getRoomPrice();

        String error = "{\"error\":\"Incorrect arguments. No rooms added\"}\n";
        if (description == null || description.length() > 100) {
            throw new IllegalArgumentException(error);
        }
        if (price < 0 || price > 999999.99) throw new IllegalArgumentException(error);

        // add new room
        String query = "INSERT INTO " + ROOM + " (room_description, room_price) VALUES ('" +
                description + "', " + price + ")";
        System.out.println("DAO: " + executeUpdate(query) + " room(s) added");

        // get room_id last added
        JdbcRowSet rowSet = new JdbcRowSetImpl(connection);
        rowSet.setCommand("SELECT MAX(room_id) FROM " + ROOM);
        rowSet.execute();
        rowSet.next();
        return (rowSet.getInt(1));
    }

    // Delete one room by ID. All bookings for this room will also be deleted
    // (automatically, by DB)
    // Returns 1 if the room has deleted or 0 if incorrect room_id come to to method
    public int delRoomByID(int roomID) throws SQLException {
        String query = "DELETE FROM " + ROOM + " WHERE (room_id = " + roomID + ")";
        int result = executeUpdate(query);
        System.out.println("DAO: " + result + " room(s) deleted");
        return result;
    }

    // Get all the rooms
    // sortedBy - field for sort, may be "room_price" or "reg_date"
    // order may be "ASC" or "DESC"
    // If incorrect arguments come to the method, the IllegalArgumentException will be thrown
    public List<Room> getAllRooms(String sortedBy, String order) throws SQLException {
        if (sortedBy == null || order == null) throw new IllegalArgumentException();

        sortedBy = sortedBy.toLowerCase();
        if (!sortedBy.equals("room_price") && !sortedBy.equals("reg_date")) {
            throw new IllegalArgumentException("{\"error\":\"Incorrect arguments\"}\n");
        }

        if (order.isEmpty()) order = "ASC";
        if (!order.equalsIgnoreCase("ASC") &&
                !order.equalsIgnoreCase("DESC")) {
            throw new IllegalArgumentException("{\"error\":\"Incorrect arguments\"}\n");
        }

        String query = "SELECT * FROM " + ROOM + " ORDER BY " + sortedBy + " " + order;
        JdbcRowSet rowSet = new JdbcRowSetImpl(connection);
        rowSet.setCommand(query);
        rowSet.execute();

        List<Room> allRooms = new ArrayList<>();
        while (rowSet.next()) {
            Room room = new Room();
            room.setRoomID(rowSet.getInt("room_id"));
            room.setRoomDescription(rowSet.getString("room_description"));
            room.setRoomPrice(rowSet.getDouble("room_price"));
            room.setRegDate(rowSet.getDate("reg_date"));
            allRooms.add(room);
        }

        return allRooms;
    }

    // Add new booking, returns booking_id
    // If incorrect arguments come to the method, the IllegalArgumentException will be thrown
    public int addBooking(Booking booking) throws SQLException {
        int roomID = booking.getRoomID();
        Date start = booking.getBookingStart();
        Date end = booking.getBookingEnd();

        // add new booking
        String query = "INSERT INTO " + BOOKING +" (room_id, date_start, date_end) VALUES (" +
                roomID + ", '" + start + "', '" + end + "')";
        try {
            System.out.println("DAO: " + executeUpdate(query) + " booking(s) added");
        } catch (SQLIntegrityConstraintViolationException | MysqlDataTruncation e){
            System.out.println("DAO: incorrect arguments");
            throw new IllegalArgumentException(e.getMessage() + "\n");
        }

        // get booking_id last added
        JdbcRowSet rowSet = new JdbcRowSetImpl(connection);
        rowSet.setCommand("SELECT MAX(booking_id) FROM " + BOOKING);
        rowSet.execute();
        rowSet.next();
        return (rowSet.getInt(1));
    }

    // Delete one booking by ID
    // Returns 1 if the room has deleted or 0 if incorrect room_id come to to method
    public int delBookingByID(int bookingID) throws SQLException {
        String query = "DELETE FROM " + BOOKING + " WHERE (booking_id = " + bookingID + ")";
        int result = executeUpdate(query);
        System.out.println("DAO: " + result + " room(s) deleted");
        return result;
    }

    // Get all the bookings sorted by date_start
    public List<Booking> getAllBookings() throws SQLException {

        String query = "SELECT * FROM " + BOOKING + " ORDER BY date_start";
        JdbcRowSet rowSet = new JdbcRowSetImpl(connection);
        rowSet.setCommand(query);
        rowSet.execute();

        List<Booking> allBookings = new ArrayList<>();
        while (rowSet.next()) {
            Booking booking = new Booking();
            booking.setBookingID(rowSet.getInt("booking_id"));
            booking.setBookingStart(rowSet.getDate("date_start"));
            booking.setBookingEnd(rowSet.getDate("date_end"));
            allBookings.add(booking);
        }

        return allBookings;
    }

    // Get all the bookings for one room, sorted by date_start
    public List<Booking> getBookingsByRoomID(int roomID) throws SQLException {

        String query = "SELECT * FROM " + BOOKING +
                " WHERE room_id = " + roomID + " ORDER BY date_start";
        JdbcRowSet rowSet = new JdbcRowSetImpl(connection);
        rowSet.setCommand(query);
        rowSet.execute();

        List<Booking> selectedBookings = new ArrayList<>();
        while (rowSet.next()) {
            Booking booking = new Booking();
            booking.setBookingID(rowSet.getInt("booking_id"));
            booking.setBookingStart(rowSet.getDate("date_start"));
            booking.setBookingEnd(rowSet.getDate("date_end"));
            selectedBookings.add(booking);
        }

        return selectedBookings;
    }



    // for Insert, Update, Delete
    private int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }


}
