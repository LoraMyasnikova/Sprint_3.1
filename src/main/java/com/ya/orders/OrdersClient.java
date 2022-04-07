package com.ya.orders;

import com.ya.ScooterRestClient;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static io.restassured.RestAssured.given;

public class OrdersClient extends ScooterRestClient {

    private static final String ORDER_CREATION_PATH = "/api/v1/orders";
    private static final String ORDER_DELETION_PATH = "/api/v1/orders/cancel/";
    private static final String GET_ORDER_PATH = "/api/v1/orders/track";
    private static final String GET_ORDER_LIST_PATH = "/api/v1/orders";

    @Step("Create an order")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_CREATION_PATH)
                .then();
    }

    @Step("Get an order by track")
    public ValidatableResponse getOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("t", track)
                .get(GET_ORDER_PATH)
                .then();
    }

    @Step("Delete an order")
    public ValidatableResponse deleteOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("track", track)
                .put(ORDER_DELETION_PATH)
                .then();
    }

    @Step("Get the all orders list")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_ORDER_LIST_PATH)
                .then();
    }

    @Step("Create random order data")
    public static OrderData getRandomOrderData() {

        final String firstName = RandomStringUtils.randomAlphabetic(10);
        final String lastName = RandomStringUtils.randomAlphabetic(10);
        final String address = RandomStringUtils.randomAlphabetic(10);
        final String metroStation = RandomStringUtils.randomAlphabetic(10);
        final String phone = RandomStringUtils.randomNumeric(11);
        final int rentTime = RandomUtils.nextInt(0, 5);
        final String comment = RandomStringUtils.randomAlphabetic(10);
        final String[] color = new String[2];

        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, 1);
        Date dateDeliveryDate = date.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String deliveryDate = dateFormat.format(dateDeliveryDate);

        Allure.addAttachment("First Name: ", firstName);
        Allure.addAttachment("Last Name: ", lastName);
        Allure.addAttachment("Address: ", address);
        Allure.addAttachment("Metro Station: ", metroStation);
        Allure.addAttachment("Phone: ", phone);
        Allure.addAttachment("Rent Time: ", String.valueOf(rentTime));
        Allure.addAttachment("Delivery Date: ", deliveryDate);
        Allure.addAttachment("Comment: ", comment);

        return new OrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}