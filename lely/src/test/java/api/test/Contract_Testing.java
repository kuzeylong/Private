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
        Response response = UserEndPoint.readUsers();
        response.then().log().all();

        response.then().statusCode(200);

        List<Integer> ids = response.jsonPath().getList("data.id", Integer.class);

        
        List<Integer> invalidIds = new ArrayList<>(); // To store non-4-digit IDs
        List<Integer> nullIds = new ArrayList<>();    // To store null IDs

     
        ids.forEach(id -> {
            if (id == null) {
                nullIds.add(id); // Add null IDs to nullIds list
            } else if (!id.toString().matches("\\d{4}")) {
                invalidIds.add(id); // Add non-4-digit IDs to invalidIds list
            }
        });

      
        if (!nullIds.isEmpty()) {
            System.err.println("Validation Error: Found " + nullIds.size() + " null IDs.");
        }

        if (!invalidIds.isEmpty()) {
            System.err.println("Validation Error: Found " + invalidIds.size() + " non-4-digit IDs:");
            invalidIds.forEach(id -> System.err.println("Invalid ID: " + id));
        }

       
        assertThat("There should be no null IDs", nullIds.isEmpty(), is(true));
        assertThat("There should be no non-4-digit IDs", invalidIds.isEmpty(), is(true));
    }
}