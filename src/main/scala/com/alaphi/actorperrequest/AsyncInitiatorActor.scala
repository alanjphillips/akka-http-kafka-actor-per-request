package com.alaphi.actorperrequest

import akka.actor.{Actor, ActorSystem, Props}

class AsyncInitiatorActor(producer: KafkaProducer)(implicit system: ActorSystem) extends Actor {

  override def receive: Receive =  {
    case Command(somethingToDo) =>
      system.actorOf(DispatchAndReceiveActor.props(producer, sender)) ! somethingToDo
  }

}

object AsyncInitiatorActor {
  def props(producer: KafkaProducer)(implicit system: ActorSystem): Props = {
    Props(new AsyncInitiatorActor(producer))
  }
}