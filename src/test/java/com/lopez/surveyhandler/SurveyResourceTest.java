package com.lopez.surveyhandler;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class SurveyResourceTest {

    @Test
    public void testHealthEndpoint() {
        given()
          .when().get("/api/v1/health")
          .then()
             .statusCode(200)
             .body(containsString("healthy"));
    }
}