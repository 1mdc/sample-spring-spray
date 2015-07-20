package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;

public interface RecFilter {

    /**
     * Implementing recommendation filter
     *
     * @param r input recommendation for filtering
     * @param start start time of recommendation
     * @param end end time of recommendation
     * @return BOOLEAN of accept or discard the recommendation
     */
    public boolean isRelevant(Recommendation r, long start, long end);
}
