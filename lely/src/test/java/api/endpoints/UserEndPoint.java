package api.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import api.payload.UserData;

/**
 * This class contains CRUD operations for User endpoint.
 */
public class UserEndPoint {

    // Create a new user with Authorization header
    public static Response createUser(UserData payload) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer 1db9c9b6c959682be7c96f74ca532c3cb0bd331f46b86a92602f8d319481b6f5") // Authorization header
                .body(payload)
                .when()
                .post(Routes.post_users_url);
    }

    // Read all users
    public static Response readUsers() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer 1db9c9b6c959682be7c96f74ca532c3cb0bd331f46b86a92602f8d319481b6f5") // Authorization header
                .when()
                .get(Routes.get_users_url);
    }

    // Read a user by ID
    public static Response readUserById(int id) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer 1db9c9b6c959682be7c96f74ca532c3cb0bd331f46b86a92602f8d319481b6f5") // Authorization header
                .pathParam("id", id)
                .when()
                .get(Routes.get_user_by_id_url);
    }
}
