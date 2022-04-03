package orders;

import com.ya.orders.Order;
import com.ya.orders.OrderData;
import com.ya.orders.OrdersClient;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static com.ya.DataForTest.clearOrderTestData;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Story("User can create custom order")
@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final String[] color;
    private final int expectedStatusCode;
    private int track;

    public OrderCreationTest(String[] color, int expectedStatusCode) {
        this.color = color;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"BLACK", ""}, SC_CREATED},
                {new String[]{"BLACK", "GREY"}, SC_CREATED},
                {new String[]{"", ""}, SC_CREATED}
        };
    }

    @After
    public void tearDown() {
        clearOrderTestData(track);
    }

    /*можно указать один из цветов — BLACK или GREY;
    можно указать оба цвета;
    можно совсем не указывать цвет;
    тело ответа содержит track*/
    @Test
    @DisplayName("Create a new orders with different colors")
    @Description("User can choose any color - black or grey, or can make an order without color. Postcondition: delete created order.")
    public void createOrderWithDifferentColor() {
        Allure.addAttachment("Color: ", Arrays.toString(color));

        OrdersClient ordersClient = new OrdersClient();
        OrderData orderData = OrdersClient.getRandomOrderData();
        orderData.setColor(color);
        Order order = new Order(orderData.getFirstName(), orderData.getLastName(), orderData.getAddress(), orderData.getMetroStation(),
                orderData.getPhone(), orderData.getRentTime(), orderData.getDeliveryDate(), orderData.getComment(), orderData.getColor());
        ValidatableResponse createOrderResponse = ordersClient.createOrder(order);
        int statusCode = createOrderResponse.extract().statusCode();
        track = createOrderResponse.extract().path("track");

        assertThat("The status code is " + statusCode, statusCode, equalTo(expectedStatusCode));
        assertThat("Some data were wrong", track, notNullValue());
    }
}