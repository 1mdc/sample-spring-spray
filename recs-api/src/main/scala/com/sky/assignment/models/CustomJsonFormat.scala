package com.sky.assignment.models

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
 * Created by hoang on 22/07/15.
 */
object CustomJsonFormat extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val RecommendationJson = jsonFormat(Recommendation,"uuid", "start", "end")
  implicit val RecommendationsJson = jsonFormat(Recommendations, "recommendations")
}
