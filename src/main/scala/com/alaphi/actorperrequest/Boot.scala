package com.alaphi.actorperrequest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory


object Boot extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val config = ConfigFactory.load()

  val consumer = system.actorOf(ResponseDeliveryActor.props)

  val producer = KafkaProducer()

  val asyncInitiatorActor = system.actorOf(AsyncInitiatorActor.props(producer))

  val somethingToDoService = SomethingToDoService(asyncInitiatorActor)

  val routes = Routes(somethingToDoService)

  val bindingFuture = Http().bindAndHandle(routes.workerRoutes, "0.0.0.0", 8081)

}
