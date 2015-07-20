package com.sky.assignment;

import com.sky.assignment.filters.RecFilter;
import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@CacheConfig(cacheNames = "recommendations")
public class RecsEngine {
    private static final Logger logger = LogManager.getLogger(RecsEngine.class);

    private static final int MAX_LOOPS = 10000;
    private static final int MIN_DURATION = 30;
    private static final int RANDOM_DURATION = 90;
    private static final int MAX_BEGIN_END = 300;

    private Random random = new Random(System.currentTimeMillis());

    private RecFilter[] filters;

    @Autowired
    public RecsEngine(RecFilter[] filters) {
        for(RecFilter f : filters)
            logger.info(String.format("Injected %s filter for generating recommendations",f.getClass()));
        this.filters = filters;
    }

    /**
     * Generate multiple recommendations with start and end time
     *
     * @param subscriber subscriber who receives recommendation. This value is used for caching generated recommendations for each subscriber
     * @param numberOfRecs number of recommendations to generate
     * @param start start time of filtering recommendation
     * @param end end time of filtering recommendation
     * @return random recommendations
     */
    @Cacheable
    public Recommendations recommend(String subscriber, long numberOfRecs, long start, long end) {
        List<Recommendation> recs = new ArrayList<Recommendation>();
        int loops = 0;
        do {
            Recommendation r = generate();
            if (runFilters(r, start, end)) {
                recs.add(r);
            }
        } while (recs.size() < numberOfRecs && ++loops < MAX_LOOPS);

        return new Recommendations(recs);
    }

    /**
     * Generate one random recommendation
     *
     * @return a recommendation
     */
    public Recommendation generate() {
        // generate random recommendation, with random start and end time
        DateTime start = randomStartTime();
        DateTime end = start.plus(randomDuration());
        return new Recommendation(UUID.randomUUID().toString(), start.getMillis(), end.getMillis());
    }
    
    private DateTime randomStartTime() {
        DateTime now = DateTime.now();
        return random.nextBoolean() ? now.plusMinutes(random.nextInt(MAX_BEGIN_END)) : now.minusMinutes(random.nextInt(MAX_BEGIN_END));
    }
    
    private Duration randomDuration() {
        return Duration.standardMinutes(MIN_DURATION + random.nextInt(RANDOM_DURATION));
    }

    private boolean runFilters(Recommendation r, long start, long end) {
        for (RecFilter f: filters) {
            if (!f.isRelevant(r, start, end)) {
                return false;
            }
        }
        return true;
    }
}
