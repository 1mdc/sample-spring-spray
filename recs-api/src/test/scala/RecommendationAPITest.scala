import com.sky.assignment.apis.RecommendationAPI
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class RecommendationAPITest extends FlatSpec with Matchers {
  "RecommendationAPI" should "retrieves enough recommendations" in {
    val recommendations = Await.result(RecommendationAPI.getRecommendation(3,5,"user"), Duration.Inf)
    recommendations.size should be (3)
    recommendations(0).recommendations.size should be (5)
    recommendations(1).recommendations.size should be (5)
    recommendations(2).recommendations.size should be (5)
  }
}
