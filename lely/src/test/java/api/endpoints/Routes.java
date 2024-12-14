package api.endpoints;

/*
 * This class stores the API routes for the application under the test.
 *
 */
public class Routes {

    public static String base_url = "https://gorest.co.in/public/v1";

    public static  String get_users_url =  base_url + "/users";
    public static  String post_users_url = base_url + "/users";
    public static  String get_user_by_id_url = base_url + "/users/{id}";
}
