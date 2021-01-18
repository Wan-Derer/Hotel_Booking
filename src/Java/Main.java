package Java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    private static final int HTTP_PORT = 9000;

    public static void main(String[] args) throws IOException {
        DAO dao;
        try {
            dao = new DAO();
        } catch (SQLException e) {
            System.out.println("Unable to connect to database. \n" +
                    "Please check if DB server is started and try again\n" + e);
            return;
        }

        DAO finalDao = dao;

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);


        //curl -X POST -H "Content-Type: application/json" \
        // -d '{"roomDescription":"Комната 1-35", "roomPrice":1234.56}' \
        // http://localhost:9000/rooms/create
        httpServer.createContext("/rooms/create", httpExchange -> {
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String status;
                InputStream inputStream = httpExchange.getRequestBody();
                ObjectMapper mapper = new ObjectMapper();

                Room room = mapper.readValue(inputStream, Room.class);

                try {
                    int roomID = finalDao.addRoom(room);
                    status = "{\"roomID\":" + roomID + "}\n";
                } catch (IllegalArgumentException e) {
                    status = e.getMessage();
                } catch (SQLException e) {
                    status = "{\"error\":\"Database error. " + e.getMessage() + "\"}\n";
                }

                inputStream.close();

                httpExchange.sendResponseHeaders(200, status.getBytes().length);
                OutputStream output = httpExchange.getResponseBody();
                output.write(status.getBytes());
                output.flush();
            } else {
                httpExchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
            }
            httpExchange.close();
        });


        // curl -X POST -H "Content-Type: application/json" -d '{"roomID":2}' \
        // http://localhost:9000/rooms/delete
        httpServer.createContext("/rooms/delete", httpExchange -> {
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String status = "{\"error\":\"Incorrect arguments. Nothing to delete\"}\n";
                InputStream inputStream = httpExchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String query = reader.readLine();
                inputStream.close();

                query = query.substring(1, query.length() - 1); // delete "{" and "}"
                String[] queryArray = query.split(":");
                System.out.println(Arrays.toString(queryArray));

                if (queryArray[0].equals("\"roomID\"")) {
                    try {
                        int roomID = Integer.parseInt(queryArray[1]);
                        if (finalDao.delRoomByID(roomID) == 1) {
                            status = "{\"ok\":\"Room #" + roomID + " and it's bookings has " +
                                    "deleted\"}\n";
                        }
                    } catch (NumberFormatException e) {
                        // incorrect arguments
                    } catch (SQLException e) {
                        status = "{\"error\":\"Database error. " + e.getMessage() + "\"}\n";
                    }
                } // else - incorrect arguments

                httpExchange.sendResponseHeaders(200, status.getBytes().length);
                OutputStream output = httpExchange.getResponseBody();
                output.write(status.getBytes());
                output.flush();
            } else {
                httpExchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
            }
            httpExchange.close();
        });


        // curl -X GET "http://localhost:9000/rooms/list?sortedBy=room_price&order=DESC"
        httpServer.createContext("/rooms/list", httpExchange -> {
            if ("GET".equals(httpExchange.getRequestMethod())) {
                String respText;

                String[] arguments = httpExchange.getRequestURI().getRawQuery().split("=|&");

                if (arguments.length == 4) {
                    try {
                        respText = dao.getAllRooms(arguments[1], arguments[3]).toString();
                    } catch (SQLException e) {
                        respText = "{\"error\":\"Database error. " + e.getMessage() + "\"}\n";
                    } catch (IllegalArgumentException e) {
                        respText = e.getMessage();
                    }
                } else respText = "{\"error\":\"Incorrect arguments\"}\n";


                httpExchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = httpExchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                httpExchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
            }
            httpExchange.close();
        });


        //curl -X POST -H "Content-Type: application/json" \
        // -d '{"roomID":2, "bookingStart":"2021-01-23", "bookingEnd":"2021-02-02"}' \
        // http://localhost:9000/bookings/create
        httpServer.createContext("/bookings/create", httpExchange -> {
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String status;
                InputStream inputStream = httpExchange.getRequestBody();
                ObjectMapper mapper = new ObjectMapper();

                Booking booking = mapper.readValue(inputStream, Booking.class);
                System.out.println(booking);
                try {
                    int bookingID = finalDao.addBooking(booking);
                    status = "{\"bookingID\":" + bookingID + "}\n";
                } catch (IllegalArgumentException e) {
                    status = "{\"error\":\"Incorrect argument(s). " + e.getMessage() + "\"}\n";
                } catch (SQLException e) {
                    status = "{\"error\":\"Database error. " + e.getMessage() + "\"}\n";
                }

                inputStream.close();

                httpExchange.sendResponseHeaders(200, status.getBytes().length);
                OutputStream output = httpExchange.getResponseBody();
                output.write(status.getBytes());
                output.flush();
            } else {
                httpExchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
            }
            httpExchange.close();
        });


        // curl -X POST -H "Content-Type: application/json" -d '{"bookingID":2}' \
        // http://localhost:9000/bookings/delete
        httpServer.createContext("/bookings/delete", httpExchange -> {
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String status = "{\"error\":\"Incorrect arguments. Nothing to delete\"}\n";
                InputStream inputStream = httpExchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String query = reader.readLine();
                inputStream.close();

                query = query.substring(1, query.length() - 1); // delete "{" and "}"
                String[] queryArray = query.split(":");
                System.out.println(Arrays.toString(queryArray));

                if (queryArray[0].equals("\"bookingID\"")) {
                    System.out.println("uytrew");
                    try {
                        int bookingID = Integer.parseInt(queryArray[1]);
                        System.out.println(bookingID);
                        if (finalDao.delBookingByID(bookingID) == 1) {
                            status = "{\"ok\":\"Booking #" + bookingID + " has deleted\"}\n";
                        }
                    } catch (NumberFormatException e) {
                        // incorrect arguments
                    } catch (SQLException e) {
                        status = "{\"error\":\"Database error. " + e.getMessage() + "\"}\n";
                    }
                } // else - incorrect arguments

                httpExchange.sendResponseHeaders(200, status.getBytes().length);
                OutputStream output = httpExchange.getResponseBody();
                output.write(status.getBytes());
                output.flush();
            } else {
                httpExchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
            }
            httpExchange.close();
        });


        // curl -X GET "http://localhost:9000/bookings/list?room_id=5"
        httpServer.createContext("/bookings/list", httpExchange -> {
            if ("GET".equals(httpExchange.getRequestMethod())) {
                String respText;

                String[] arguments = httpExchange.getRequestURI().getRawQuery().split("=|&");

                if (arguments.length == 2) {
                    try {
                        int roomID = Integer.parseInt(arguments[1]);
                        respText = dao.getBookingsByRoomID(roomID).toString();
                    } catch (SQLException e) {
                        respText = "{\"error\":\"Database error. " + e.getMessage() + "\"}\n";
                    } catch (IllegalArgumentException e) {
                        respText = "{\"error\":\"Incorrect arguments. " + e.getMessage() + " \"}\n";
                    }
                } else respText = "{\"error\":\"Incorrect arguments\"}\n";


                httpExchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = httpExchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                httpExchange.sendResponseHeaders(405, -1);  // 405 Method Not Allowed
            }
            httpExchange.close();
        });


        httpServer.setExecutor(null);   // default executor
        httpServer.start();

    }
}


/*

curl -X POST -d "description=Комната 1-35” -d "price=1234.56" http://localhost:9000/rooms/create



 * Примеры запросов и ответов:
 * Запрос создания брони: `curl -X POST -d "room_id=24” -d "date_start=2021-12-30" -d "date_end=2022-01-02" http://localhost:9000/bookings/create`
 * Ответ: `{"booking_id": 1444}`
 * Запрос списка броней номера: `curl -X GET "http://localhost:9000/bookings/list?room_id=24"`
 * Ответ: `[{"booking_id": 1444, "date_start": "2021-12-30", "date_end": "2022-01-02"}, ...]`
 */