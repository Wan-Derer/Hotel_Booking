package Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Java.DAO.DB_PWD;
import static Java.DAO.DB_USER;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class AppTest {
    private static Connection connection;

    @Before
    public void init() throws SQLException {
        connection = getNewConnection();
    }

    @After
    public void close() throws SQLException {
        connection.close();
    }

    private Connection getNewConnection() throws SQLException {
        String host = "jdbc:mysql://localhost/Hotel_Booking";
        return DriverManager.getConnection(host, DB_USER, DB_PWD);
    }



    @Test
    public void testDbConnection() throws SQLException {
        try (Connection connection = getNewConnection()) {
            assertTrue(connection.isValid(1));
            assertFalse(connection.isClosed());
        }

    }


    public static void main(String[] args) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-1-99");
        System.out.println(date);
    }

}
