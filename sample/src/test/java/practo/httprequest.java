package practo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class httprequest {

    @Test
    public void testPopulationByCity() {
        // Set the base URI for RestAssured
        RestAssured.baseURI = "https://countriesnow.space/api/v0.1/countries/population/cities";

        // Create the JSON body for the request
        String requestBody = "{\"city\": \"Lagos\"}";

        // Send the POST request and capture the response
        Response initialResponse = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/v0.1/countries/population/cities");

        // Check for redirect (status code 301)
        if (initialResponse.getStatusCode() == 301) {
           
            Response redirectedResponse = given().when()
                   .get("https://countriesnow.space/api/v0.1/countries/population/cities/q?city=Lagos");

            // Assert that the status code is 200
            redirectedResponse.then().statusCode(200);

            // Validate the data in the response
            redirectedResponse.then().body("data.city", Matchers.equalToIgnoringCase("Lagos"));
            redirectedResponse.then().body("data.country", Matchers.equalTo("Nigeria"));

            // Validate JSON schema
            InputStream schema = getClass().getResourceAsStream("/schema.json");
            redirectedResponse.then().body(matchesJsonSchema(schema));
        } else {
            // Assert that the status code is 200
            initialResponse.then().statusCode(200);

            // Validate the data in the response
            initialResponse.then().body("data.city", Matchers.equalToIgnoringCase("Lagos"));
            initialResponse.then().body("data.country", Matchers.equalTo("Nigeria"));

            // Validate JSON schema
            InputStream schema = getClass().getResourceAsStream("/schema.json");
            initialResponse.then().body(matchesJsonSchema(schema));
        }
    }
}
