package com.ya.courier;

import com.ya.ScooterRestClient;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";

    @Step("Login in the system")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then();
    }

    @Step("Register new courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Delete a courier with ID = {courierId}")
    public ValidatableResponse delete(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + "/" + courierId)
                .then();
    }

    @Step("Create random credentials")
    public static Courier getRandomCredentials() {
        final String courierLogin = RandomStringUtils.randomAlphabetic(10);
        final String courierPassword = RandomStringUtils.randomAlphabetic(10);
        final String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        Allure.addAttachment("Login: ", courierLogin);
        Allure.addAttachment("Password: ", courierPassword);
        Allure.addAttachment("First name: ", courierFirstName);

        return new Courier(courierLogin, courierPassword, courierFirstName);
    }
}