package com.alaphi.actorperrequest

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class AsyncInitiatorActor(producer: KafkaProducer)(implicit system: ActorSystem) extends Actor {

  // create DispatchAndReceiveActor for each incoming http request, pass this actorRef so Kafka response consumer can pass response

  override def receive: Receive =  {
    case Command(somethingToDo) =>
      system.actorOf(DispatchAndReceiveActor.props(producer, sender)) ! SomethingToDo
  }

}

object AsyncInitiatorActor {
  def props(producer: KafkaProducer)(implicit system: ActorSystem): Props = {
    Props(new AsyncInitiatorActor(producer))
  }
}