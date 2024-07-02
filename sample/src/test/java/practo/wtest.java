package practo;


import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class wtest {

    @Test
    public void testWeatherInLondon() {
        // Set the base URI for RestAssured
        String url = "https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02";

        // Send the GET request and get the response
        Response response = given().when().get(url);
        // Assert that the status code is 200
        response.then().statusCode(200);

        // Validate the data in the response
        response.then().body("base", Matchers.equalTo("stations"));
        response.then().body("name", Matchers.equalTo("London"));

        // Validate JSON schema
        InputStream schema = getClass().getResourceAsStream("/wschema.json");
        response.then().body(matchesJsonSchema(schema));
    }
}
