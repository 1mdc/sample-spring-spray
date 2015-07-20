package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.junit.Test;

import java.util.Calendar;
import java.util.UUID;

public class PercentCompleteFilterTest {
    PercentCompleteFilter filter = new PercentCompleteFilter();

    @Test
    public void testThatFilterPassesAll() {
        Calendar rStart = Calendar.getInstance();
        rStart.set(2015, 7, 15, 8, 0, 0);
        Calendar rEnd = Calendar.getInstance();
        rEnd.set(2015, 7, 15, 9, 0, 0);
        Calendar slotStart = Calendar.getInstance();
        slotStart.set(2015,7,15,8,40,0);
        Calendar slotEnd = Calendar.getInstance();
        slotEnd.set(2015, 7, 15, 9, 40, 0);
        Calendar slotStart2 = Calendar.getInstance();
        slotStart2.set(2015,7,15,8,30,0);
        Calendar slotEnd2 = Calendar.getInstance();
        slotEnd2.set(2015,7,15,9,30,0);

        org.junit.Assert.assertEquals(filter.isRelevant(new Recommendation(UUID.randomUUID().toString(), rStart.getTimeInMillis(), rEnd.getTimeInMillis()), slotStart.getTimeInMillis(), slotEnd.getTimeInMillis()), false);
        org.junit.Assert.assertEquals(filter.isRelevant(new Recommendation(UUID.randomUUID().toString(), rStart.getTimeInMillis(), rEnd.getTimeInMillis()), slotStart2.getTimeInMillis(), slotEnd2.getTimeInMillis()), true);
    }
}
