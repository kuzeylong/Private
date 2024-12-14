package api.test;

import api.endpoints.UserEndPoint;
import api.payload.UserData;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

public class Duplicate_Email_Test {

    Faker faker;
    UserData userPayload;

    @BeforeClass
    public void setupData() {
        // Initialize Faker library for generating random test data
        faker = new Faker();
        userPayload = new UserData();

        // Generate random user data for testing
        userPayload.setName(faker.name().firstName()); // Generate a random first name
        userPayload.setEmail(faker.internet().safeEmailAddress()); // Generate a random safe email address
        userPayload.setGender(faker.options().option("male", "female")); // Randomly assign male or female
        userPayload.setStatus(faker.options().option("active", "inactive")); // Randomly set status as active or inactive
    }

    @Test
    public void testDuplicateEmailValidation() {
        // Send a POST request to create a new user
        Response firstResponse = UserEndPoint.createUser(userPayload);

        // Log the details of the first response for debugging
        firstResponse.then().log().all();

        // Validate that the first user creation request was successful
        firstResponse.then().statusCode(201)
                .body("data.email", equalTo(userPayload.getEmail())); // Verify the email in the response matches the payload

        // Attempt to create a user with the same email (duplicate email)
        Response duplicateResponse = UserEndPoint.createUser(userPayload);

        // Log the details of the duplicate response for debugging
        duplicateResponse.then().log().all();

        // Validate that the duplicate email request fails with a 422 status code
        duplicateResponse.then().statusCode(422); 

        // Extract the error message from the response body
        String errorMessage = duplicateResponse.jsonPath().getString("data.message[0]");
        System.err.println("Error Message: " + errorMessage); // Print the error message to the console for reference

        // Assert that the error message indicates a duplicate email issue
        assertThat("Error message should indicate duplicate email", errorMessage, containsString("has already been taken"));
    }
}


