package com.sky.assignment

import akka.actor.ActorSystem
import spray.routing.SimpleRoutingApp

object Application extends App with SimpleRoutingApp {

  implicit val system = ActorSystem("recs")

  startServer(interface = "localhost", port = 8090) {
    path("personalised" / Segment) { subscriber =>
      get {
        complete {
          <h1>Here there should be recommendations for {subscriber} in json format</h1>
        }
      }
    }
  }
}
