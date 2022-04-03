package orders;

import com.ya.orders.Order;
import com.ya.orders.OrderData;
import com.ya.orders.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.ya.DataForTest.clearOrderTestData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("Forming orders list")
public class OrderListTest {

    private int track;
    OrdersClient ordersClient = new OrdersClient();

    @Before
    public void setUp() {
        OrderData orderData = OrdersClient.getRandomOrderData();
        Order order = new Order(orderData.getFirstName(), orderData.getLastName(), orderData.getAddress(), orderData.getMetroStation(),
                orderData.getPhone(), orderData.getRentTime(), orderData.getDeliveryDate(), orderData.getComment(), orderData.getColor());

        ValidatableResponse createOrderResponse = ordersClient.createOrder(order);
        track = createOrderResponse.extract().path("track");
    }

    @After
    public void tearDown() {
        clearOrderTestData(track);
    }

    @Test
    @DisplayName("Get an orders list")
    @Description("Get the list of all the orders in the system. Precondition: create an order. Postcondition: delete created order.")
    public void getOrderList() {
        ValidatableResponse getOrderListResponse = ordersClient.getOrderList();
        int statusCode = getOrderListResponse.extract().statusCode();
        ArrayList<String> ordersList = getOrderListResponse.extract().path("orders");

        assertThat("", statusCode, equalTo(SC_OK));
        assertThat("", ordersList, notNullValue());
    }
}