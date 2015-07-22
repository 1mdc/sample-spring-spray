package com.sky.assignment

import akka.actor.ActorSystem
import com.sky.assignment.apis.RecommendationAPI
import org.slf4j.LoggerFactory
import spray.routing.SimpleRoutingApp

object Application extends App with SimpleRoutingApp {

  val logger = LoggerFactory.getLogger(Application.getClass)

  //this is require to marshalling case class object in CustomJsonFormat. Because the returned object is in Future, so future context of akka needed to be passed in.
  implicit def executionContext = actorRefFactory.dispatcher

  implicit val system = ActorSystem("recs")

  startServer(interface = "localhost", port = 8090) {
    path("personalised" / Segment) { subscriber =>
      get {
        complete {
          //This is where I defined custom marshalling protocols
          import models.CustomJsonFormat._
          RecommendationAPI.getRecommendation(3,5,subscriber)
        }
      }
    }
  }
}
