1. Start your SQL server (I used MySQL for this job). Perform commands from ini.sql file to create 
   database and two tables.
   
2. Start application. HTTP server at localhost:9000 will be started.

3. Using `curl` utility send commands and receve answers. Also you can use your SQL server console to 
   monitor of records creation/deletion. 
   
Supported commands examples:

- room addition:

`curl -X POST -H "Content-Type: application/json" -d '{"roomDescription":"Комната 1-35", "roomPrice":1234.56}' http://localhost:9000/rooms/create`

- room delete:

`curl -X POST -H "Content-Type: application/json" -d '{"roomID":2}' http://localhost:9000/rooms/delete`

- get all the rooms:

`curl -X GET "http://localhost:9000/rooms/list?sortedBy=room_price&order=DESC"`

`sortedBy` - field for sort, may be "room_price" or "reg_date"

`order` may be "ASC" or "DESC"


- booking addition:

`curl -X POST -H "Content-Type: application/json" -d '{"roomID":2, "bookingStart":"2021-01-23", "bookingEnd":"2021-02-02"}' http://localhost:9000/bookings/create`

- booking delete:

`curl -X POST -H "Content-Type: application/json" -d '{"bookingID":2}' http://localhost:9000/bookings/delete`

- get all bookings for one room:

`curl -X GET "http://localhost:9000/bookings/list?room_id=5"`
