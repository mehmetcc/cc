package org.mehmetcc.user;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

// TODO add some tests for update and delete endpoints, if there's some time
// TODO these endpoints are not going to be used anyways, so this is not too urgent for now
@QuarkusTest
class UserResourceTest {
    @Test
    public void givenValidUser_shouldReturnCreated() {
        given()
                .contentType("application/json")
                .body("""
                        {
                          "firstName": "String",
                          "lastName": "Stringoglu"
                        }
                        """)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .body("firstName", is("String"))
                .body("lastName", is("Stringoglu"))
                .body("isActive", is(true));
    }

    @Test
    public void givenInvalidUser_shouldReturnServerError() {
        given()
                .contentType("application/json")
                .body("""
                        {
                          "firstName": "String"
                        }
                        """)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(500);
    }

    @Test
    public void givenInvalidRequest_shouldReturnBadRequest() {
        given()
                .contentType("application/json")
                .body("""
                        {
                          "firstName": "String"
                          "lastName": "Stringoglu",
                        }
                        """)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(400);
    }
}