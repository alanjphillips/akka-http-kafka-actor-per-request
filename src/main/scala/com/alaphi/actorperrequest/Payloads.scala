package com.alaphi.actorperrequest

trait Payload

case class SomethingToDo(name: String) extends Payload

case class SomethingToDoResponse(name: String) extends Payload