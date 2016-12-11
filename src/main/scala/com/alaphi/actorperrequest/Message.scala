package com.alaphi.actorperrequest

case class Message[A <: Payload](origin: String, payload: A)
