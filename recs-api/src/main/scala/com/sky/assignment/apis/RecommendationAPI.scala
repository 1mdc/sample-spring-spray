package com.sky.assignment.apis

import akka.actor.ActorSystem
import com.sky.assignment.models.{Recommendation, Recommendations}
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * This object implements APIs interact with Recommendation engine
 */
object RecommendationAPI {
  val logger = LoggerFactory.getLogger(RecommendationAPI.getClass)
  implicit val system = ActorSystem("recs-api-recommendation")

  //deserializer that is to convert XML string from Recommendation engine to higher level data structure (recommendation object)
  implicit val RecommendationsUnmarshaller: HttpResponse => Recommendations = { response =>
    val xmlDocument = scala.xml.XML.loadString(response.entity.asString)
    var recommendations:Seq[Recommendation] = Seq()
    for(node <- xmlDocument \ "recommendations"){
      recommendations = recommendations :+ Recommendation((node \ "uuid").text,(node \ "start").text.toLong,(node \ "end").text.toLong)
    }
    Recommendations(recommendations)
  }

  //format url to retrieve recommendations
  val RECOMMENDATION_URL = "http://localhost:8080/recs/personalised?num=%s&start=%s&end=%s&subscriber=%s"

  //a pipeline that process request and then returned a deserialised recommendation objects
  //Spring boot automatically return XML or JSON according to accept type.
  // If we request application/json here, it return Json, so we do not need this API to convert from xml to json.
  // However, to demonstrate the work, I forced it return xml here by adding a header and then parse into json
  val pipeline = addHeader("Accept", "application/xml") ~> sendReceive ~> RecommendationsUnmarshaller

  /**
   * Get recommendations
   *
   * @param nbOfRequest number of requests to retrieve recommendations
   * @param nbOfRecomPerRequest number of recommendation retrieve per request
   * @param subscriber subscriber who retrieve recommendations
   * @return recommendations
   */
  def getRecommendation(nbOfRequest:Int, nbOfRecomPerRequest:Int, subscriber:String):Future[Seq[Recommendations]] = {
    var start = System.currentTimeMillis
    var end = System.currentTimeMillis + 60 * 60 * 1000

    //this variable keeps all futures of request. We will accumulate the result at return step
    var listPipelines:List[Future[Recommendations]] = List()
    for(i <- 1 to nbOfRequest) {
      val url = RECOMMENDATION_URL.format(nbOfRecomPerRequest,start,end, subscriber)
      logger.debug("Get recommendation for %s at %s".format(subscriber, url))
      listPipelines ::= pipeline(Get(url))
      start = end
      end = end + 60 * 60 * 1000
    }

    //converting list of futures into future of a future of list of request and wait for the result
    Future.sequence(listPipelines)
  }
}
