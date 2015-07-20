package com.sky.assignment

import akka.actor.ActorSystem
import com.sky.assignment.apis.RecommendationAPI
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.slf4j.LoggerFactory
import spray.routing.SimpleRoutingApp
import org.json4s.native.Serialization.write

object Application extends App with SimpleRoutingApp {
  val logger = LoggerFactory.getLogger(Application.getClass)

  //we need this to write case class to JSON
  implicit val jsonFormats = Serialization.formats(NoTypeHints)

  implicit val system = ActorSystem("recs")

  startServer(interface = "localhost", port = 8090) {
    path("personalised" / Segment) { subscriber =>
      get {
        complete {
          write(RecommendationAPI.getRecommendation(3,5,subscriber))
        }
      }
    }
  }
}
