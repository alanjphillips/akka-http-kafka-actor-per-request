package com.alaphi.actorperrequest

import akka.http.scaladsl.server.Directives._
import scala.util.{Failure, Success}
import akka.http.scaladsl.model.StatusCodes._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.auto._


case class Sent(details: String)
case class Command(somethingToDo: SomethingToDo)

class Routes(somethingToDoService: SomethingToDoService) {

  val workerRoutes = {
    path("service" / "work") {
      post {
        entity(as[SomethingToDo]) { somethingToDo =>
          onComplete(somethingToDoService.doSomething(somethingToDo)) {
            case Success(s) => complete(s.name)
            case Failure(f) => complete(BadRequest -> s"Failed: $f ")
          }
        }
      }
    }
  }

}

object Routes {
  def apply(somethingToDoService: SomethingToDoService): Routes = new Routes(somethingToDoService)
}