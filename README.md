Extremely simple WebSocket-based, client-server system across 4 different programming languages, to familiarizing myself with their libraries and specific paradigms. 

The server (Go, Scala) will need to be listening on port X (e.g. 8080) of the local machine for incoming WebSocket connections.
A client (TypeScript/Web, Java/Android) can connect to the local machine on port X (when the user clicks on a “connect” button of your choice).
Every time a client connects to the server, the server will initialize a new counter (at 0) for that WebSocket connection.
The client can send messages containing raw numbers over the WebSocket connection. Whenever the server receives a number, it will increment the counter associated with the WebSocket connection by that number, and send back a message containing the current value of the counter.
Both clients and servers should log all incoming/outgoing communications.

For now, I have built working Go (and Scala) server and Android client that can communicate together.


