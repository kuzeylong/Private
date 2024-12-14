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
        faker = new Faker();
        userPayload = new UserData();

        // Generate random user data
        userPayload.setName(faker.name().firstName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setGender(faker.options().option("male", "female"));
        userPayload.setStatus(faker.options().option("active", "inactive"));
    }

    @Test
    public void testDuplicateEmailValidation() {
        // Step 1: Create user with unique email
        Response firstResponse = UserEndPoint.createUser(userPayload);
        firstResponse.then().log().all();

        // Verify that the user is created successfully
        firstResponse.then().statusCode(201)
                .body("data.email", equalTo(userPayload.getEmail()));

        // Step 2: Attempt to create user again with the same email
        Response duplicateResponse = UserEndPoint.createUser(userPayload);
        duplicateResponse.then().log().all();

        // Verify that the user is not created and appropriate error is returned
        duplicateResponse.then().statusCode(422); // 422 Unprocessable Entity

        // Extract and log the error message
        String errorMessage = duplicateResponse.jsonPath().getString("data.message[0]");
        System.err.println("Error Message: " + errorMessage);

        // Assert that the error message mentions the duplicate email
        assertThat("Error message should indicate duplicate email", errorMessage, containsString("has already been taken"));
    }
}

