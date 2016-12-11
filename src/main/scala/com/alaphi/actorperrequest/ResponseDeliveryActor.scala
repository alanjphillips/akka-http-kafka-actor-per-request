package com.alaphi.actorperrequest

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.{ExecutionContext, Future}

import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._

class ResponseDeliveryActor(implicit as: ActorSystem,
                            mat: Materializer,
                            ec: ExecutionContext) extends Actor with ActorLogging {


  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = {
    val consumerSettings = ConsumerSettings(as, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("kafka:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val done =
      Consumer.committableSource(consumerSettings, Subscriptions.topics("app_commands"))
        .mapAsync(1) { msg =>
          println(s"Received message from kafka: $msg")

          val origin = decode[Message[SomethingToDoResponse]](msg.record.value)

          origin map { m =>
            println(s"Received Message from Kafka origin=${m.origin} : payload=${m.payload}")
        //    as.actorSelection(m.origin) ! m.payload
          }

          Future.successful(msg)

        }
        .mapAsync(1) { msg =>
          msg.committableOffset.commitScaladsl()
        }
        .runWith(Sink.ignore)
  }

}

object ResponseDeliveryActor {
   def props(implicit as: ActorSystem, mat: Materializer, ec: ExecutionContext): Props = {
     Props(new ResponseDeliveryActor)
   }
}
