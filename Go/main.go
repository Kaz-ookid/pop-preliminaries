package main

import (
	"log"
	"net/http"
	"strconv"
	"sync"

	"github.com/gorilla/websocket"
)

const PORT = 8080

var PORT_STR = ":" + strconv.Itoa(PORT)

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true // allows connections from any origin :)
	},
}

// connectionCounter keeps a separate counter for each connection.
var connectionCounter = make(map[*websocket.Conn]int)

// countersMutex is used to protect connectionCounter from concurrent access.
var countersMutex sync.Mutex

// echoHandler handles WebSocket connections and messages.
func echoHandler(w http.ResponseWriter, r *http.Request) {
	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println("Error upgrading to WebSocket:", err)
		return
	}
	defer conn.Close()

	countersMutex.Lock()
	connectionCounter[conn] = 0
	countersMutex.Unlock()

	for {
		_, message, err := conn.ReadMessage()
		if err != nil {
			log.Println("Error reading message:", err)
			break
		}
		log.Printf("Received: %s\n", message)

		receivedNumber, err := strconv.Atoi(string(message))
		if err != nil {
			log.Println("Error converting message to int:", err)
			continue
		}

		countersMutex.Lock()
		connectionCounter[conn] += receivedNumber

		counterMessage := strconv.Itoa(connectionCounter[conn])
		countersMutex.Unlock()
		err = conn.WriteMessage(websocket.TextMessage, []byte(counterMessage))
		if err != nil {
			log.Println("Error writing message:", err)
			break
		}
		log.Printf("Sent: %s\n", counterMessage)
	}
}

func main() {
	http.HandleFunc("/echo", echoHandler)
	log.Printf("WebSocket server starting on port %s ...\n", PORT_STR)
	err := http.ListenAndServe(PORT_STR, nil)
	if err != nil {
		log.Println("Error starting server:", err)
	}
}
