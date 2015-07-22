package com.sky.assignment.filters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {com.sky.assignment.Application.class})
@WebIntegrationTest
@IntegrationTest("server.port:8080")
public class IntegrateTestRequest {
    @Test
    public void sameSubscriberShouldBeCachedRequests() {
        String r1 = when()
                .get("/recs/personalised?num={num}&start={start}&end={end}&subscriber={subscriber}", 5, 1415286463203L, 1415294605557L, "asd")
                .then()
                .extract().response().asString();
        String r2 = when()
                .get("/recs/personalised?num={num}&start={start}&end={end}&subscriber={subscriber}", 5, 1415286463203L, 1415294605557L, "asd")
                .then()
                .extract().response().asString();
        org.junit.Assert.assertEquals(r1, r2);
    }
    @Test
    public void differentSubscriberShouldNotBeCachedRequests() {
        String r1 = when()
                .get("/recs/personalised?num={num}&start={start}&end={end}&subscriber={subscriber}", 5, 1415286463203L, 1415294605557L, "asd")
                .then()
                .extract().response().asString();
        String r2 = when()
                .get("/recs/personalised?num={num}&start={start}&end={end}&subscriber={subscriber}", 5, 1415286463203L, 1415294605557L, "asd2")
                .then()
                .extract().response().asString();
        org.junit.Assert.assertNotEquals(r1, r2);
    }
}
