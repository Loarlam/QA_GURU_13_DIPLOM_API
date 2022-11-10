package in.reqres.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    static DataForTheTest dataForTheTest = new DataForTheTest();

    @BeforeAll
    static void settingURI() {
        RestAssured.baseURI = dataForTheTest.baseUrl;
    }
}
