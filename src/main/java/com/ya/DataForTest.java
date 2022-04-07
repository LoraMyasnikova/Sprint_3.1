package com.ya;

import com.ya.courier.Courier;
import com.ya.courier.CourierClient;
import com.ya.courier.CourierCredentials;
import com.ya.orders.OrdersClient;
import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DataForTest {

    public static void clearCourierTestData(int courierId) {
        CourierClient courierClient = new CourierClient();
        ValidatableResponse deleteCourierResponse = courierClient.delete(courierId);
        int statusCodeForDelete = deleteCourierResponse.extract().statusCode();

        assertThat("Can't delete a user with this id", statusCodeForDelete, equalTo(SC_OK));
    }

    public static void clearCourierTestData(Courier courier) {
        CourierClient courierClient = new CourierClient();

        CourierCredentials courierCredentials = CourierCredentials.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();

        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCodeForLogin = loginResponse.extract().statusCode();

        if (statusCodeForLogin == SC_OK) {
            int courierId = loginResponse.extract().path("id");
            ValidatableResponse deleteCourierResponse = courierClient.delete(courierId);
            int statusCodeForDelete = deleteCourierResponse.extract().statusCode();

            assertThat("Can't delete a user with this id", statusCodeForDelete, equalTo(SC_OK));
        }
    }

    public static void clearOrderTestData(int track) {
        OrdersClient ordersClient = new OrdersClient();
        ValidatableResponse deleteOrderResponse = ordersClient.deleteOrder(track);
        int statusCodeForDeleteOrder = deleteOrderResponse.extract().statusCode();

        assertThat("Can't delete an order with this track", statusCodeForDeleteOrder, equalTo(SC_OK));
    }
}