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

  val producer = KafkaProducer(system, materializer, executionContext)

  val asyncInitiatorActor = system.actorOf(AsyncInitiatorActor.props(producer))

  val routes = Routes(asyncInitiatorActor).workerRoutes

  val bindingFuture = Http().bindAndHandle(routes, "0.0.0.0", 8081)

}
