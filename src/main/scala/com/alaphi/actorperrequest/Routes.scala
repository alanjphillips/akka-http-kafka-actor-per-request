package com.alaphi.actorperrequest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.util.{Failure, Success}
import akka.http.scaladsl.model.StatusCodes._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._

import scala.concurrent.duration._

case class Sent(details: String)
case class Command(somethingToDo: SomethingToDo)

class Routes(asyncInitiatorActor: ActorRef) {

  implicit val timeout =  Timeout(5 seconds)

  val workerRoutes = {
    path("service" / "work") {
      post {
        entity(as[SomethingToDo]) { somethingToDo =>
          val done: Future[SomethingToDoResponse] = (asyncInitiatorActor ? Command(somethingToDo)).mapTo[SomethingToDoResponse]
          onComplete(done) {
            case Success(s) => complete(s.name)
            case Failure(f) => complete(BadRequest -> "Failed")
          }
        }
      }
    }
  }

}

object Routes {
  def apply(asyncInitiatorActor: ActorRef): Routes = new Routes(asyncInitiatorActor)
}