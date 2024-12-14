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
        faker = new Faker();
        userPayload = new UserData();

        userPayload.setName(faker.name().firstName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setGender(faker.options().option("male", "female"));
        userPayload.setStatus(faker.options().option("active", "inactive"));
    }

    @Test
    public void testCreateAndGetUser() {
        // Create user
        Response postResponse = UserEndPoint.createUser(userPayload);
        postResponse.then().log().all();

        postResponse.then().statusCode(201)
                .body("data.name", equalTo(userPayload.getName()))
                .body("data.email", equalTo(userPayload.getEmail()))
                .body("data.gender", equalTo(userPayload.getGender()))
                .body("data.status", equalTo(userPayload.getStatus()));

        // Extract user ID
        int userId = postResponse.jsonPath().getInt("data.id");
        userPayload.setId(userId);

        // Retrieve user by ID
        Response getResponse = UserEndPoint.readUserById(userId);

        getResponse.then().statusCode(200)
                .body("data.id", equalTo(userPayload.getId()))
                .body("data.name", equalTo(userPayload.getName()))
                .body("data.email", equalTo(userPayload.getEmail()))
                .body("data.gender", equalTo(userPayload.getGender()))
                .body("data.status", equalTo(userPayload.getStatus()));
    }
}

