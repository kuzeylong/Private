package api.test;

import api.endpoints.UserEndPoint;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Contract_Testing {

    @Test
    public void testValidateUserIdsLength() {
        // Send a request to read all users and store the response
        Response response = UserEndPoint.readUsers();

        // Log the full response details for debugging purposes
        response.then().log().all();

        // Validate that the HTTP response status is 200 (OK)
        response.then().statusCode(200);

        // Extract the list of user IDs from the JSON response
        // The "data.id" path should correspond to the JSON structure
        List<Integer> ids = response.jsonPath().getList("data.id", Integer.class);

        // Initialize lists to store invalid and null IDs
        List<Integer> invalidIds = new ArrayList<>(); // To store non-4-digit IDs
        List<Integer> nullIds = new ArrayList<>();    // To store null IDs

        // Iterate over each ID to validate its format
        ids.forEach(id -> {
            if (id == null) {
                // Add null IDs to the nullIds list for reporting
                nullIds.add(id);
            } else if (!id.toString().matches("\\d{4}")) {
                // Add IDs that are not exactly 4 digits to the invalidIds list
                invalidIds.add(id);
            }
        });

        // Log an error message if any null IDs are found
        if (!nullIds.isEmpty()) {
            System.err.println("Validation Error: Found " + nullIds.size() + " null IDs.");
        }

        // Log an error message if any invalid IDs are found
        if (!invalidIds.isEmpty()) {
            System.err.println("Validation Error: Found " + invalidIds.size() + " non-4-digit IDs:");
            invalidIds.forEach(id -> System.err.println("Invalid ID: " + id));
        }

        // Assert that there are no null IDs in the response
        assertThat("There should be no null IDs", nullIds.isEmpty(), is(true));

        // Assert that there are no IDs that are not exactly 4 digits long
        assertThat("There should be no non-4-digit IDs", invalidIds.isEmpty(), is(true));
    }
}
