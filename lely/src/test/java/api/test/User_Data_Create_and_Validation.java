package api.test;

import api.endpoints.UserEndPoint;
import api.payload.UserData;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;

import static org.hamcrest.Matchers.equalTo;

public class User_Data_Create_and_Validation {

    Faker faker;
    UserData userPayload;

    @BeforeClass
    public void setupData() {
        // Initialize Faker to generate random test data
        faker = new Faker();
        userPayload = new UserData();

        // Generate random user data
        userPayload.setName(faker.name().firstName()); // Generate a random first name
        userPayload.setEmail(faker.internet().safeEmailAddress()); // Generate a random safe email address
        userPayload.setGender(faker.options().option("male", "female")); // Randomly choose male or female
        userPayload.setStatus(faker.options().option("active", "inactive")); // Randomly set status as active or inactive
    }

    @Test
    public void testCreateAndGetUser() {
        // Step 1: Send a POST request to create a new user
        Response postResponse = UserEndPoint.createUser(userPayload);

        // Log the response details for debugging purposes
        postResponse.then().log().all();

        // Validate the response for the POST request
        postResponse.then().statusCode(201) // Check that the HTTP status code is 201 (Created)
                .body("data.name", equalTo(userPayload.getName())) // Verify the name matches the payload
                .body("data.email", equalTo(userPayload.getEmail())) // Verify the email matches the payload
                .body("data.gender", equalTo(userPayload.getGender())) // Verify the gender matches the payload
                .body("data.status", equalTo(userPayload.getStatus())); // Verify the status matches the payload

        // Step 2: Extract the user ID from the response
        int userId = postResponse.jsonPath().getInt("data.id");
        userPayload.setId(userId); // Store the user ID in the payload for later use

        // Step 3: Send a GET request to retrieve the user by ID
        Response getResponse = UserEndPoint.readUserById(userId);

        // Validate the response for the GET request
        getResponse.then().statusCode(200) // Check that the HTTP status code is 200 (OK)
                .body("data.id", equalTo(userPayload.getId())) // Verify the ID matches the created user's ID
                .body("data.name", equalTo(userPayload.getName())) // Verify the name matches the created user's name
                .body("data.email", equalTo(userPayload.getEmail())) // Verify the email matches the created user's email
                .body("data.gender", equalTo(userPayload.getGender())) // Verify the gender matches the created user's gender
                .body("data.status", equalTo(userPayload.getStatus())); // Verify the status matches the created user's status
    }
}
