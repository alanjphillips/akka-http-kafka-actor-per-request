package com.alaphi.actorperrequest

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import io.circe.generic.auto._
import io.circe.syntax._

class DispatchAndReceiveActor(producer: KafkaProducer, origin: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case s: SomethingToDo =>
      log.info(s"DispatchAndReceiveActor Received: ${s}")
      context.become(responseHandler)
      val msg = Message(self.path.toStringWithAddress(self.path.address), SomethingToDoResponse(s.name)).asJson
      log.info(s"msg.noSpaces: ${msg.noSpaces}")
      producer.sendToKafka(msg.noSpaces)
  }

  def responseHandler: Receive = {
    case resp: SomethingToDoResponse =>
      log.info(s"Almost there, received $resp")
      origin ! resp
      context.stop(self)

    case Message(originStr, payload) =>
      context.stop(self)
  }

}

object DispatchAndReceiveActor {
  def props(producer: KafkaProducer, origin: ActorRef): Props = {
    Props(new DispatchAndReceiveActor(producer, origin))
  }
}