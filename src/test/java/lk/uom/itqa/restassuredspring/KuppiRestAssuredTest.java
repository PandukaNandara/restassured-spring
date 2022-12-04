package lk.uom.itqa.restassuredspring;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*; // Optional

@SpringBootTest
@RequiredArgsConstructor
public class KuppiRestAssuredTest {

    @Test
    void test1() {
        when()
                .get("https://reqres.in/api/users?page=2") // post, delete, put, patch
                .then()
                .body("page", equalTo(2))

                .statusCode(200);
    }

    @Test
    void test2() {
        given()
                .baseUri("https://reqres.in")
                .get("/api/users?page=2")
                .then()
                .header("content-type", "application/json; charset=utf-8");
    }
}
