package example

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import akka.stream.{Materializer, OverflowStrategy}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.Try

object WebsocketServer extends App {
  implicit val system: ActorSystem = ActorSystem("websocket-server")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = Materializer(system)
  val log = Logging(system, getClass)

  var counter = 0

  def newClientFlow: Flow[Message, Message, Any] = Flow[Message]
    .collect {
      case TextMessage.Strict(text) =>
        Try(text.toInt) match {
          case scala.util.Success(number) =>
            counter += number
            log.info(s"Received and incremented counter by $number, new counter is $counter")
            TextMessage(s"Counter: $counter")
          case scala.util.Failure(_) =>
            log.info(s"Received non-integer message: $text")
            TextMessage(s"Counter unchanged: $counter")
        }
    }
    .watchTermination()((_, done) => done.onComplete {
      case scala.util.Success(_) => log.info("WebSocket connection closed normally.")
      case scala.util.Failure(exception) => log.error(exception, "WebSocket connection closed with error.")
    })

  val websocketRoute =
    path("echo") {
      handleWebSocketMessages(newClientFlow)
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bindFlow(websocketRoute)

  println(s"Server is now online at http://localhost:8080\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete { _ =>
      log.info("Server is shutting down...")
      system.terminate()
    }
}
