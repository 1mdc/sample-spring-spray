package com.sky.assignment;

import com.sky.assignment.model.Recommendations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This controller is an API inteface for recommendation engine
 */
@Controller
@RequestMapping("/recs")
public class RecsController {
    private static final Logger logger = LogManager.getLogger(RecsController.class);

    private RecsEngine recsEngine;

    @Autowired
    public RecsController(RecsEngine recsEngine) {
        this.recsEngine = recsEngine;
    }

    /**
     * This API is to generate recommendations
     *
     * @param numberOfRecs number of recommendations to generate
     * @param start timestamp of beginning window for which to generate recommendations
     * @param end timestamp of ending of window for which to generate recommendations
     * @param subscriber unique identifier of subscriber
     * @return filtered recommendations in XML format
     */
    @RequestMapping(value = {"/personalised"}, method = RequestMethod.GET)
    @ResponseBody
    public Recommendations getPersonalisedRecommendations(@RequestParam("num") Long numberOfRecs,
                                                          @RequestParam("start") Long start,
                                                          @RequestParam("end") Long end,
                                                          @RequestParam("subscriber") String subscriber) {
        logger.info(String.format("Generating (%s) XML for %s from %s to %s", numberOfRecs, subscriber, start, end));
        return recsEngine.recommend(subscriber,numberOfRecs, start, end);
    }
}
