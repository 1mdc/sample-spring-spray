package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PercentCompleteFilter implements RecFilter {
    private static final Logger logger = LogManager.getLogger(PercentCompleteFilter.class);

    //The threshold of specify to discard recommendations that are running past it
    final private double THRESHOLD_RATIO = 0.6;

    /**
     * this filter discard recommendations that running time is past 60%
     * for example if recommendation start time is 8:00 and end time is 9:00 then
     * this recommendation should be discarded if timeslot start is past 8:36, which is 60% of total show time
     *
     * @param r
     * @param start
     * @param end
     * @return
     */
    @Override
    public boolean isRelevant(Recommendation r, long start, long end) {
        long threshold = r.start + (long)((r.end - r.start)*THRESHOLD_RATIO);
        logger.debug(String.format("requested %s-%s. Recomendation: %s-%s. Threshold: %s", start, end, r.start, r.end, threshold));
        if(start > threshold) {
            logger.debug(String.format("rejected: %s", r.uuid));
            return false;
        }else {
            return true;
        }
    }
}
