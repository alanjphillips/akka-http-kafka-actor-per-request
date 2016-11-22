package com.alaphi.actorperrequest

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration._


class SomethingToDoService(asyncInitiatorActor: ActorRef) {

  implicit val timeout =  Timeout(5 seconds)

  def doSomething(somethingToDo: SomethingToDo): Future[SomethingToDoResponse] = {
    (asyncInitiatorActor ? Command(somethingToDo)).mapTo[SomethingToDoResponse]
  }

}