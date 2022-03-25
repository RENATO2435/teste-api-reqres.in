import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ReqresTest {
    String userEndpoint = "https://reqres.in/api/users";

    @Test
    public void getList() {
        given()
                .param("page", 1)
                .when()
                .get(userEndpoint)
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("page"))
                .body("$", Matchers.hasKey("per_page"))
                .body("$", Matchers.hasKey("total"))
                .body("$", Matchers.hasKey("total_pages"))
                .body("$", Matchers.hasKey("data"))
                .body("data.size()", Is.is(6));
    }

    @Test
    public void getUser() {

        get(userEndpoint + "/1")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("data"))
                .body("data", Matchers.hasKey("id"))
                .body("data", Matchers.hasKey("email"))
                .body("data", Matchers.hasKey("first_name"))
                .body("data", Matchers.hasKey("last_name"))
                .body("data", Matchers.hasKey("avatar"))
                .body("data.id", equalTo(1));
    }

    @Test
    public void getNotFoundUser() {

        get(userEndpoint + "/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void postUser() {
        String newUser = "{\"name\":\"Eduardo\", \"job\":\"baker\"}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(userEndpoint)
                .then()
                .statusCode(201)
                .body("$", Matchers.hasKey("name"))
                .body("$", Matchers.hasKey("job"))
                .body("$", Matchers.hasKey("id"))
                .body("$", Matchers.hasKey("createdAt"))
                .body("name", equalTo("Eduardo"))
                .body("job", equalTo("baker"));
    }

    @Test
    public void patchUser() {
        String newUser = "{\"job\":\"baker\"}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .patch(userEndpoint + "/2")
                .then()
                .statusCode(200)
                .body("$", Matchers.hasKey("job"))
                .body("$", Matchers.hasKey("updatedAt"))
                .body("job", equalTo("baker"));
    }
}
