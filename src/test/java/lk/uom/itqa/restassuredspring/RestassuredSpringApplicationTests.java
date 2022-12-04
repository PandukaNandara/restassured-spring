package lk.uom.itqa.restassuredspring;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.*;

@Data
class Book {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String author;
}

@SpringBootTest
@RequiredArgsConstructor
class RestassuredSpringApplicationTests {

    private final static String BASE_URL = "http://localhost:7081";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void testDeleteBookingRA001() {
        Book book = createBook();
        String path = "/api/books/" + book.getId();

        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("user"))
                .delete(path)
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    void testDeleteBookThatIsNotExistsRA004() {
        String path = "/api/books/" + 12039;
        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("user"))
                .delete(path)
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    void testDeleteUsingAdminRA002() {
        Book book = createBook();
        String path = "/api/books/" + book.getId();
        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("admin"))
                .delete(path)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void passStringToDeleteIdRA003() {
        String path = "/api/books/" + "THIS_IS_A_STRING";
        given()
                .baseUri(BASE_URL)
                .header(getAuthHeaderForRestAssured("user"))
                .delete(path)
                .then()
                .assertThat()
                .statusCode(400);
    }


    @Test
    void deleteWithoutAuthRA005() {
        String path = "/api/books/" + 2134;
        given()
                .baseUri(BASE_URL)
                .delete(path)
                .then()
                .assertThat()
                .statusCode(401);
    }

    private Header getAuthHeaderForRestAssured(String role) {
        return new Header("Authorization", "Basic " + getBasicAuth(role));
    }

    private Book createBook() {
        Book booking = new Book();
        var rand = Math.round(Math.random() * 100000);
        booking.setAuthor("Test Book Author " + rand);
        booking.setTitle("Test Book Title " + rand);
        String basicAuth = getBasicAuth();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + basicAuth);
        HttpEntity<Book> request = new HttpEntity<>(booking, headers);

        var rst = restTemplate.exchange(BASE_URL + "/api/books", HttpMethod.POST, request, Book.class);
        return rst.getBody();
    }
    private String getBasicAuth() {
        return getBasicAuth("admin");
    }
    private String getBasicAuth(String role) {
        String plainCreds = role + ":password";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes);
    }
}
