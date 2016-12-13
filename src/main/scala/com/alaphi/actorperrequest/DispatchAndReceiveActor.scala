package com.alaphi.actorperrequest

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import io.circe.generic.auto._
import io.circe.syntax._

class DispatchAndReceiveActor(producer: KafkaProducer, origin: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case s: SomethingToDo =>
      context.become(responseHandler)
      val msg = Message(self.path.toStringWithAddress(self.path.address), SomethingToDoResponse(s.name)).asJson
      producer.sendToKafka(msg.noSpaces)
  }

  def responseHandler: Receive = {
    case resp: SomethingToDoResponse =>
      log.info(s"Got Response from Kafka, received: $resp, sending to ${origin}")
      origin ! resp
      context.stop(self)
  }

}

object DispatchAndReceiveActor {
  def props(producer: KafkaProducer, origin: ActorRef): Props = {
    Props(new DispatchAndReceiveActor(producer, origin))
  }
}