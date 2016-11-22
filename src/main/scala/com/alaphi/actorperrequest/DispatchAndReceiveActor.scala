package com.alaphi.actorperrequest

import akka.actor.{Actor, ActorRef, Props}

class DispatchAndReceiveActor(producer: KafkaProducer, origin: ActorRef) extends Actor {

  override def receive: Receive = {
    case s: SomethingToDo =>
      context.become(responseHandler)
      self ! SomethingToDoResponse(s"${s.name} : ${self.path}")
  }

  def responseHandler: Receive = {
    case resp: SomethingToDoResponse =>
      origin ! resp
      context.stop(self)
  }

}

object DispatchAndReceiveActor {
  def props(producer: KafkaProducer, origin: ActorRef): Props = {
    Props(new DispatchAndReceiveActor(producer, origin))
  }
}