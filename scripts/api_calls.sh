#!/bin/bash

curl localhost:8080/

curl localhost:8080/books

echo "Check health of the application"
curl -X GET http://localhost:8080

echo -e "\nConsume the external API"
curl http://localhost:8080/spaceship

http -v "http://localhost:8080/book"

http -v DELETE "http://ilocalhost:8080/book/19"

http -v PUT "http://172.27.176.1:8080/book" id=18 author=me title="The Enigma code" price=999.99f

http -v POST "http://172.27.176.1:8080/book/123" author=me title="How to run ktor" price=123


# try authentication on dummy API endpoint
http -v -a "user:password" "http://localhost:8080/api/try-auth"

# get book list ordered by title in ascending order
http -v -a 'user:password' "http://localhost:8080/book/list" sort==title order==asc

# get book list ordered by title in descending order with page 2 and page size 5
 http -v -a 'user:password' "http://172.21.192.1:8080/book/list" sort==title order==asc page==2 pageSize==5
